package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.bean.EdukitItem;
import com.stufeed.android.databinding.RowTextViewBinding;
import com.stufeed.android.listener.OnItemClickListener;

import java.util.ArrayList;

public class EdukitListAdapter extends RecyclerView.Adapter<EdukitListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<EdukitItem> arrayList;
    private OnItemClickListener onItemClickListener;

    public EdukitListAdapter(Context context, ArrayList<EdukitItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowTextViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_text_view, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.textView.setText(arrayList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowTextViewBinding binding;

        public ViewHolder(RowTextViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(getAdapterPosition(), null);
                }
            });
        }
    }

    public void onItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
