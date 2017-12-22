package com.xtelsolution.sdk.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.HealthRecordOptionListener;
import com.xtelsolution.sdk.callback.SelectDayListener;
import com.xtelsolution.xmec.R;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/23/2017
 * Email: leconglongvu@gmail.com
 */
public class DialogUtils {

    public static void showHealthRecordOption(Context context, long endDateLong, final HealthRecordOptionListener listener) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.Theme_Transparent);
       bottomSheetDialog.setContentView(R.layout.dialog_hr_option);
        //noinspection ConstantConditions
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        Button btnContinueTreatment = bottomSheetDialog.findViewById(R.id.btn_continue_treatment);
        Button btnEndOfTreatment = bottomSheetDialog.findViewById(R.id.btn_end_of_treatment);
        Button btnEdit = bottomSheetDialog.findViewById(R.id.btn_edit);
        Button btnDelete = bottomSheetDialog.findViewById(R.id.btn_delete);
        View line_1 = bottomSheetDialog.findViewById(R.id.view_1);

        assert btnContinueTreatment != null;
        btnContinueTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                listener.onClicked(1);
            }
        });

        assert btnEndOfTreatment != null;
        btnEndOfTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                listener.onClicked(2);
            }
        });

        assert btnEdit != null;
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                listener.onClicked(3);
            }
        });

        assert btnDelete != null;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                listener.onClicked(4);
            }
        });

        if (endDateLong > 0) {
            btnEndOfTreatment.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            assert line_1 != null;
            line_1.setVisibility(View.GONE);
        } else {
            btnContinueTreatment.setVisibility(View.GONE);
        }

        bottomSheetDialog.show();
    }

    @SuppressWarnings("ConstantConditions")
    public static void showUserDrugOption(Context context, final HealthRecordOptionListener listener) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.Theme_Transparent);
        bottomSheetDialog.setContentView(R.layout.dialog_hr_option);
        //noinspection ConstantConditions
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        Button btnEdit = bottomSheetDialog.findViewById(R.id.btn_edit);
        Button btnDelete = bottomSheetDialog.findViewById(R.id.btn_delete);

        bottomSheetDialog.findViewById(R.id.btn_continue_treatment).setVisibility(View.GONE);
        bottomSheetDialog.findViewById(R.id.btn_end_of_treatment).setVisibility(View.GONE);
        bottomSheetDialog.findViewById(R.id.view_1).setVisibility(View.GONE);

        assert btnEdit != null;
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                listener.onClicked(3);
            }
        });

        assert btnDelete != null;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                listener.onClicked(4);
            }
        });

        bottomSheetDialog.show();
    }

    @SuppressWarnings("ConstantConditions")
    public static void showSharedOption(Context context, final HealthRecordOptionListener listener) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.Theme_Transparent);
        bottomSheetDialog.setContentView(R.layout.dialog_shared_option);
        //noinspection ConstantConditions
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        Button btnEdit = bottomSheetDialog.findViewById(R.id.btn_edit);
        Button btnDelete = bottomSheetDialog.findViewById(R.id.btn_delete);

        assert btnEdit != null;
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                listener.onClicked(3);
            }
        });

        assert btnDelete != null;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                listener.onClicked(4);
            }
        });

        bottomSheetDialog.show();
    }

    public static void showConfirmDeleteDrug(Context context, String drugName, final ConfirmListener listener) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_delete_drug);
        //noinspection ConstantConditions
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        String message = "Bạn có chắc muốn xóa thuốc: <b>" + drugName + "?</b>";

        TextView txtMessage = dialog.findViewById(R.id.txt_message);
        assert txtMessage != null;
        //noinspection deprecation
        txtMessage.setText(Html.fromHtml(message));

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnDelete = dialog.findViewById(R.id.btn_delete);

        assert btnCancel != null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        assert btnDelete != null;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.onConfirmSuccess();
            }
        });

        dialog.show();
    }

    public static void selectDay(Context context, String title, String currentDay, final SelectDayListener listener) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_select_day);
        //noinspection ConstantConditions
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        TextView txtTitle = dialog.findViewById(R.id.txt_title);
        final TextInputEditText edtDay = dialog.findViewById(R.id.edt_day);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnAdd = dialog.findViewById(R.id.btn_add);

        txtTitle.setText(title);
        edtDay.setText(currentDay);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.onDismis();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edtDay.getText().toString();

                if (TextUtils.isEmpty(input)) {
                    edtDay.setError("Vui lòng nhập số ngày");
                    return;
                }

                int day;

                try {
                    day = Integer.parseInt(input);
                } catch (Exception e) {
                    day = -1;
                }

                if (day == -1) {
                    edtDay.setError("Ngày không hợp lệ");
                    return;
                }

                if (day == 0) {
                    edtDay.setError("Số ngày phải từ 1 trở lên");
                    return;
                }

                dialog.dismiss();
                listener.onSet(String.valueOf(day));
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                listener.onDismis();
            }
        });

        dialog.show();

        edtDay.requestFocus();
        KeyboardUtils.showSoftKeyboard(dialog);
    }

    public static void selectPeriodWeek(final Context context, String selected, final SelectDayListener listener) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_period_week);
        //noinspection ConstantConditions
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final CheckBox ck_t2 = dialog.findViewById(R.id.ck_t2);
        final CheckBox ck_t3 = dialog.findViewById(R.id.ck_t3);
        final CheckBox ck_t4 = dialog.findViewById(R.id.ck_t4);
        final CheckBox ck_t5 = dialog.findViewById(R.id.ck_t5);
        final CheckBox ck_t6 = dialog.findViewById(R.id.ck_t6);
        final CheckBox ck_t7 = dialog.findViewById(R.id.ck_t7);
        final CheckBox ck_cn = dialog.findViewById(R.id.ck_cn);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnAdd = dialog.findViewById(R.id.btn_add);

        if (selected != null) {
            String split[] = selected.split(",");

            for (String t : split) {
                switch (t) {
                    case "CN":
                        ck_cn.setChecked(true);
                        break;
                    case "1":
                        ck_cn.setChecked(true);
                        break;
                    case "2":
                        ck_t2.setChecked(true);
                        break;
                    case "3":
                        ck_t3.setChecked(true);
                        break;
                    case "4":
                        ck_t4.setChecked(true);
                        break;
                    case "5":
                        ck_t5.setChecked(true);
                        break;
                    case "6":
                        ck_t6.setChecked(true);
                        break;
                    case "7":
                        ck_t7.setChecked(true);
                        break;
                }
            }
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.onDismis();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ck_t2.isChecked() && !ck_t3.isChecked() && !ck_t4.isChecked() &&
                        !ck_t5.isChecked() && !ck_t6.isChecked() && !ck_t7.isChecked() &&
                        !ck_cn.isChecked()) {

                    Toast.makeText(context, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                    return;
                }

                String text = "*";

                text += ck_t2.isChecked() ? ",2" : "";
                text += ck_t3.isChecked() ? ",3" : "";
                text += ck_t4.isChecked() ? ",4" : "";
                text += ck_t5.isChecked() ? ",5" : "";
                text += ck_t6.isChecked() ? ",6" : "";
                text += ck_t7.isChecked() ? ",7" : "";
                text += ck_cn.isChecked() ? ",1" : "";

                text = text.replace("*,", "");

                dialog.dismiss();

                listener.onSet(text);
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                listener.onDismis();
            }
        });

        dialog.show();
    }
}