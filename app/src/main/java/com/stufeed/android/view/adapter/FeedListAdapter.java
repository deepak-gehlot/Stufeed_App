package com.stufeed.android.view.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appunite.appunitevideoplayer.PlayerActivity;
import com.bumptech.glide.Glide;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.DeletePostResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.LikeResponse;
import com.stufeed.android.api.response.RePostResponse;
import com.stufeed.android.databinding.RowAudioPlayerBinding;
import com.stufeed.android.databinding.RowFeedBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.CommentPostActivity;
import com.stufeed.android.view.activity.PostActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<GetPostResponse.Post> postArrayList;
    private String loginUserId;
    private String videoURL = "";

    public FeedListAdapter(Activity context, ArrayList<GetPostResponse.Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        loginUserId = Utility.getLoginUserDetail(context).getUserId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 5:
                RowAudioPlayerBinding rowAudioPlayerBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R
                                .layout.row_audio_player,
                        parent,
                        false);
                return new ViewHolder(rowAudioPlayerBinding);
            default:
                RowFeedBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_feed,
                        parent,
                        false);
                return new ViewHolder(rowBinding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        switch (postArrayList.get(position).getPostType()) {
            case "5":
                viewType = 5;
                break;
        }

        return viewType;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GetPostResponse.Post post = postArrayList.get(position);
        switch (post.getPostType()) {
            case "5":
                audioRow(holder, post);
                break;
            default:
                imageOrVideoRow(holder, post);
                break;
        }
    }

    private void audioRow(final ViewHolder holder, GetPostResponse.Post post) {
        holder.rowAudioPlayerBinding.setAdapter(this);

    }

    private void imageOrVideoRow(final ViewHolder holder, GetPostResponse.Post post) {
        holder.rowBinding.setModel(post);
        holder.rowBinding.setAdapter(this);
        ArrayList<String> arrayList = Utility.extractUrls(post.getDescription());
        if (arrayList.size() != 0) {
            videoURL = arrayList.get(0);
            if (Utility.isValidUrl(videoURL)) {
                holder.rowBinding.playBtn.setVisibility(View.VISIBLE);
                try {
                    if (videoURL.contains("www.youtube.com")) {
                        String url = "https://img.youtube.com/vi/" + videoURL.split("\\=")[1] + "/0.jpg";
                        Glide.with(context)
                                .load(url)
                                .into(holder.rowBinding.image);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final Bitmap bitmap = Utility.retriveVideoFrameFromVideo(videoURL);
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Glide.with(context)
                                                    .load(bitmap)
                                                    .into(holder.rowBinding.image);
                                        }
                                    });
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    holder.rowBinding.playBtn.setVisibility(View.GONE);
                }
            } else {
                holder.rowBinding.playBtn.setVisibility(View.GONE);
            }
        } else {
            holder.rowBinding.playBtn.setVisibility(View.GONE);
            Glide.with(context)
                    .load(post.getFilePath() + post.getImage())
                    .into(holder.rowBinding.image);
        }

        if (!TextUtils.isEmpty(post.getAllowRePost())) {
            if (post.getAllowRePost().equals("1")) {
                holder.rowBinding.imgRepost.setVisibility(View.VISIBLE);
            } else {
                holder.rowBinding.imgRepost.setVisibility(View.GONE);
            }
        } else {
            holder.rowBinding.imgRepost.setVisibility(View.VISIBLE);
        }

        holder.rowBinding.actionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionMenu(postArrayList.get(holder.getAdapterPosition()), holder.getAdapterPosition(), holder
                        .rowBinding.actionIcon);
            }
        });
    }

    //http://techslides.com/demos/sample-videos/small.mp4
    @Override
    public int getItemCount() {
        return postArrayList != null ? postArrayList.size() : 0;
    }

    public void onImageClick(GetPostResponse.Post post) {
        ArrayList<String> arrayList = Utility.extractUrls(post.getDescription());
        if (arrayList.size() != 0) {
            String videoURL = arrayList.get(0);
            if (Utility.isValidUrl(videoURL)) {
                if (videoURL.contains("www.youtube.com")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
                    context.startActivity(browserIntent);
                } else {
                    context.startActivity(PlayerActivity.getVideoPlayerIntent(context,
                            videoURL,
                            "Video title"));
                }
            }
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
        String like = post.getTotalLike();
        if (!TextUtils.isEmpty(like)) {
            int likeCount = Integer.parseInt(like) + 1;
            post.setTotalLike("" + likeCount);
        }
        int position = postArrayList.indexOf(post);
        postArrayList.add(position, post);
        notifyItemChanged(position);
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

    public void onCommentBtnClick(GetPostResponse.Post post) {
        Intent intent = new Intent(context, CommentPostActivity.class);
        intent.putExtra(CommentPostActivity.TAG_POST, post);
        context.startActivity(intent);
    }

    /**
     * show re post confirmation dialog
     *
     * @param post {@link GetPostResponse.Post}
     */
    public void onRePostBtnClick(final GetPostResponse.Post post) {
        Utility.setDialog(context, "Message", "Do you want to repost this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        rePost(post);
                    }
                });
    }

    /**
     * Call service for Re post
     *
     * @param post
     */
    private void rePost(GetPostResponse.Post post) {
        Api api = APIClient.getClient().create(Api.class);
        Call<RePostResponse> responseCall = api.rePost(loginUserId, post.getPostId());
        ProgressDialog.getInstance().showProgressDialog(context);
        responseCall.enqueue(new Callback<RePostResponse>() {
            @Override
            public void onResponse(Call<RePostResponse> call, Response<RePostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showToast(context, "Post successfully.");
            }

            @Override
            public void onFailure(Call<RePostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(context);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private RowFeedBinding rowBinding;
        private RowAudioPlayerBinding rowAudioPlayerBinding;

        private ViewHolder(RowFeedBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
        }

        private ViewHolder(RowAudioPlayerBinding rowAudioPlayerBinding) {
            super(rowAudioPlayerBinding.getRoot());
            this.rowAudioPlayerBinding = rowAudioPlayerBinding;
        }
    }
}
