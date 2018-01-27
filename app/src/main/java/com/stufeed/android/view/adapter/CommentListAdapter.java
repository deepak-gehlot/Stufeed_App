package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.databinding.RowCommentBinding;

import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetAllCommentResponse.Comment> commentArrayList;

    public CommentListAdapter(Context context, ArrayList<GetAllCommentResponse.Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCommentBinding rowCommentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R
                .layout.row_comment, parent, false);
        return new ViewHolder(rowCommentBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetAllCommentResponse.Comment comment = commentArrayList.get(position);
        holder.rowCommentBinding.setModel(comment);
    }

    @Override
    public int getItemCount() {
        return commentArrayList == null ? 0 : commentArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowCommentBinding rowCommentBinding;

        private ViewHolder(RowCommentBinding rowCommentBinding) {
            super(rowCommentBinding.getRoot());
            this.rowCommentBinding = rowCommentBinding;
        }
    }
}