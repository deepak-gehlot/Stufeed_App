package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetJoinBoardListResponse;
import com.stufeed.android.databinding.ActivityBoardSelectionBinding;
import com.stufeed.android.listener.OnItemClickListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.BoardSelectionAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardSelectionActivity extends AppCompatActivity {

    private ActivityBoardSelectionBinding mBinding;
    ArrayList<GetBoardListResponse.Board> mBoardArrayList = new ArrayList<>();
    private String loginUserId;
    private String postId = "";
    String ids = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_board_selection);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loginUserId = Utility.getLoginUserId(this);
        getBoardList();
        getDataFromBundle();

        mBinding.buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ids)) {
                    Intent intent = new Intent();
                    intent.putExtra("board_id", ids);
                    intent.putExtra("post_id", postId);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            postId = bundle.getString("post_id");
        }
    }

    private void getBoardList() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
        Api api = APIClient.getClient().create(Api.class);
        Call<GetBoardListResponse> responseCall = api.getBoardList(loginUserId);
        responseCall.enqueue(new Callback<GetBoardListResponse>() {
            @Override
            public void onResponse(Call<GetBoardListResponse> call, Response<GetBoardListResponse> response) {
                handleBoardListResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetBoardListResponse> call, Throwable t) {
                handleBoardListResponse(null);
            }
        });
    }

    private void handleBoardListResponse(GetBoardListResponse response) {
        if (response == null) {
            Utility.showErrorMsg(BoardSelectionActivity.this);
            return;
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            mBoardArrayList = response.getBoardArrayList();
        }
        getJoinedBoardList();
    }

    /**
     * Get login user board list
     */
    private void getJoinedBoardList() {
        Api api = APIClient.getClient().create(Api.class);
        Call<GetJoinBoardListResponse> responseCall = api.getJoinBoardList(loginUserId,loginUserId);
        responseCall.enqueue(new Callback<GetJoinBoardListResponse>() {
            @Override
            public void onResponse(Call<GetJoinBoardListResponse> call, Response<GetJoinBoardListResponse> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<GetJoinBoardListResponse> call, Throwable t) {
                handleResponse(null);
            }
        });
    }

    private void handleResponse(GetJoinBoardListResponse response) {
        if (response != null) {
            if (response.getResponseCode().equals(Api.SUCCESS)) {
                if (response.getBoardArrayList() != null) {
                    ArrayList<GetJoinBoardListResponse.Board> boardArrayList = response.getBoardArrayList();
                    for (int i = 0; i < boardArrayList.size(); i++) {
                        GetJoinBoardListResponse.Board board = boardArrayList.get(i);
                        if (board.getIsCircle().equalsIgnoreCase("1")) {
                            GetBoardListResponse.Board newBoard = new GetBoardListResponse.Board();
                            newBoard.setBoardName(board.getBoardName());
                            newBoard.setBoardId(board.getBoardId());
                            mBoardArrayList.add(newBoard);
                        }
                    }
                }
            }
        }
        setRecyclerView();
    }

    private void setRecyclerView() {
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        if (mBoardArrayList != null) {
            mBoardArrayList.add(0, new GetBoardListResponse.Board());
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(BoardSelectionActivity.this));
            BoardSelectionAdapter adapter = new BoardSelectionAdapter(BoardSelectionActivity.this, mBoardArrayList);
            mBinding.recyclerView.setAdapter(adapter);

            adapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(int position, Object obj) {
                    ids = ((String) obj);
                }
            });
        }
    }
}