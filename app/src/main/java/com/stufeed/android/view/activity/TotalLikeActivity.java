package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllLikes;
import com.stufeed.android.api.response.GetPostResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.ActivityTotalLikeBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.CommentListAdapter;
import com.stufeed.android.view.adapter.LikeListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalLikeActivity extends AppCompatActivity {
    public static final String TAG_POST = "post";
    public static final String TAG_POSITION = "position";
    private ActivityTotalLikeBinding binding;
    private GetPostResponse.Post post;
    private ArrayList<GetAllLikes.Like> likeArrayList=new ArrayList<>();
    private UserDetail userDetail;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_total_like);
        setSupportActionBar(binding.toolbar);
        binding.setActivity(this);
        getDataFromBundle();

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getAllLikes();
    }
    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        post = bundle.getParcelable(TAG_POST);
        position = bundle.getInt(TAG_POSITION);
        userDetail = Utility.getLoginUserDetail(TotalLikeActivity.this);
    }

    private void getAllLikes(){
        Api api= APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(TotalLikeActivity.this);
        Call<GetAllLikes> likesCall=api.getPostLikeUserList(post.getPostId());
        likesCall.enqueue(new Callback<GetAllLikes>() {
            @Override
            public void onResponse(Call<GetAllLikes> call, Response<GetAllLikes> response) {
                ProgressDialog.getInstance().dismissDialog();
                handleGetAllLikesResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetAllLikes> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showErrorMsg(TotalLikeActivity.this);

            }
        });
    }

    private void handleGetAllLikesResponse(GetAllLikes likesResponse){
        if (likesResponse == null) {
            Utility.showErrorMsg(TotalLikeActivity.this);
        }else if (likesResponse.getResponseCode().equals(Api.SUCCESS)){
            likeArrayList.clear();
            likeArrayList.addAll(likesResponse.getCommentArrayList());
            setLikeRecyclerView();
        }

    }
    private void setLikeRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(TotalLikeActivity.this));
        LikeListAdapter adapter = new LikeListAdapter(TotalLikeActivity.this, likeArrayList);
        binding.recyclerView.setAdapter(adapter);
        scrollToBottom();

    }
    private void scrollToBottom() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.smoothScrollToPosition(likeArrayList.size() - 1);
            }
        }, 100);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
