package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.DeletePostResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.LikeResponse;
import com.stufeed.android.databinding.RowFeedBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.TimeUtil;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.CommentPostActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetPostResponse.Post> postArrayList;
    private String loginUserId;

    public FeedListAdapter(Context context, ArrayList<GetPostResponse.Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        loginUserId = Utility.getLoginUserDetail(context).getUserId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFeedBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_feed, parent,
                false);
        return new ViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.rowBinding.setModel(postArrayList.get(position));
        holder.rowBinding.actionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionMenu(postArrayList.get(holder.getAdapterPosition()), holder.getAdapterPosition(), holder
                        .rowBinding.actionIcon);
            }
        });

        holder.rowBinding.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentPostActivity.class);
                intent.putExtra(CommentPostActivity.TAG_POST, postArrayList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

        String postDateTime = postArrayList.get(position).getDateTime();
        String timeAgo = TimeUtil.getTimeAgo(context, postDateTime, "yyy-mm-dd HH:mm:ss");
        holder.rowBinding.time.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return postArrayList != null ? postArrayList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RowFeedBinding rowBinding;

        private ViewHolder(RowFeedBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
        }
    }

    private void showActionMenu(final GetPostResponse.Post post, int position, View view) {
        PopupMenu popup = new PopupMenu(context, view);

        if (post.getUserId().equals(loginUserId)) {
            popup.inflate(R.menu.post_user_row_menu);
        } else {
            popup.inflate(R.menu.post_row_menu);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuEdit:
                        break;
                    case R.id.menuDelete:
                        showDeleteConfirmatinDialog(post);
                        break;
                    case R.id.menuReport:
                        showReportConfirmatinDialog(post);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    public void onLikeClick(GetPostResponse.Post post) {
        Api api = APIClient.getClient().create(Api.class);
        Call<LikeResponse> responseCall = api.likePost(loginUserId, post.getPostId());
        responseCall.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {

            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {

            }
        });
    }

    private void showDeleteConfirmatinDialog(final GetPostResponse.Post post) {
        Utility.setDialog(context, context.getString(R.string.alert), "Are you sure, You want to delete this post?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        int position = postArrayList.indexOf(post);
                        postArrayList.remove(position);
                        notifyItemChanged(position);
                        deletePost(post);
                    }
                });
    }

    private void deletePost(GetPostResponse.Post post) {
        Api api = APIClient.getClient().create(Api.class);
        Call<DeletePostResponse> responseCall = api.deletePost(loginUserId, post.getPostId());
        responseCall.enqueue(new Callback<DeletePostResponse>() {
            @Override
            public void onResponse(Call<DeletePostResponse> call, Response<DeletePostResponse> response) {

            }

            @Override
            public void onFailure(Call<DeletePostResponse> call, Throwable t) {

            }
        });
    }


    private void showReportConfirmatinDialog(final GetPostResponse.Post post) {
        Utility.setDialog(context, context.getString(R.string.alert), "Are you sure, You want to report this post?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    private void reportPost(GetPostResponse.Post post) {
        Api api = APIClient.getClient().create(Api.class);

    }
}
