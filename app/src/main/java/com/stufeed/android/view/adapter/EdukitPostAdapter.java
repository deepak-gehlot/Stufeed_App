package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.RowEdukitItemBinding;

public class EdukitPostAdapter extends RecyclerView.Adapter<EdukitPostAdapter.ViewHolder> {

    private Context context;

    public EdukitPostAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowEdukitItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_edukit_item, parent, false);
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

        private RowEdukitItemBinding binding;

        public ViewHolder(RowEdukitItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}