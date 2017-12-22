package com.xtelsolution.xmec.view.activity.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.NotifyConfig;
import com.xtelsolution.xmec.model.entity.ObjectNotify;
import com.xtelsolution.xmec.view.activity.inf.notification.INotificationView;

import java.util.ArrayList;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by xtel on 11/8/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    INotificationView view;
    private List<ObjectNotify> notifyList;
    private Context context;
    private int TYPE_SERVER = 3;
    private int TYPE_NORMAL = 1;

    public NotificationAdapter(Context context, INotificationView view) {
        this.notifyList = new ArrayList<>();
        this.context = context;
        this.view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SERVER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_notification_server, parent, false);
            return new ServerHolder(view);
        } else if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_notification_action, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (notifyList.get(position).getNotifyType() == TYPE_SERVER) {
            return TYPE_SERVER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ObjectNotify object = notifyList.get(position);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.swipeLayout.animateReset();
            viewHolder.setData(object, position);
        } else if (holder instanceof ServerHolder) {
            ServerHolder serverHolder = (ServerHolder) holder;
            serverHolder.setData(object, position);

        }
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public ObjectNotify getItem(int position) {
        return notifyList.get(position);
    }

    void updateAction(int position, boolean isAccept, long create) {
//        long create = notifyList.get(position).getDateCreatedLong();
        if (isAccept) {
            notifyList.get(position).setActionState(NotifyConfig.STATE_ACCEPT);
            notifyList.get(position).setDateCreatedLong(create);
            notifyItemChanged(position);
        } else {
            notifyList.get(position).setActionState(NotifyConfig.STATE_DECLINE);
            notifyList.get(position).setDateCreatedLong(create);
            notifyItemChanged(position);
        }
    }

    public void updateDataCollection(List<ObjectNotify> list) {
        if (notifyList.size() > 0) {
            notifyList.clear();
        }
        notifyList.addAll(list);
        notifyDataSetChanged();
        MyApplication.log("Adapter notify", notifyList.toString());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_content, tv_time, tv_state;
        private ImageView img_avatar, img_delete;
        private FrameLayout fr_main_notify;
        private int TYPE_VIEW = 1;
        private int TYPE_ACTION = 2;
        SwipeLayout swipeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            fr_main_notify = itemView.findViewById(R.id.fr_main_notify);

            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);

            img_avatar = itemView.findViewById(R.id.img_avatar);
            img_delete = itemView.findViewById(R.id.img_delete);
            swipeLayout = itemView.findViewById(R.id.swipeLayout);
        }

        public void setData(ObjectNotify object, int position) {
            if (object != null) {
                if (object.getNotifyState() > 0) {
                    itemView.setBackgroundResource(android.R.color.white);
                } else {
                    itemView.setBackgroundResource(R.color.blue_10);
                }


                if (!TextUtils.isEmpty(object.getAvatar())) {
                    WidgetUtils.setImageURL(img_avatar, object.getAvatar(), R.mipmap.ic_small_avatar_default);
                }

                if (object.getNotifyContent() != null) {
                    String result = "";
                    switch (object.getActionState()) {
                        case NotifyConfig.STATE_ACCEPT:
                            if (object.getNotifyType() == NotifyConfig.TYPE_SHARE) {
                                result = "Bạn đã chấp nhận yêu cầu chia sẻ y bạ của <b>" + object.getSenderName() + "</b>";
                            } else if (object.getNotifyType() == NotifyConfig.TYPE_LINK) {
                                result = "Bạn đã chấp nhận yêu cầu liên kết y bạ của <b>" + object.getSenderName() + "</b>";
                            }
                            break;
                        case NotifyConfig.STATE_DECLINE:
                            if (object.getNotifyType() == NotifyConfig.TYPE_SHARE) {
                                result = "Bạn đã từ chối yêu cầu chia sẻ y bạ của <b>" + object.getSenderName() + "</b>";
                            } else if (object.getNotifyType() == NotifyConfig.TYPE_LINK) {
                                result = "Bạn đã từ chối yêu cầu liên kết y bạ của <b>" + object.getSenderName() + "</b>";
                            }
                            break;
                        case NotifyConfig.STATE_PENDING:
                            if (object.getNotifyType() == NotifyConfig.TYPE_SHARE) {
                                result = "<b>" + object.getSenderName() + "</b> muốn chia sẻ y bạ <b>" + object.getMrbName() + "</b> với bạn";
                            } else if (object.getNotifyType() == NotifyConfig.TYPE_LINK) {
                                result = "<b>" + object.getSenderName() + "</b> muốn liên kết y bạ <b>" + object.getMrbName() + "</b> với bạn";
                            }
                            break;
                    }
                    tv_content.setText(Html.fromHtml(result));
                }

                switch (object.getActionState()) {
                    case NotifyConfig.STATE_ACCEPT:
                        tv_state.setVisibility(View.VISIBLE);
                        tv_state.setTextColor(context.getResources().getColor(R.color.login_tabcolor));
                        tv_state.setText("Đã đồng ý");
                        break;
                    case NotifyConfig.STATE_DECLINE:
                        tv_state.setVisibility(View.VISIBLE);
                        tv_state.setTextColor(context.getResources().getColor(R.color.background_color));
                        tv_state.setText("Đã từ chối");
                        break;
                    case NotifyConfig.STATE_PENDING:
                        tv_state.setVisibility(View.GONE);
                        break;
                }
                tv_time.setText(TextUtils.comparingTime(object.getDateCreatedLong()));
                initOnClick(object, position);
            }
        }

        private void initOnClick(final ObjectNotify data, final int position) {
            fr_main_notify.setOnClickListener(view -> actionClick(data, position));

            img_avatar.setOnClickListener(v -> actionClick(data, position));
            tv_content.setOnClickListener(v -> actionClick(data, position));
            tv_time.setOnClickListener(v -> actionClick(data, position));
            tv_state.setOnClickListener(v -> actionClick(data, position));
            img_delete.setOnClickListener(view -> actionDelete(data, position));
        }

        private void actionDelete(ObjectNotify data, int position) {
            swipeLayout.animateReset();
            view.deleteNotification(position, data.getId());
        }

        private void actionClick(ObjectNotify data, int position) {
            swipeLayout.animateReset();
            if (data.getNotifyState() > 0) {
                if (data.getActionState() > 0) {
                    if (data.getNotifyType() == NotifyConfig.TYPE_LINK) {
                        view.getInfomationMbr(data, NotifyConfig.TYPE_LINK, position, TYPE_VIEW);
                    } else if (data.getNotifyType() == NotifyConfig.TYPE_SHARE) {
                        view.getInfomationMbr(data, NotifyConfig.TYPE_SHARE, position, TYPE_VIEW);
                    }
                } else {
                    if (data.getNotifyType() == NotifyConfig.TYPE_LINK) {
                        view.getInfomationMbr(data, NotifyConfig.TYPE_LINK, position, TYPE_ACTION);
                    } else if (data.getNotifyType() == NotifyConfig.TYPE_SHARE) {
                        view.getInfomationMbr(data, NotifyConfig.TYPE_SHARE, position, TYPE_ACTION);
                    }
                }
            } else {
                data.setNotifyState(1);
//                data.setDateCreated(null);
                notifyItemChanged(position);
                view.requestUpdateStateNotify(data, position);
                if (data.getNotifyType() == NotifyConfig.TYPE_LINK) {
                    view.getInfomationMbr(data, NotifyConfig.TYPE_LINK, position, TYPE_ACTION);
                } else if (data.getNotifyType() == NotifyConfig.TYPE_SHARE) {
                    view.getInfomationMbr(data, NotifyConfig.TYPE_SHARE, position, TYPE_ACTION);
                }
            }
        }
    }

    class ServerHolder extends RecyclerView.ViewHolder {

        private TextView tv_content, tv_time, tv_system;


        public ServerHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_system = itemView.findViewById(R.id.tv_system);
        }

        public void setData(ObjectNotify object, int position) {
            if (object != null) {

                if (object.getNotifyState() > 0) {
                    itemView.setBackgroundResource(android.R.color.white);
                } else {
                    itemView.setBackgroundResource(R.color.blue_10);
                }

                if (object.getNotifyContent() != null) {
                    tv_content.setText(object.getNotifyContent());
                }
                tv_time.setText(TextUtils.comparingTime(object.getDateCreatedLong()));
                initOnClick(itemView, object, position);
            }
        }

        private void initOnClick(View itemView, final ObjectNotify data, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setNotifyState(1);
                    notifyDataSetChanged();
                    view.requestUpdateStateNotify(data, position);
                    view.startUrl(data.getContentUrl());
                }
            });
        }
    }

    public void removeItem(int position) {
        notifyList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notifyList.size());
    }
}
