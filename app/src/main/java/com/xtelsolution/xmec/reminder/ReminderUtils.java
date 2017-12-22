package com.xtelsolution.xmec.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.reminder.receiver.ReminderReceiver;
import com.xtelsolution.xmec.reminder.service.ReminderService;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/9/2017
 * Email: leconglongvu@gmail.com
 */
public class ReminderUtils {

    private static final String TAG = "ReminderUtils";

    public static void startService(Context context, int shceduleID) {
        Intent intent = new Intent(context, ReminderService.class);
        intent.putExtra(Constants.OBJECT, shceduleID);
        context.startService(intent);
    }

    public static void startService(Context context, int id, int repeat) {
        Intent intent = new Intent(context, ReminderService.class);
        intent.putExtra(Constants.OBJECT, id);
        intent.putExtra(Constants.OBJECT_2, repeat);
        context.startService(intent);
    }

    public static void startReminderSchedule(Context context, ScheduleDrug scheduleDrug, long repeat) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(Constants.OBJECT, scheduleDrug.getId());

        if (alarmManager != null) {
            if (scheduleDrug.getState() == 2) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduleDrug.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(pendingIntent);
            } else {

                if (scheduleDrug.getScheduleTimePerDays().size() > 0 && scheduleDrug.getScheduleTimePerDays().get(0).getScheduleType() == 1) {
                    Collections.sort(scheduleDrug.getScheduleTimePerDays(), new Comparator<ScheduleTimePerDay>() {
                        @Override
                        public int compare(ScheduleTimePerDay lhs, ScheduleTimePerDay rhs) {
                            try {
                                long time1 = TimeUtils.convertTimeToLong(lhs.getStartTime());
                                long time2 = TimeUtils.convertTimeToLong(rhs.getStartTime());

                                return (int) (time1 - time2);
                            } catch (Exception e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                }

                long timeReminder;

                if (repeat != -1)
                    timeReminder = System.currentTimeMillis() + (repeat * 60 * 1000);
                else
                    timeReminder = getStartTime(scheduleDrug);

                MyApplication.log(TAG, "startReminderSchedule " + timeReminder);

                if (timeReminder == -1) {
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduleDrug.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.cancel(pendingIntent);
                } else {
                    intent.putExtra(Constants.OBJECT_2, timeReminder);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduleDrug.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeReminder, pendingIntent);
                }
            }
        }
    }

    public static void cancelReminderSchedule(Context context, int id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }


    /**
     * DayDuration - số ngày uống
     * 0 - Không giới hạn
     * <p>
     * Period Type - kiểu chu kỳ:
     * 1: Hằng ngày
     * 2: Kiểu cách ngày
     * 3: Kiểu Quartz
     * <p>
     * Period
     * 1. Kiểu hằng ngày: null
     * 2. Kiểu cách ngày [số]. Vd: 2
     * 3. Kiểu của Quartz:
     * Các ngày trong tuần [1-7], cn là 1. Vd: 2,4,6
     */
    private static long getStartTime(ScheduleDrug scheduleDrug) {

        switch (scheduleDrug.getPeriodType()) {
            case 1:
                return getTimeHangNgay(scheduleDrug.getDateCreateLong(),
                        scheduleDrug.getDayDuration(),
                        scheduleDrug.getScheduleTimePerDays());
            case 2:
                return getTimeCachNgay(scheduleDrug.getDateCreateLong(),
                        scheduleDrug.getDayDuration(), scheduleDrug.getPeriod(),
                        scheduleDrug.getScheduleTimePerDays());
            case 3:
                return getTimeTrongTuan(scheduleDrug.getDateCreateLong(),
                        scheduleDrug.getDayDuration(), scheduleDrug.getPeriod(),
                        scheduleDrug.getScheduleTimePerDays());
            default:
                return -1;
        }
    }

    /**
     * DayDuration - số ngày uống
     * 0 - Không giới hạn
     */
    private static long getTimeHangNgay(long timeStart, int dayDuration, RealmList<ScheduleTimePerDay> realmList) {
        long timeNow = (TimeUtils.getTodayLong());
        Long timeEnd = dayDuration > 0 ? (timeStart + (dayDuration * AlarmManager.INTERVAL_DAY)) : null;

        if (timeEnd != null && timeEnd < timeNow) {
            MyApplication.log(TAG, "getTimeHangNgay 1 " + timeStart);
            return -1;
        }

        long timeSelected;

        if (timeStart >= timeNow) {
            timeSelected = getTimePerDay(timeStart, realmList);
        } else {
            timeSelected = getTimePerDay(timeNow, realmList);
        }

        MyApplication.log(TAG, "getTimeHangNgay 2 " + timeStart);

        if (timeSelected == -1) {
            long timeTomorrow = timeNow + AlarmManager.INTERVAL_DAY;

            if (timeEnd != null && timeTomorrow <= timeEnd) {
                timeSelected = getTimePerDay(timeTomorrow, realmList);
                MyApplication.log(TAG, "getTimeHangNgay 3 " + timeStart);
            }
        }

        return timeSelected;
    }

    /**
     * DayDuration - số ngày uống
     * 0 - Không giới hạn
     * <p>
     * Period
     * 2. Kiểu cách ngày [số]. Vd: 2
     */
    private static long getTimeCachNgay(long timeStart, int dayDuration, String period, RealmList<ScheduleTimePerDay> realmList) {
        long timeNow = (TimeUtils.getTodayLong());
        Long timeEnd = dayDuration > 0 ? (timeStart + (dayDuration * AlarmManager.INTERVAL_DAY)) : null;

        if (timeEnd != null && timeEnd < timeNow) {
            return -1;
        }

        long timeSelected = -1;

        if (timeStart >= timeNow) {
            timeSelected = getTimePerDay(timeStart, realmList);
        }

        if (timeSelected == -1) {
            int periodDay = Integer.parseInt(period);
            long timeNextDay = timeStart + (periodDay * AlarmManager.INTERVAL_DAY);

            while (true) {
                if (timeEnd != null && timeNextDay > timeEnd)
                    return timeSelected;

                if (timeNextDay >= timeNow) {
                    timeSelected = getTimePerDay(timeNextDay, realmList);

                    if (timeSelected != -1)
                        return timeSelected;
                }

                timeNextDay += periodDay * AlarmManager.INTERVAL_DAY;
            }
        }

        return timeSelected;
    }

    private static long getTimeTrongTuan(long timeStart, int dayDuration, String period, RealmList<ScheduleTimePerDay> realmList) {
        long timeNow = (TimeUtils.getTodayLong());
        Long timeEnd = dayDuration > 0 ? (timeStart + (dayDuration * AlarmManager.INTERVAL_DAY)) : null;

        if (timeEnd != null && timeEnd < timeNow) {
            return -1;
        }

        Calendar calendar = Calendar.getInstance();

        String periodDay[] = period.split(",");

        long timeSelected = -1;

        if (timeStart > timeNow) {
            calendar.setTimeInMillis(timeStart);
            int today = calendar.get(Calendar.DAY_OF_WEEK);

            for (String aPeriodDay : periodDay) {
                if (today == Integer.parseInt(aPeriodDay))
                    timeSelected = getTimePerDay(timeStart, realmList);
            }

            if (timeSelected == -1) {
                for (String aPeriodDay : periodDay) {
                    int day = Integer.parseInt(aPeriodDay);

                    if (day > today) {
                        long timeNexDay = timeStart + ((day - today) * AlarmManager.INTERVAL_DAY);

                        if (timeEnd != null && timeNexDay > timeEnd) {
                            return -1;
                        }

                        timeSelected = getTimePerDay(timeNexDay, realmList);
                        break;
                    }
                }
            }

            if (timeSelected == -1) {
                long nextWeek = timeStart + (7 * AlarmManager.INTERVAL_DAY);

                calendar.setTimeInMillis(nextWeek);
                calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(periodDay[0]));

                if (timeEnd != null && calendar.getTimeInMillis() > timeEnd) {
                    return -1;
                }

                timeSelected = getTimePerDay(calendar.getTimeInMillis(), realmList);
            }
        } else {
            calendar.setTimeInMillis(timeNow);
            int today = calendar.get(Calendar.DAY_OF_WEEK);

            for (String aPeriodDay : periodDay) {
                if (today == Integer.parseInt(aPeriodDay))
                    timeSelected = getTimePerDay(timeNow, realmList);
            }

            if (timeSelected == -1) {
                for (String aPeriodDay : periodDay) {
                    int day = Integer.parseInt(aPeriodDay);

                    if (day >= today) {
                        long timeNexDay = timeNow + ((day - today) * AlarmManager.INTERVAL_DAY);

                        if (timeEnd != null && timeNexDay > timeEnd) {
                            return -1;
                        }

                        timeSelected = getTimePerDay(timeNexDay, realmList);
                        break;
                    } else if (day == 1) {
                        day = 8;
                        long timeNexDay = timeNow + ((day - today) * AlarmManager.INTERVAL_DAY);

                        if (timeEnd != null && timeNexDay > timeEnd) {
                            return -1;
                        }

                        timeSelected = getTimePerDay(timeNexDay, realmList);
                        break;
                    }
                }
            }

            if (timeSelected == -1) {
                long nextWeek = timeNow + (7 * AlarmManager.INTERVAL_DAY);

                calendar.setTimeInMillis(nextWeek);
                calendar.set(Calendar.DAY_OF_WEEK, Integer.parseInt(periodDay[0]));

                if (timeEnd != null && calendar.getTimeInMillis() > timeEnd) {
                    return -1;
                }

                timeSelected = getTimePerDay(calendar.getTimeInMillis(), realmList);
            }
        }

        MyApplication.log("ReminderUtils", "getTimeTrongTuan " + timeSelected);
        return timeSelected;
    }

