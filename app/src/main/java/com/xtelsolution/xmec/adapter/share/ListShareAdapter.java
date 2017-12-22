//package com.xtelsolution.xmec.adapter.share;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//import com.xtelsolution.MyApplication;
//import com.xtelsolution.sdk.utils.TimeUtils;
//import com.xtelsolution.sdk.utils.WidgetUtils;
//import com.xtelsolution.xmec.R;
//import com.xtelsolution.xmec.model.entity.ShareAccounts;
//import com.xtelsolution.xmec.model.entity.ShareMbrEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * Created by vulcl on 8/12/17
// */
//
//public class ListShareAdapter extends RecyclerView.Adapter<ListShareAdapter.ViewHolder> {
//    private List<ShareAccounts> listData;
//
//    public ListShareAdapter() {
//        listData = new ArrayList<>();
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_share_link, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        ShareAccounts entity = listData.get(position);
//        holder.setData(entity);
//    }
//
//    @Override
//    public int getItemCount() {
//        return listData.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.img_avatar)
//        ImageView img_avatar;
//        @BindView(R.id.img_edit)
//        ImageView img_edit;
//        @BindView(R.id.txt_name)
//        TextView txt_name;
//        @BindView(R.id.txt_birthday)
//        TextView txt_birthday;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//
//        public void setData(ShareAccounts item) {
//            if (item != null) {
//
//                WidgetUtils.setImageURL(img_avatar, item.getAvatar(), R.mipmap.ic_small_avatar_default);
//
//                txt_name.setText(item.getFullname());
//                String day = item.getBirthdayLong() != null ? TimeUtils.convertLongToDate(item.getBirthdayLong()) : null;
//                String birthday = day != null ? day : MyApplication.context.getString(R.string.layout_not_update);
//                txt_birthday.setText(birthday);
//            }
////            if (state == 0)
////                initOnClick(position, data);
////            else
////                img_edit.setVisibility(View.GONE);
//
//        }
//
//        private void initOnClick(final int position, final ShareMbrEntity data) {
//            img_edit.setVisibility(View.VISIBLE);
//            img_edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    view.onClickItem(position, data);
//                }
//            });
//        }
//    }
//}
