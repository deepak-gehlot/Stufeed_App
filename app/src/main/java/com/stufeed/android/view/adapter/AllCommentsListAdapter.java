package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.api.response.GetAllLikes;
import com.stufeed.android.databinding.RowTotalCommentsBinding;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;

public class AllCommentsListAdapter extends RecyclerView.Adapter<AllCommentsListAdapter.MyHolder> {
    private Context context;
    private ArrayList<GetAllCommentResponse.Comment> commentsArrayList;


    public AllCommentsListAdapter(Context context, ArrayList<GetAllCommentResponse.Comment> commentsArrayList) {
        this.context = context;
        this.commentsArrayList = commentsArrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowTotalCommentsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_total_comments, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        GetAllCommentResponse.Comment comment = commentsArrayList.get(position);
        holder.binding.setModel(comment);
        Utility.setUserTypeIconColor(context, comment.getUserType(), holder.binding.userTypeView);


    }

    @Override
    public int getItemCount() {
        return commentsArrayList == null ? 0 : commentsArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private RowTotalCommentsBinding binding;

        public MyHolder(RowTotalCommentsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
