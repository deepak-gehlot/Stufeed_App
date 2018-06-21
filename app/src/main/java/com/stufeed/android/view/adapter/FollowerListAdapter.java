package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.databinding.RowMyFollowerBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.UserProfileActivity;

import java.util.ArrayList;

public class FollowerListAdapter extends RecyclerView.Adapter<FollowerListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetFollowerListResponse.User> userArrayList;

    public FollowerListAdapter(Context context, ArrayList<GetFollowerListResponse.User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowMyFollowerBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_my_follower, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetFollowerListResponse.User user = userArrayList.get(position);
        holder.binding.setAdapter(this);
        holder.binding.setUser(user);

        Utility.setUserTypeIconColor(context, user.getUserType(), holder.binding.userTypeView);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowMyFollowerBinding binding;

        ViewHolder(RowMyFollowerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetFollowerListResponse.User user = userArrayList.get(getAdapterPosition());
                    GetCollegeUserResponse.User user1 = new GetCollegeUserResponse.User();
                    user1.setUserId(user.getFollowUserid());
                    user1.setFullName(user.getFullName());
                    user1.setCollegeId("");
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(UserProfileActivity.USER, user1);
                    context.startActivity(intent);
                }
            });
        }
    }
}