    /**
     * 1: Theo lần
     * 2: Theo giờ
     */
    private static long getTimePerDay(long startTime, RealmList<ScheduleTimePerDay> realmList) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);

        if (realmList != null && realmList.size() > 0)
            if (realmList.get(0) != null && realmList.get(0).getScheduleType() == 1) {
                for (int i = 0; i < realmList.size(); i++) {
                    String[] time = realmList.get(i).getStartTime().split(":");

                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    long timeSelected = calendar.getTimeInMillis();

                    MyApplication.log(TAG, "getTimePerDay " + i + " time " +
                            time[0] + " " + time[1] + " " + timeSelected);

                    if (timeSelected > System.currentTimeMillis()) {
                        return timeSelected;
                    }
                }

                return -1;
            } else {
                ScheduleTimePerDay scheduleTimePerDay = realmList.get(0);

                String[] splitStart = scheduleTimePerDay.getStartTime().split(":");
                String[] splitEnd = scheduleTimePerDay.getEndTime().split(":");
                long timeS;
                long timeE;
                long currentTime = System.currentTimeMillis();
                int hour = scheduleTimePerDay.getTime();

                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitStart[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(splitStart[1]));
                timeS = calendar.getTimeInMillis();

                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitEnd[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(splitEnd[1]));
                timeE = calendar.getTimeInMillis();

                if (timeE < currentTime) {
                    return -1;
                }

                if (timeS > currentTime) {
                    return timeS;
                }

                long selectTime = timeS;
                selectTime += hour * AlarmManager.INTERVAL_HOUR;

                while (selectTime <= timeE) {
                    if (selectTime > currentTime) {
                        return selectTime;
                    }

                    selectTime += hour * AlarmManager.INTERVAL_HOUR;
                }

            }
        return -1;
    }

    /**
     * Period Type - kiểu chu kỳ:
     * 1: Hằng ngày
     * 2: Kiểu cách ngày
     * 3: Kiểu Quartz
     *
     * DayDuration - số ngày uống
     * 0 - Không giới hạn
     *
     * Period
     * 1. Kiểu hằng ngày: null
     * 2. Kiểu cách ngày [số]. Vd: 2
     * 3. Kiểu của Quartz:
     * Các ngày trong tuần [1-7], cn là 1. Vd: 2,4,6
     */
}