package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetFollowerListResponse;
import com.stufeed.android.databinding.RowFollowerBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 4/4/2018.
 */
public class UserFollowerListAdapter extends RecyclerView.Adapter<UserFollowerListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetFollowerListResponse.User> userArrayList;
    private String userId = "";

    public UserFollowerListAdapter(Context context, ArrayList<GetFollowerListResponse.User> userArrayList, String
            userId) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFollowerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.row_follower, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetFollowerListResponse.User user = userArrayList.get(position);
        holder.binding.setUser(user);
        holder.binding.setAdapter(this);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowFollowerBinding binding;

        public ViewHolder(RowFollowerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void onClickUnFollow(GetFollowerListResponse.User user) {
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
        Call<FollowResponse> responseCall = api.follow(userId, followUserId);
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