package com.stufeed.android.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.RowNotificationBinding;

/**
 * Created by HP on 2/28/2018.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                        .row_notification, parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RowNotificationBinding binding;

        private ViewHolder(RowNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
