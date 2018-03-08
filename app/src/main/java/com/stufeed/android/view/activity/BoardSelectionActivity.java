package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.databinding.ActivityBoardSelectionBinding;
import com.stufeed.android.listener.OnItemClickListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.BoardListAdapter;
import com.stufeed.android.view.adapter.BoardSelectionAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardSelectionActivity extends AppCompatActivity {

    private ActivityBoardSelectionBinding mBinding;
    private String loginUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_board_selection);
        loginUserId = Utility.getLoginUserId(this);
        getBoardList();
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
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        if (response == null) {
            Utility.showErrorMsg(BoardSelectionActivity.this);
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            setRecyclerView(response.getBoardArrayList());
        }
    }

    private void setRecyclerView(final ArrayList<GetBoardListResponse.Board> boardArrayList) {
        if (boardArrayList != null) {
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(BoardSelectionActivity.this));
            BoardSelectionAdapter adapter = new BoardSelectionAdapter(BoardSelectionActivity.this, boardArrayList);
            mBinding.recyclerView.setAdapter(adapter);

            adapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(int position, Object obj) {
                    String boardId = boardArrayList.get(position).getBoardId();
                    Intent intent = new Intent();
                    intent.putExtra("board_id", boardId);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}