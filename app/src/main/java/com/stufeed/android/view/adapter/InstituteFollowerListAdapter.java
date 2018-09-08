package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetAllInstituteFollowersListResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.databinding.RowInstituteFollowerBinding;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.UserProfileActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstituteFollowerListAdapter extends RecyclerView.Adapter<InstituteFollowerListAdapter.MyHolder> {
    private Context context;
    private ArrayList<GetAllInstituteFollowersListResponse.InstituteFollower> followerArrayList;
    private String loginUserId = "";

    public InstituteFollowerListAdapter(Context context, ArrayList<GetAllInstituteFollowersListResponse.InstituteFollower> followerArrayList) {
        this.context = context;
        this.followerArrayList = followerArrayList;
        loginUserId = Utility.getLoginUserId(context);

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowInstituteFollowerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.row_institute_follower, parent, false);
        return new MyHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        GetAllInstituteFollowersListResponse.InstituteFollower follower = followerArrayList.get(position);
        holder.binding.setAdapter(this);
        holder.binding.setInstituteFollower(follower);
        Utility.setUserTypeIconColor(context,
                follower.getUserType(),
                holder.binding.userTypeView);

        if (follower.getUserId().equals(loginUserId)) {
            holder.binding.txtFollow.setText("You");
        } else if (follower.getIsFollow().equals("1")) {
            holder.binding.txtFollow.setText(context.getString(R.string.un_follow));
        } else {
            holder.binding.txtFollow.setText(context.getString(R.string.follow));
        }
    }

    @Override
    public int getItemCount() {
        return followerArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private RowInstituteFollowerBinding binding;

        public MyHolder(RowInstituteFollowerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetAllInstituteFollowersListResponse.InstituteFollower follower = followerArrayList.get(getAdapterPosition());
                    GetCollegeUserResponse.User user = new GetCollegeUserResponse.User();
                    user.setUserId(follower.getUserId());
                    user.setFullName(follower.getFullName());
                    user.setCollegeId("");
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(UserProfileActivity.USER, user);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void onClickUnFollow(GetAllInstituteFollowersListResponse.InstituteFollower follower) {
        unFollowRequest(follower.getUserId());
        int position = followerArrayList.indexOf(follower);
        followerArrayList.remove(position);
        notifyItemRemoved(position);
    }

    private void unFollowRequest(String userId) {
        Api api = APIClient.getClient().create(Api.class);
        Call<FollowResponse> responseCall = api.follow(loginUserId, userId);
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
