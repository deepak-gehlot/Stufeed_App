package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetNotificationResponse;
import com.stufeed.android.databinding.RowNotiItemBinding;

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
        RowNotiItemBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_noti_item, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setModel(notiItems.get(position));
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
        }
    }
}