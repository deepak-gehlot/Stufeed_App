package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
    private ArrayList<Boolean> checkList = new ArrayList<>();
    private int lastPosition = -1;

    public EdukitListAdapter(Context context, ArrayList<EdukitItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        for (int i = 0; i < arrayList.size(); i++) {
            checkList.add(false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowTextViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_text_view, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.textView.setText(arrayList.get(position).getValue());

        if (checkList.get(position)) {
            holder.binding.textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_box_checl, 0, 0, 0);
        } else {
            holder.binding.textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_box_uncheck, 0, 0, 0);
        }
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
                    if (lastPosition != -1) {
                        checkList.set(lastPosition, false);
                        notifyItemChanged(lastPosition);
                    }
                    checkList.set(getAdapterPosition(), true);
                    lastPosition = getAdapterPosition();
                    notifyItemChanged(getAdapterPosition());
                    onItemClickListener.onClick(getAdapterPosition(), null);
                }
            });
        }
    }

    public void onItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
