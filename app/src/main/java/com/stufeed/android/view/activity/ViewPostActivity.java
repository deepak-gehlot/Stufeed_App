package com.stufeed.android.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;

import com.appunite.appunitevideoplayer.PlayerActivity;
import com.bumptech.glide.Glide;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.DeletePostResponse;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.SinglePost;
import com.stufeed.android.api.response.LikeResponse;
import com.stufeed.android.api.response.RePostResponse;
import com.stufeed.android.api.response.SavePostResponse;
import com.stufeed.android.databinding.ActivitySinglePostBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.CommentListAdapter;
import com.stufeed.android.view.adapter.FeedListAdapter;
import com.stufeed.android.view.fragment.audioplayer.PlayerDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPostActivity extends AppCompatActivity {

    private String loginUserId = "";
    private String postId = "";
    private String _for = "";
    private ActivitySinglePostBinding mBinding;
    private String videoURL = "";
    private ArrayList<GetAllCommentResponse.Comment> commentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_post);
        loginUserId = Utility.getLoginUserId(this);
        mBinding.setActivity(this);
        getDataFromBundle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPostById(postId);
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
        } else {
            postId = bundle.getString("post_id");
            _for = bundle.getString("for");
        }
    }

    private void getPostById(String postId) {
        ProgressDialog.getInstance().showProgressDialog(ViewPostActivity.this);
        Api api = APIClient.getClient().create(Api.class);
        Call<SinglePost> call = api.getPostById(postId);
        call.enqueue(new Callback<SinglePost>() {
            @Override
            public void onResponse(Call<SinglePost> call, Response<SinglePost> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<SinglePost> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(SinglePost singlePost) {
        ProgressDialog.getInstance().dismissDialog();
        if (singlePost == null) {
            Utility.showErrorMsg(ViewPostActivity.this);
        } else if (singlePost.getResponseCode().equals(Api.SUCCESS)) {
            setView(singlePost.getPost());
            if (_for.equals("comment")) {
                getAllCommentList();
            }
        } else {
            Utility.showErrorMsg(ViewPostActivity.this);
        }
    }

    private void setView(final SinglePost.Post post) {
        mBinding.setModel(post);
        mBinding.setActivity(this);
        mBinding.audioCardLayout.setVisibility(View.GONE);
        mBinding.pollLayout.setVisibility(View.GONE);
        mBinding.imageLayout.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(post.getBoardId())) {
            mBinding.boardName.setVisibility(View.VISIBLE);
        } else {
            mBinding.boardName.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(post.getIsLike())) {
            if (post.getIsLike().equals("0")) {
                mBinding.imgLike.setImageResource(R.drawable.favorite_border_icon);
            } else {
                mBinding.imgLike.setImageResource(R.drawable.favorite_icon);
            }
        } else {
            mBinding.imgLike.setImageResource(R.drawable.favorite_border_icon);
        }

        if (!TextUtils.isEmpty(post.getIsBookmark())) {
            if (post.getIsBookmark().equals("0")) {
                mBinding.imgSave.setImageResource(R.drawable.ic_bookmark_border);
            } else {
                mBinding.imgSave.setImageResource(R.drawable.bookmark_icon);
            }
        } else {
            mBinding.imgSave.setImageResource(R.drawable.ic_bookmark_border);
        }

        switch (post.getPostType()) {
            case "5":  // for audio
                mBinding.audioCardLayout.setVisibility(View.VISIBLE);
                String fileName = URLUtil.guessFileName(post.getFilePath() + post.getImage(), null, null);
                mBinding.audioText.setText(fileName);
                break;
            case "4":  // for poll
                mBinding.pollLayout.setVisibility(View.VISIBLE);
                mBinding.txtPollQuestion.setText(post.getQuestion());

                mBinding.option1.setText(post.getOptionArrayList().get(0).getOptionValue());
                mBinding.option2.setText(post.getOptionArrayList().get(1).getOptionValue());
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
                            mBinding.option1.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                    .ic_radio_button_checked, 0, 0, 0);
                            mBinding.option2.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                    .ic_radio_button_unchecked, 0, 0, 0);
                        } else {
                            mBinding.option1.setCompoundDrawablesWithIntrinsicBounds(R.drawable
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

                        mBinding.totalCount1.setText("" + firstPer);
                    } else if (i == 1) {
                        if (!TextUtils.isEmpty(select) && select.equals("1")) {
                            mBinding.option1.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                    .ic_radio_button_unchecked, 0, 0, 0);
                            mBinding.option2.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                                    .ic_radio_button_checked, 0, 0, 0);
                        } else {
                            mBinding.option2.setCompoundDrawablesWithIntrinsicBounds(R.drawable
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

                        mBinding.totalCount2.setText("" + firstPer);
                    }
                }

                break;
            default:
                mBinding.imageLayout.setVisibility(View.VISIBLE);
                imageOrVideoRow(post);
                break;
        }

        if (!TextUtils.isEmpty(post.getAllowRePost())) {
            if (post.getAllowRePost().equals("1")) {
                mBinding.imgRepost.setVisibility(View.VISIBLE);
            } else {
                mBinding.imgRepost.setVisibility(View.GONE);
            }
        } else {
            mBinding.imgRepost.setVisibility(View.VISIBLE);
        }

    }


    private void imageOrVideoRow(SinglePost.Post post) {
        ArrayList<String> arrayList = Utility.extractUrls(post.getDescription());
        if (arrayList.size() != 0) {
            videoURL = arrayList.get(0);
            if (Utility.isValidUrl(videoURL)) {
                mBinding.playBtn.setVisibility(View.VISIBLE);
                try {
                    if (videoURL.contains("www.youtube.com")) {
                        String url = "https://img.youtube.com/vi/" + videoURL.split("\\=")[1] + "/0.jpg";
                        Glide.with(ViewPostActivity.this)
                                .load(url)
                                .into(mBinding.image);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final Bitmap bitmap = Utility.retriveVideoFrameFromVideo(videoURL);
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Glide.with(ViewPostActivity.this)
                                                    .load(bitmap)
                                                    .into(mBinding.image);
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
                    mBinding.playBtn.setVisibility(View.GONE);
                }
            } else {
                mBinding.playBtn.setVisibility(View.GONE);
            }
        } else {
            mBinding.playBtn.setVisibility(View.GONE);
            Glide.with(ViewPostActivity.this)
                    .load(post.getFilePath() + post.getImage())
                    .into(mBinding.image);
        }
    }

    /**
     * On click user name
     */
    public void onClickName(SinglePost.Post post) {
        GetCollegeUserResponse.User user = new GetCollegeUserResponse.User();
        user.setUserId(post.getUserId());
        user.setFullName(post.getFullName());
        user.setIsFollow("0");
        Intent intent = new Intent(ViewPostActivity.this, UserProfileActivity.class);
        intent.putExtra(UserProfileActivity.USER, user);
        startActivity(intent);
    }

    public void onImageClick(SinglePost.Post post) {
        ArrayList<String> arrayList = Utility.extractUrls(post.getDescription());
        if (arrayList.size() != 0) {
            String videoURL = arrayList.get(0);
            if (Utility.isValidUrl(videoURL)) {
                if (videoURL.contains("www.youtube.com")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
                    startActivity(browserIntent);
                } else {
                    startActivity(PlayerActivity.getVideoPlayerIntent(ViewPostActivity.this,
                            videoURL,
                            "Video title"));
                }
            }
        } else {
            String imageUrl = post.getFilePath() + post.getImage();
            if (!TextUtils.isEmpty(imageUrl)) {
                Intent intent = new Intent(ViewPostActivity.this, FullImageActivity.class);
                intent.putExtra("image", imageUrl);
                startActivity(intent);
            }
        }
    }

    private void showActionMenu(final SinglePost.Post post, int position, View view) {
        PopupMenu popup = new PopupMenu(ViewPostActivity.this, view);

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
                        Intent intent = new Intent(ViewPostActivity.this, EditPostActivity.class);
                        intent.putExtra("item", post);
                        startActivityForResult(intent, 192);
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

    public void onLikeClick(SinglePost.Post post) {
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

    public void onCommentBtnClick(SinglePost.Post post) {
        Intent intent = new Intent(ViewPostActivity.this, CommentPostActivity.class);
        intent.putExtra(CommentPostActivity.TAG_POST, post);
        intent.putExtra(CommentPostActivity.TAG_POSITION, 0);
        startActivityForResult(intent, 114);
    }

    public void refreshItem(int position, SinglePost.Post post) {
        onResume();
    }

    /**
     * show re post confirmation dialog
     *
     * @param post {@link SinglePost.Post}
     */
    public void onRePostBtnClick(final SinglePost.Post post) {
        Utility.setDialog(ViewPostActivity.this, "Message", "Do you want to repost this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        Intent intent = new Intent(ViewPostActivity.this, BoardSelectionActivity.class);
                        intent.putExtra("post_id", post.getPostId());
                        startActivityForResult(intent, 231);
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
        ProgressDialog.getInstance().showProgressDialog(ViewPostActivity.this);
        responseCall.enqueue(new Callback<RePostResponse>() {
            @Override
            public void onResponse(Call<RePostResponse> call, Response<RePostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showToast(ViewPostActivity.this, "Post successfully.");
            }

            @Override
            public void onFailure(Call<RePostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(ViewPostActivity.this);
            }
        });
    }


    /**
     * show save post confirmation dialog
     *
     * @param post {@link SinglePost.Post}
     */
    public void onSavePostBtnClick(final SinglePost.Post post) {
        if (!TextUtils.isEmpty(post.getIsBookmark())) {
            if (post.getIsBookmark().equals("1")) {
                onRemovePostBtnClick(post);
                return;
            }
        }
        Utility.setDialog(ViewPostActivity.this, "Message", "Do you want to bookmark this post.", "No", "Yes",
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
     * @param post {@link SinglePost.Post}
     */
    private void onRemovePostBtnClick(final SinglePost.Post post) {
        if (!TextUtils.isEmpty(post.getIsBookmark())) {
            if (post.getIsBookmark().equals("0")) {
                return;
            }
        }
        Utility.setDialog(ViewPostActivity.this, "Message", "Do you want to unbookmark this post.", "No", "Yes",
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
    private void savePost(final SinglePost.Post post) {
        Api api = APIClient.getClient().create(Api.class);
        Call<SavePostResponse> responseCall = api.savePost(loginUserId, post.getPostId());
        ProgressDialog.getInstance().showProgressDialog(ViewPostActivity.this);
        responseCall.enqueue(new Callback<SavePostResponse>() {
            @Override
            public void onResponse(Call<SavePostResponse> call, Response<SavePostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                post.setIsBookmark("1");
                // notifyItemChanged(postArrayList.indexOf(post));
                Utility.showToast(ViewPostActivity.this, "Bookmark Post successfully.");
            }

            @Override
            public void onFailure(Call<SavePostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(ViewPostActivity.this);
            }
        });
    }

    /**
     * Call service for save post
     *
     * @param post
     */
    private void removePost(final SinglePost.Post post) {
        Api api = APIClient.getClient().create(Api.class);
        Call<SavePostResponse> responseCall = api.removePost(loginUserId, post.getPostId());
        ProgressDialog.getInstance().showProgressDialog(ViewPostActivity.this);
        responseCall.enqueue(new Callback<SavePostResponse>() {
            @Override
            public void onResponse(Call<SavePostResponse> call, Response<SavePostResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                post.setIsBookmark("0");
                //   notifyItemChanged(postArrayList.indexOf(post));
                Utility.showToast(ViewPostActivity.this, "Bookmark Post successfully.");
            }

            @Override
            public void onFailure(Call<SavePostResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(ViewPostActivity.this);
            }
        });
    }

    private void showDeleteConfirmatinDialog(final SinglePost.Post post) {
        Utility.setDialog(ViewPostActivity.this, getString(R.string.alert), "Are you sure, You want to delete" +
                        " this post?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        deletePost(post);
                    }
                });
    }

    private void deletePost(SinglePost.Post post) {
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

    private void showReportConfirmatinDialog(final SinglePost.Post post) {
        Utility.setDialog(ViewPostActivity.this, getString(R.string.alert), "Are you sure, You want to report" +
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

    private void reportPost(SinglePost.Post post) {
        Api api = APIClient.getClient().create(Api.class);

    }

    public void onAudioPlayClick(SinglePost.Post post) {
        // Show DialogFragment
        PlayerDialogFragment.newInstance(
                post.getFilePath() + post.getAudioFile()
        ).show(getFragmentManager(), "Dialog Fragment");
    }

    /**
     * On poll option text view click listener
     *
     * @param post
     * @param option
     */
    public void onPollOptionSelect(SinglePost.Post post, int option) {
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
        //notifyItemChanged(postArrayList.indexOf(post));
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

    private void getAllCommentList() {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(ViewPostActivity.this);
        Call<GetAllCommentResponse> responseCall = api.getAllComment(postId);
        responseCall.enqueue(new Callback<GetAllCommentResponse>() {
            @Override
            public void onResponse(Call<GetAllCommentResponse> call, Response<GetAllCommentResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetAllCommentResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAllCommentResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(ViewPostActivity.this);
            }
        });
    }

    private void handleGetAllCommentResponse(GetAllCommentResponse allCommentResponse) {
        if (allCommentResponse == null) {
            Utility.showErrorMsg(ViewPostActivity.this);
        } else if (allCommentResponse.getResponseCode().equals(Api.SUCCESS)) {
            commentArrayList.clear();
            commentArrayList.addAll(allCommentResponse.getCommentArrayList());
            setCommentRecyclerView();
        } else {
            Utility.showToast(ViewPostActivity.this, "No comment.");
        }
    }

    private void setCommentRecyclerView() {
        mBinding.recyclerViewComment.setLayoutManager(new LinearLayoutManager(ViewPostActivity.this));
        CommentListAdapter adapter = new CommentListAdapter(ViewPostActivity.this, commentArrayList);
        mBinding.recyclerViewComment.setAdapter(adapter);
        mBinding.recyclerViewComment.setHasFixedSize(true);
    }
}