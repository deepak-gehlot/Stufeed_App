package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetArchiveBoardListResponse;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.databinding.RowArchiveBoardBinding;

import java.util.ArrayList;

/**
 * Created by Deepak Gehlot on 3/8/2018.
 */

public class ArchiveBoardListAdapter extends RecyclerView.Adapter<ArchiveBoardListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetArchiveBoardListResponse.Board> boardArrayList;

    public ArchiveBoardListAdapter(Context context, ArrayList<GetArchiveBoardListResponse.Board> boardArrayList) {
        this.context = context;
        this.boardArrayList = boardArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowArchiveBoardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout
                .row_archive_board, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setAdapter(this);
        holder.binding.setModel(boardArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return boardArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowArchiveBoardBinding binding;

        public ViewHolder(RowArchiveBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}