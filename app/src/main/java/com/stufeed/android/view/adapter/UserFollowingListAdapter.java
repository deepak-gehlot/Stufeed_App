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
import com.stufeed.android.api.response.GetFollowingListResponse;
import com.stufeed.android.databinding.RowFollowingBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.UserProfileActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 4/4/2018.
 */
public class UserFollowingListAdapter extends RecyclerView.Adapter<UserFollowingListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetFollowingListResponse.User> userArrayList;
    private String userId = "";

    public UserFollowingListAdapter(Context context, ArrayList<GetFollowingListResponse.User> userArrayList, String
            userId) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFollowingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.row_following, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetFollowingListResponse.User user = userArrayList.get(position);
        holder.binding.setUser(user);
        holder.binding.setAdapter(this);

        Utility.setUserTypeIconColor(context, user.getUserType(), holder.binding.userTypeView);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowFollowingBinding binding;

        public ViewHolder(RowFollowingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetFollowingListResponse.User user = userArrayList.get(getAdapterPosition());
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

    public void onClickUnFollow(GetFollowingListResponse.User user) {
        unFollowRequest(user.getFollowUserid());
        int position = userArrayList.indexOf(user);
        userArrayList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Request un follow user
     */
    private void unFollowRequest(String followUserId) {
        Api api = APIClient.getClient().create(Api.class);
        Call<FollowResponse> responseCall = api.follow(followUserId, userId);
        responseCall.enqueue(new Callback<FollowResponse>() {
            @Override
            public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {

            }

            @Override
            public void onFailure(Call<FollowResponse> call, Throwable t) {

            }
        });
    }
}