package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.databinding.RowMyFollowerBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.UserProfileActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerListAdapter extends RecyclerView.Adapter<FollowerListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetFollowerListResponse.User> userArrayList;
    private String loginUserId = "";

    public FollowerListAdapter(Context context, ArrayList<GetFollowerListResponse.User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
        loginUserId = Utility.getLoginUserId(context);
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

        if (user.getFollowUserid().equals(loginUserId)) {
            holder.binding.txtFollow.setText("You");
        } else if (user.getIsFollow().equals("1")) {
            holder.binding.txtFollow.setText(context.getString(R.string.un_follow));
        } else {
            holder.binding.txtFollow.setText(context.getString(R.string.follow));
        }

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

            binding.txtFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetFollowerListResponse.User user = userArrayList.get(getAdapterPosition());
                    if (!user.getFollowUserid().equals(loginUserId)) {
                        onClickUnFollow(getAdapterPosition(), user);
                    }
                }
            });

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetFollowerListResponse.User user = userArrayList.get(getAdapterPosition());
                    if (!user.getFollowUserid().equals(loginUserId)) {
                        GetCollegeUserResponse.User user1 = new GetCollegeUserResponse.User();
                        user1.setUserId(user.getFollowUserid());
                        user1.setFullName(user.getFullName());
                        user1.setCollegeId("");
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra(UserProfileActivity.USER, user1);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }


    public void onClickUnFollow(int pos, GetFollowerListResponse.User user) {
        unFollowRequest(pos, user, user.getFollowUserid());
        /*int position = userArrayList.indexOf(user);
        userArrayList.remove(position);
        notifyItemRemoved(position);*/
    }

    /**
     * Request un follow user
     */
    private void unFollowRequest(final int position, final GetFollowerListResponse.User user, String followUserId) {
        Api api = APIClient.getClient().create(Api.class);
        Call<FollowResponse> responseCall = api.follow(loginUserId, followUserId);
        responseCall.enqueue(new Callback<FollowResponse>() {
            @Override
            public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                if (user.getIsFollow().equals("1")) {
                    user.setIsFollow("0");
                } else {
                    user.setIsFollow("1");
                }
                userArrayList.set(position, user);
                notifyItemChanged(position);
            }

            @Override
            public void onFailure(Call<FollowResponse> call, Throwable t) {

            }
        });
    }
}