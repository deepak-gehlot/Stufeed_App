package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.RowBoardMemberBinding;

/**
 * Created by Deepak Gehlot on 4/9/2018.
 */

public class BoardMemberListAdapter extends RecyclerView.Adapter<BoardMemberListAdapter.ViewHolder> {

    private Context context;

    public BoardMemberListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBoardMemberBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout
                .row_board_member, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowBoardMemberBinding binding;

        public ViewHolder(RowBoardMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
