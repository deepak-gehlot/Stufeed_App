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
import com.stufeed.android.databinding.RowTotalLikesBinding;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.MyHolder> {
    private Context context;
    private ArrayList<GetAllLikes.Like> likeArrayList;
    private String mLoginUserId = "";

    public LikeListAdapter(Context context, ArrayList<GetAllLikes.Like> likeArrayList) {
        this.context = context;
        this.likeArrayList = likeArrayList;
        this.mLoginUserId = mLoginUserId;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowTotalLikesBinding likesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_total_likes, parent, false);
        return new MyHolder(likesBinding);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        GetAllLikes.Like likes = likeArrayList.get(position);
        holder.likesBinding.setModel(likes);
        Utility.setUserTypeIconColor(context, likes.getUserType(), holder.likesBinding.userTypeView);


    }

    @Override
    public int getItemCount() {
        return likeArrayList == null ? 0 : likeArrayList.size();

    }

    class MyHolder extends RecyclerView.ViewHolder {
        private RowTotalLikesBinding likesBinding;

        public MyHolder(RowTotalLikesBinding likesBinding) {
            super(likesBinding.getRoot());
            this.likesBinding = likesBinding;
        }
    }
}
