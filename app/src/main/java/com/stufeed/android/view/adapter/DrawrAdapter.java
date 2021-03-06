package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.bean.DrawerItem;
import com.stufeed.android.databinding.RowNavigationBinding;
import com.stufeed.android.listener.OnItemClickListener;

import java.util.ArrayList;

public class DrawrAdapter extends RecyclerView.Adapter<DrawrAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DrawerItem> drawerItems;
    private OnItemClickListener onItemClickListener;

    public DrawrAdapter(Context context, ArrayList<DrawerItem> drawerItems) {
        this.context = context;
        this.drawerItems = drawerItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowNavigationBinding rowNavigationBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout
                .row_navigation, parent, false);
        return new ViewHolder(rowNavigationBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.rowNavigationBinding.text.setText(drawerItems.get(position).getText());
        holder.rowNavigationBinding.text.setCompoundDrawablesWithIntrinsicBounds(drawerItems.get(position).getIcon(),
                0, 0, 0);

        holder.rowNavigationBinding.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(holder.getAdapterPosition(),null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowNavigationBinding rowNavigationBinding;

        private ViewHolder(RowNavigationBinding rowNavigationBinding) {
            super(rowNavigationBinding.getRoot());
            this.rowNavigationBinding = rowNavigationBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}