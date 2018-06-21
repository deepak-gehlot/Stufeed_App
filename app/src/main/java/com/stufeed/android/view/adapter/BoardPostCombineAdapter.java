package com.stufeed.android.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cunoraz.tagview.Tag;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.GetUserDetailsResponse;
import com.stufeed.android.databinding.RowBoardPostCombineBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.FolloweListActivity;
import com.stufeed.android.view.activity.UserJoinBoardActivity;
import com.stufeed.android.view.activity.UserProfileActivity;
import com.stufeed.android.view.activity.UsersPostActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardPostCombineAdapter extends RecyclerView.Adapter<BoardPostCombineAdapter.ViewHolder> {

    private final int TYPE_PROFILE = 11;
    private final int TYPE_BOARD = 12;
    private final int TYPE_POST = 13;

    private Activity context;
    private GetCollegeUserResponse.User user;
    private GetBoardListResponse.Count count;
    private GetUserDetailsResponse.Details userDetailsResponse;
    private ArrayList<GetBoardListResponse.Board> boardArrayList;
    private ArrayList<GetPostResponse.Post> postArrayList;
    private String mLoginUserId = "";
    private List<Tag> tagList = new ArrayList<>();

    public BoardPostCombineAdapter(Activity context, GetCollegeUserResponse.User user,
                                   GetBoardListResponse.Count count,
                                   GetUserDetailsResponse.Details userDetailsResponse,
                                   ArrayList<GetBoardListResponse.Board> boardArrayList,
                                   ArrayList<GetPostResponse.Post> postArrayList) {
        this.context = context;
        mLoginUserId = Utility.getLoginUserId(context);
        this.user = user;
        this.count = count;
        this.userDetailsResponse = userDetailsResponse;
        this.boardArrayList = boardArrayList;
        this.postArrayList = postArrayList;
        String allSkills = userDetailsResponse.getSkills();
        String skills[] = allSkills.split(",");
        for (int i = 0; i < skills.length; i++) {
            if (!TextUtils.isEmpty(skills[i])) {
                Tag tag = new Tag(skills[i]);
                tagList.add(tag);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBoardPostCombineBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_board_post_combine, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_PROFILE:
                holder.binding.recyclerView.setVisibility(View.GONE);
                holder.binding.topPanel.setVisibility(View.VISIBLE);
                holder.binding.profilePic.setVisibility(View.VISIBLE);
                holder.binding.setModel(userDetailsResponse);
                holder.binding.setCountModel(count);
                holder.binding.setUser(user);
                holder.binding.setActivity(this);
                setUserType(holder, userDetailsResponse.getUserType());
                String description = userDetailsResponse.getAbout();
                holder.binding.textAboutMeData.setText(description);
                holder.binding.txtUserName.setText(userDetailsResponse.getFullName());
                String allSkills = userDetailsResponse.getSkills();
                String skills[] = allSkills.split(",");
                ArrayList<Tag> tagList = new ArrayList<>();
                for (int i = 0; i < skills.length; i++) {
                    if (!TextUtils.isEmpty(skills[i])) {
                        Tag tag = new Tag(skills[i]);
                        tagList.add(tag);
                    }
                }
                if (tagList.size() == 0) {
                    //..isHaveSkills = false;
                    holder.binding.textSkill.setVisibility(View.GONE);
                } else {
                    //isHaveSkills = true;
                    holder.binding.textSkill.setVisibility(View.VISIBLE);
                }

                if (userDetailsResponse.getIsFollow().equals("1")) {
                    holder.binding.btnFollowStatus.setText("Un Follow");
                } else {
                    holder.binding.btnFollowStatus.setText("Follow");
                }
                setAchievementRecyclerView(holder, userDetailsResponse.getAchievementArrayList());
                break;
            case TYPE_BOARD:
                holder.binding.recyclerView.setVisibility(View.VISIBLE);
                holder.binding.topPanel.setVisibility(View.GONE);
                holder.binding.profilePic.setVisibility(View.GONE);
                holder.binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                UserBoardListAdapter adapter = new UserBoardListAdapter(context, boardArrayList);
                holder.binding.recyclerView.setAdapter(adapter);
                break;
            case TYPE_POST:
                holder.binding.recyclerView.setVisibility(View.VISIBLE);
                holder.binding.topPanel.setVisibility(View.GONE);
                holder.binding.profilePic.setVisibility(View.GONE);
                holder.binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                UserFeedListAdapter adapterPost = new UserFeedListAdapter(context, postArrayList);
                holder.binding.recyclerView.setAdapter(adapterPost);
                break;
        }
        holder.binding.btnFollowStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUnFollowClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_PROFILE;
            case 1:
                return TYPE_BOARD;
            case 2:
                return TYPE_POST;
            default:
                return TYPE_PROFILE;

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RowBoardPostCombineBinding binding;

        public ViewHolder(final RowBoardPostCombineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.imageDownIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.detailsLayout.getVisibility() == View.GONE) {
                        binding.detailsLayout.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (tagList != null) {
                                    binding.tagGroup.addTags(tagList);
                                }
                            }
                        }, 10);
                    } else {
                        binding.detailsLayout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public void onPostCountClick() {
        Intent intent = new Intent(context, UsersPostActivity.class);
        intent.putExtra("user_id", user.getUserId());
        context.startActivity(intent);
    }

    public void onBoardJoinCountClick() {
        Intent intent = new Intent(context, UserJoinBoardActivity.class);
        intent.putExtra("user_id", user.getUserId());
        intent.putExtra("login_user_id", mLoginUserId);
        context.startActivity(intent);
    }

    public void onFollowerCountClick() {
        Intent intent = new Intent(context, FolloweListActivity.class);
        intent.putExtra("user_id", user.getUserId());
        context.startActivity(intent);
    }

    public void setUserType(ViewHolder viewHolder, String type) {
        Utility.setUserTypeTagColor(context, type, viewHolder.binding.txtType);
        switch (type) {
            case "1":
                viewHolder.binding.txtType.setText("Student");
                break;
            case "2":
                viewHolder.binding.txtType.setText("Department");
                break;
            case "3":
                viewHolder.binding.txtType.setText("Faculty");
                break;
            case "4":
                viewHolder.binding.txtType.setText("Institute");
                break;
        }
    }

    public void followUnFollowClick() {
        if (userDetailsResponse.getIsFollow().equals("1")) {
            setUnFollowConfirmation(userDetailsResponse);
        } else {
            setFollowConfirmation(userDetailsResponse);
        }
    }

    private void follow() {
        if (userDetailsResponse.getIsFollow().equals("1")) {
            userDetailsResponse.setIsFollow("0");
        } else {
            userDetailsResponse.setIsFollow("1");
        }
        notifyItemChanged(0);
        Api api = APIClient.getClient().create(Api.class);
        Call<FollowResponse> responseCall = api.follow(mLoginUserId, user.getUserId());
        responseCall.enqueue(new Callback<FollowResponse>() {
            @Override
            public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {

            }

            @Override
            public void onFailure(Call<FollowResponse> call, Throwable t) {

            }
        });
    }

    private void setUnFollowConfirmation(final GetUserDetailsResponse.Details user) {
        Utility.setDialog(context, "Message", "Do you want to un-follow this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        follow();
                    }
                });
    }

    private void setFollowConfirmation(final GetUserDetailsResponse.Details user) {
        Utility.setDialog(context, "Message", "Do you want to follow this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        follow();
                    }
                });
    }

    /**
     * Get Achievement response
     */
    private void setAchievementRecyclerView(ViewHolder holder, ArrayList<GetAchievementListResponse.Achievement> achievementArrayList) {
        if (achievementArrayList != null && achievementArrayList.size() != 0) {
            AchivementFragmentListAdapter adapter = new AchivementFragmentListAdapter(context,
                    achievementArrayList);
            holder.binding.recyclerViewAchievement.setLayoutManager(new LinearLayoutManager(context));
            holder.binding.recyclerViewAchievement.setAdapter(adapter);
        } else {
            holder.binding.textAchievement.setVisibility(View.GONE);
            holder.binding.recyclerViewAchievement.setVisibility(View.GONE);
        }
        // showHideArrow();
    }
}