package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetJoinBoardListResponse;
import com.stufeed.android.databinding.RowJoinBoardBinding;

import java.util.ArrayList;

/**
 * Created by HP on 3/4/2018.
 */

public class JoinBoardListAdapter extends RecyclerView.Adapter<JoinBoardListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetJoinBoardListResponse.Board> list;

    public JoinBoardListAdapter(Context context, ArrayList<GetJoinBoardListResponse.Board> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowJoinBoardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_join_board,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setAdapter(this);
        holder.binding.setModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowJoinBoardBinding binding;

        private ViewHolder(RowJoinBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}