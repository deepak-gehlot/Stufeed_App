package com.stufeed.android.view.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.androidquery.AQuery;
import com.appunite.appunitevideoplayer.PlayerActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.DeletePostResponse;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.LikeResponse;
import com.stufeed.android.api.response.RePostResponse;
import com.stufeed.android.api.response.SavePostResponse;
import com.stufeed.android.databinding.RowFeedBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.BoardSelectionActivity;
import com.stufeed.android.view.activity.CommentPostActivity;
import com.stufeed.android.view.activity.EditPostActivity;
import com.stufeed.android.view.activity.FullImageActivity;
import com.stufeed.android.view.activity.UserProfileActivity;
import com.stufeed.android.view.fragment.audioplayer.PlayerDialogFragment;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment context;
    private ArrayList<GetPostResponse.Post> postArrayList;
    private String loginUserId;
    private String videoURL = "";
    private AQuery aQuery;
    private static final int TYPE_AD = 1;
    private static final int TYPE_VIEW = 2;

    public FeedListAdapter(Fragment context, ArrayList<GetPostResponse.Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        loginUserId = Utility.getLoginUserDetail(context.getActivity()).getUserId();
        aQuery = new AQuery(context.getActivity());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_VIEW) {
            RowFeedBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context.getActivity()), R.layout
                    .row_feed, parent, false);
            return new ViewHolder(rowBinding);
        } else {
            View view = LayoutInflater.from(context.getActivity()).inflate(R.layout.row_ad, parent, false);
            return new AdViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (postArrayList.get(position) == null) {
            return TYPE_AD;
        } else {
            return TYPE_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder rowHolder, int position) {

        if (getItemViewType(position) == TYPE_AD) {
            AdViewHolder adViewHolder = (AdViewHolder) rowHolder;

        } else {
            final ViewHolder holder = (ViewHolder) rowHolder;
            final GetPostResponse.Post post = postArrayList.get(position);
            holder.rowBinding.setModel(post);
            holder.rowBinding.setAdapter(this);
            holder.rowBinding.documentLayout.setVisibility(View.GONE);
            holder.rowBinding.audioCardLayout.setVisibility(View.GONE);
            holder.rowBinding.pollLayout.setVisibility(View.GONE);
            holder.rowBinding.imageLayout.setVisibility(View.GONE);
            holder.rowBinding.audioVideoImgLayout.setVisibility(View.GONE);

            Utility.setUserTypeIconColor(context.getActivity(), post.getUserType(), holder.rowBinding.userTypeIcon);

            if (!TextUtils.isEmpty(post.getBoardId())) {
                holder.rowBinding.boardName.setVisibility(View.VISIBLE);
            } else {
                holder.rowBinding.boardName.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(post.getIsLike())) {
                if (post.getIsLike().equals("0")) {
                    holder.rowBinding.imgLike.setImageResource(R.drawable.favorite_border_icon);
                } else {
                    holder.rowBinding.imgLike.setImageResource(R.drawable.favorite_icon);
                }
            } else {
                holder.rowBinding.imgLike.setImageResource(R.drawable.favorite_border_icon);
            }

            if (!TextUtils.isEmpty(post.getIsBookmark())) {
                if (post.getIsBookmark().equals("0")) {
                    holder.rowBinding.imgSave.setImageResource(R.drawable.ic_bookmark_border);
                } else {
                    holder.rowBinding.imgSave.setImageResource(R.drawable.bookmark_icon);
                }
            } else {
                holder.rowBinding.imgSave.setImageResource(R.drawable.ic_bookmark_border);
            }

            switch (post.getPostType()) {
                case "1":

                    holder.rowBinding.documentLayout.setVisibility(View.VISIBLE);
                /*aQuery.id(holder.rowBinding.audioVideoImg).image(
                        post.getFilePath() + post.getFile(), true, true, 200, R.drawable.user_default);
                */
                    String ext = FilenameUtils.getExtension(post.getFile());
                    setFileAsExtension(holder, ext);
                    holder.rowBinding.doctextAVUrl.setText(post.getFile());
                    break;
                case "2": // aartical url
                    holder.rowBinding.audioVideoImgLayout.setVisibility(View.VISIBLE);
/*
                Glide.with(context)
                        .load(post.getArticleThumbUrl())
                        .into(holder.rowBinding.audioVideoImg);
*/

                    aQuery.id(holder.rowBinding.audioVideoImg).image(
                            post.getArticleThumbUrl(), true, true, 200, R.drawable.user_default);

                    holder.rowBinding.textAVTitle.setText(post.getArticleTitle());
                    holder.rowBinding.textAVUrl.setText(post.getVideoUrl());

                    break;
                case "5":  // for audio
                    holder.rowBinding.audioCardLayout.setVisibility(View.VISIBLE);
                    String fileName = URLUtil.guessFileName(post.getFilePath() + post.getImage(), null, null);
                    holder.rowBinding.audioText.setText(fileName);
                    break;
                case "4":  // for poll
                    holder.rowBinding.pollLayout.setVisibility(View.VISIBLE);
                    holder.rowBinding.txtPollQuestion.setText(post.getQuestion());

                    holder.rowBinding.option1.setText(post.getOptionArrayList().get(0).getOptionValue());
                    holder.rowBinding.option2.setText(post.getOptionArrayList().get(1).getOptionValue());
                    for (int i = 0; i < post.getOptionArrayList().size(); i++) {

                        if (!TextUtils.isEmpty(post.getSelectedId())) {
                            String id = post.getOptionArrayList().get(i).getId();
                            if (post.getSelectedId().equals(id)) {
                                post.getOptionArrayList().get(i).setIsSelect("1");
                            }
                        }

                        String select = post.getOptionArrayList().get(i).getIsSelect();
                        if (i == 0) {
                            if (!TextUtils.isEmpty(select) && select.equals("1")) {
                                holder.rowBinding.option1.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                        .ic_radio_button_checked, 0, 0, 0);
                                holder.rowBinding.option2.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                        .ic_radio_button_unchecked, 0, 0, 0);
                            } else {
                                holder.rowBinding.option1.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                        .ic_radio_button_unchecked, 0, 0, 0);
                            }

                            int firstValue = 0, secondValue = 0;
                            int total = 0;
                            if (!TextUtils.isEmpty(post.getOptionArrayList().get(0).getTotalVote())) {
                                firstValue = Integer.parseInt(post.getOptionArrayList().get(0).getTotalVote());
                            }

                            if (!TextUtils.isEmpty(post.getOptionArrayList().get(1).getTotalVote())) {
                                secondValue = Integer.parseInt(post.getOptionArrayList().get(1).getTotalVote());
                            }
                            total = firstValue + secondValue;
                            int firstPer = (firstValue / total) * 100;

                            holder.rowBinding.totalCount1.setText("" + firstPer);
                        } else if (i == 1) {
                            if (!TextUtils.isEmpty(select) && select.equals("1")) {
                                holder.rowBinding.option1.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                        .ic_radio_button_unchecked, 0, 0, 0);
                                holder.rowBinding.option2.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                        .ic_radio_button_checked, 0, 0, 0);
                            } else {
                                holder.rowBinding.option2.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                        .ic_radio_button_unchecked, 0, 0, 0);
                            }
                            int firstValue = 0, secondValue = 0;
                            int total = 0;
                            if (!TextUtils.isEmpty(post.getOptionArrayList().get(0).getTotalVote())) {
                                firstValue = Integer.parseInt(post.getOptionArrayList().get(0).getTotalVote());
                            }

                            if (!TextUtils.isEmpty(post.getOptionArrayList().get(1).getTotalVote())) {
                                secondValue = Integer.parseInt(post.getOptionArrayList().get(1).getTotalVote());
                            }
                            total = firstValue + secondValue;
                            int firstPer = (secondValue / total) * 100;

                            holder.rowBinding.totalCount2.setText("" + firstPer);
                        }
                    }

                    break;
                default:
                    holder.rowBinding.imageLayout.setVisibility(View.VISIBLE);
                    imageOrVideoRow(holder, post);
                    break;
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
    }

    private void setFileAsExtension(ViewHolder holder, String extension) {
        switch (extension) {
            case "pdf":
                holder.rowBinding.documentImg.setImageResource(R.drawable.icon_file_pdf);
                break;
            case "doc":
            case "docx":
                holder.rowBinding.documentImg.setImageResource(R.drawable.icon_file_doc);
                break;
            case "xlsx":
            case "xls":
                holder.rowBinding.documentImg.setImageResource(R.drawable.icon_file_xls);
                break;
            case "txt":
                holder.rowBinding.documentImg.setImageResource(R.drawable.icon_file_unknown);
                break;
            case "ppt":
                holder.rowBinding.documentImg.setImageResource(R.drawable.icon_file_ppt);
                break;
            default:
                holder.rowBinding.documentImg.setImageResource(R.drawable.icon_file_unknown);
        }
    }

    private void imageOrVideoRow(final ViewHolder holder, GetPostResponse.Post post) {
        ArrayList<String> arrayList = Utility.extractUrls(post.getDescription());
        if (arrayList.size() != 0) {
            videoURL = arrayList.get(0);
            if (Utility.isValidUrl(videoURL)) {
                holder.rowBinding.playBtn.setVisibility(View.VISIBLE);
                try {
                    if (videoURL.contains("www.youtube.com")) {
                        String url = "https://img.youtube.com/vi/" + videoURL.split("\\=")[1] + "/0.jpg";
                        /*Glide.with(context)
                                .load(url)
                                .into(holder.rowBinding.image);*/

                        aQuery.id(holder.rowBinding.image).image(
                                url, true, true, 200, R.drawable.user_default);
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
            /*Glide.with(context)
                    .load(post.getFilePath() + post.getImage())
                    .into(holder.rowBinding.image);*/

            aQuery.id(holder.rowBinding.image).image(
                    post.getFilePath() + post.getImage(), true, true, 200, R.drawable.user_default);
        }
    }

    //http://techslides.com/demos/sample-videos/small.mp4
    @Override
    public int getItemCount() {
        return postArrayList != null ? postArrayList.size() : 0;
    }

    /**
     * On click user name
     */
    public void onClickName(GetPostResponse.Post post) {
        GetCollegeUserResponse.User user = new GetCollegeUserResponse.User();
        user.setUserId(post.getUserId());
        user.setFullName(post.getFullName());
        user.setIsFollow("0");
        Intent intent = new Intent(context.getActivity(), UserProfileActivity.class);
        intent.putExtra(UserProfileActivity.USER, user);
        context.startActivity(intent);
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
                    context.startActivity(PlayerActivity.getVideoPlayerIntent(context.getActivity(),
                            videoURL,
                            "Video title"));
                }
            }
        } else {
            String imageUrl = post.getFilePath() + post.getImage();
            if (!TextUtils.isEmpty(imageUrl)) {
                Intent intent = new Intent(context.getActivity(), FullImageActivity.class);
                intent.putExtra("image", imageUrl);
                context.startActivity(intent);
            }
        }
    }

    public void onArticleClick(GetPostResponse.Post post) {
        if (post.getPostType().equals("1")) {
            String url = post.getFilePath() + post.getFile();
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context.getActivity(), R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context.getActivity(), Uri.parse(url));
        } else {
            String url = post.getVideoUrl();
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context.getActivity(), R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context.getActivity(), Uri.parse(url));
        }
    }

    private void showActionMenu(final GetPostResponse.Post post, int position, View view) {
        PopupMenu popup = new PopupMenu(context.getActivity(), view);

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
                        Intent intent = new Intent(context.getActivity(), EditPostActivity.class);
                        intent.putExtra("item", post);
                        context.startActivityForResult(intent, 192);
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
        if (post.getIsLike().equals("1")) {
            if (!TextUtils.isEmpty(like)) {
                int likeCount = Integer.parseInt(like) - 1;
                post.setTotalLike("" + likeCount);
            }
            post.setIsLike("0");
        } else {
            if (!TextUtils.isEmpty(like)) {
                int likeCount = Integer.parseInt(like) + 1;
                post.setTotalLike("" + likeCount);
            }
            post.setIsLike("1");
        }

        int position = postArrayList.indexOf(post);
        postArrayList.set(position, post);
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
        Intent intent = new Intent(context.getActivity(), CommentPostActivity.class);
        intent.putExtra(CommentPostActivity.TAG_POST, post);
        intent.putExtra(CommentPostActivity.TAG_POSITION, postArrayList.indexOf(post));
        context.startActivityForResult(intent, 114);
    }

    public void refreshItem(int position, GetPostResponse.Post post) {
        if (position != -1) {
            postArrayList.set(position, post);
            notifyItemChanged(position);
        }
    }


    /**
     * show re post confirmation dialog
     *
     * @param post {@link GetPostResponse.Post}
     */
    public void onRePostBtnClick(final GetPostResponse.Post post) {
        Utility.setDialog(context.getActivity(), "Message", "Do you want to repost this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        Intent intent = new Intent(context.getActivity(), BoardSelectionActivity.class);
                        intent.putExtra("post_id", post.getPostId());
                        context.startActivityForResult(intent, 231);
                        //rePost(post);
                    }
                });
    }

    /**
     * Call service for Re post
     */
    public void rePost(String postId, String boardId) {
        Api api = APIClient.getClient().create(Api.class);
        Call<RePostResponse> responseCall = api.rePost(loginUserId, postId);
        ProgressDialog.getInstance().showProgressDialog(context.getActivity());
        responseCall.enqueue(new Callback<RePostResponse>() {
            @Override
            public void onResponse(Call<RePostResponse> call, Response<RePostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showToast(context.getActivity(), "Post successfully.");
            }

            @Override
            public void onFailure(Call<RePostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(context.getActivity());
            }
        });
    }


    /**
     * show save post confirmation dialog
     *
     * @param post {@link GetPostResponse.Post}
     */
    public void onSavePostBtnClick(final GetPostResponse.Post post) {
        if (!TextUtils.isEmpty(post.getIsBookmark())) {
            if (post.getIsBookmark().equals("1")) {
                onRemovePostBtnClick(post);
                return;
            }
        }
        Utility.setDialog(context.getActivity(), "Message", "Do you want to bookmark this post.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        savePost(post);
                    }
                });
    }


    /**
     * show remove saved post confirmation dialog
     *
     * @param post {@link GetPostResponse.Post}
     */
    private void onRemovePostBtnClick(final GetPostResponse.Post post) {
        if (!TextUtils.isEmpty(post.getIsBookmark())) {
            if (post.getIsBookmark().equals("0")) {
                return;
            }
        }
        Utility.setDialog(context.getActivity(), "Message", "Do you want to unbookmark this post.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        removePost(post);
                    }
                });
    }

    /**
     * Call service for save post
     *
     * @param post
     */
    private void savePost(final GetPostResponse.Post post) {
        Api api = APIClient.getClient().create(Api.class);
        Call<SavePostResponse> responseCall = api.savePost(loginUserId, post.getPostId());
        ProgressDialog.getInstance().showProgressDialog(context.getActivity());
        responseCall.enqueue(new Callback<SavePostResponse>() {
            @Override
            public void onResponse(Call<SavePostResponse> call, Response<SavePostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                post.setIsBookmark("1");
                notifyItemChanged(postArrayList.indexOf(post));
                Utility.showToast(context.getActivity(), "Bookmark Post successfully.");
            }

            @Override
            public void onFailure(Call<SavePostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(context.getActivity());
            }
        });
    }

    /**
     * Call service for save post
     *
     * @param post
     */
    private void removePost(final GetPostResponse.Post post) {
        Api api = APIClient.getClient().create(Api.class);
        Call<SavePostResponse> responseCall = api.removePost(loginUserId, post.getPostId());
        ProgressDialog.getInstance().showProgressDialog(context.getActivity());
        responseCall.enqueue(new Callback<SavePostResponse>() {
            @Override
            public void onResponse(Call<SavePostResponse> call, Response<SavePostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                post.setIsBookmark("0");
                notifyItemChanged(postArrayList.indexOf(post));
                Utility.showToast(context.getActivity(), "Bookmark Post successfully.");
            }

            @Override
            public void onFailure(Call<SavePostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(context.getActivity());
            }
        });
    }

    private void showDeleteConfirmatinDialog(final GetPostResponse.Post post) {
        Utility.setDialog(context.getActivity(), context.getString(R.string.alert), "Are you sure, You want to delete" +
                        " this post?",
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
        Utility.setDialog(context.getActivity(), context.getString(R.string.alert), "Are you sure, You want to report" +
                        " this post?",
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

    public void onAudioPlayClick(GetPostResponse.Post post) {
        // Show DialogFragment
        PlayerDialogFragment.newInstance(
                post.getFilePath() + post.getAudioFile()
        ).show(context.getActivity().getFragmentManager(), "Dialog Fragment");
    }

    /**
     * On poll option text view click listener
     *
     * @param post
     * @param option
     */
    public void onPollOptionSelect(GetPostResponse.Post post, int option) {
        String totalVote1 = post.getOptionArrayList().get(0).getTotalVote();
        String totalVote2 = post.getOptionArrayList().get(1).getTotalVote();
        switch (option) {
            case 1:
                post.getOptionArrayList().get(0).setIsSelect("1");
                post.getOptionArrayList().get(1).setIsSelect("0");
                post.setSelectedId(post.getOptionArrayList().get(0).getId());

                if (!TextUtils.isEmpty(totalVote1)) {
                    totalVote1 = "" + (Integer.parseInt(totalVote1) + 1);
                } else {
                    totalVote1 = "0";
                }

                if (!TextUtils.isEmpty(totalVote2)) {
                    totalVote2 = "" + (Integer.parseInt(totalVote2) - 1);
                }

                addPollAnswer(post.getQuestionId(), post.getOptionArrayList().get(0).getId());
                break;
            case 2:
                post.getOptionArrayList().get(1).setIsSelect("1");
                post.getOptionArrayList().get(0).setIsSelect("0");
                post.setSelectedId(post.getOptionArrayList().get(1).getId());
                if (!TextUtils.isEmpty(totalVote2)) {
                    totalVote2 = "" + (Integer.parseInt(totalVote2) + 1);
                } else {
                    totalVote2 = "0";
                }
                if (!TextUtils.isEmpty(totalVote1)) {
                    totalVote1 = "" + (Integer.parseInt(totalVote1) - 1);
                }
                addPollAnswer(post.getQuestionId(), post.getOptionArrayList().get(1).getId());

                break;
        }
        post.getOptionArrayList().get(0).setTotalVote(totalVote1);
        post.getOptionArrayList().get(1).setTotalVote(totalVote2);
        notifyItemChanged(postArrayList.indexOf(post));
    }

    /**
     * update poll answer on server
     *
     * @param questionId
     * @param optionId
     */
    private void addPollAnswer(String questionId, String optionId) {
        Api api = APIClient.getClient().create(Api.class);
        Call<FollowResponse> responseCall = api.addPollAnswer(loginUserId, questionId, optionId);
        responseCall.enqueue(new Callback<FollowResponse>() {
            @Override
            public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {

            }

            @Override
            public void onFailure(Call<FollowResponse> call, Throwable t) {

            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RowFeedBinding rowBinding;

        private ViewHolder(RowFeedBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {
        public AdViewHolder(View view) {
            super(view);
            AdView mAdView = view.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }
}
