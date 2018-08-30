package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetNotificationResponse;
import com.stufeed.android.databinding.RowNotiItemBinding;
import com.stufeed.android.view.activity.UserProfileActivity;
import com.stufeed.android.view.activity.ViewPostActivity;

import java.util.ArrayList;

public class NotificationListItemAdapter extends RecyclerView.Adapter<NotificationListItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetNotificationResponse.NotiItem> notiItems;

    public NotificationListItemAdapter(Context context, ArrayList<GetNotificationResponse.NotiItem> notiItems) {
        this.context = context;
        this.notiItems = notiItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowNotiItemBinding mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_noti_item, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setModel(notiItems.get(position));

        String text = notiItems.get(position).getUnMsg();
        holder.mBinding.textView.setText(Html.fromHtml(text));
    }

    @Override
    public int getItemCount() {
        return notiItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowNotiItemBinding mBinding;

        public ViewHolder(RowNotiItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetNotificationResponse.NotiItem item = notiItems.get(getAdapterPosition());
                    switch (item.getUnType()) {
                        case "like":
                            String postIdLike = item.getUnPostId();
                            Intent intent = new Intent(context, ViewPostActivity.class);
                            intent.putExtra("post_id", postIdLike);
                            intent.putExtra("for", "like");
                            context.startActivity(intent);
                            break;
                        case "comment":
                            String postIdComment = item.getUnPostId();
                            Intent intentComment = new Intent(context, ViewPostActivity.class);
                            intentComment.putExtra("post_id", postIdComment);
                            intentComment.putExtra("for", "comment");
                            context.startActivity(intentComment);
                            break;
                        case "follow":
                        case "add_board":
                        case "board":
                            GetCollegeUserResponse.User user = new GetCollegeUserResponse.User();
                            user.setUserId(item.getUserId());
                            user.setIsFollow("0");
                            Intent intentUser = new Intent(context, UserProfileActivity.class);
                            intentUser.putExtra(UserProfileActivity.USER, user);
                            context.startActivity(intentUser);
                            break;
                    }
                }
            });
        }
    }
}