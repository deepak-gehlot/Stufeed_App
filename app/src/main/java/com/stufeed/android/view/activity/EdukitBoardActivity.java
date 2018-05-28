package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetJoinBoardListResponse;
import com.stufeed.android.bean.EdukitItem;
import com.stufeed.android.databinding.ActivityEdukitBoardBinding;
import com.stufeed.android.listener.OnItemClickListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.BoardSelectionAdapter;
import com.stufeed.android.view.adapter.EdukitListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EdukitBoardActivity extends AppCompatActivity {

    private ActivityEdukitBoardBinding mBinding;
    private ArrayList<EdukitItem> edukitItems;
    private String loginUserId = "";
    ArrayList<GetBoardListResponse.Board> mBoardArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edukit_board);
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loginUserId = Utility.getLoginUserId(this);
        getBoardList();

    }

    /*private void createList() {
        edukitItems = new ArrayList<>();
        EdukitItem edukitItem1 = new EdukitItem("1", "Academic Notice");
        EdukitItem edukitItem2 = new EdukitItem("2", "Faculty Notice");
        EdukitItem edukitItem3 = new EdukitItem("3", "Department");
        EdukitItem edukitItem4 = new EdukitItem("4", "Exam Notice");
        EdukitItem edukitItem5 = new EdukitItem("5", "Hostel Notice");
        EdukitItem edukitItem6 = new EdukitItem("6", "Almuni");
        EdukitItem edukitItem7 = new EdukitItem("7", "Event and Fest");
        EdukitItem edukitItem8 = new EdukitItem("8", "Placement");
        edukitItems.add(edukitItem1);
        edukitItems.add(edukitItem2);
        edukitItems.add(edukitItem3);
        edukitItems.add(edukitItem4);
        edukitItems.add(edukitItem5);
        edukitItems.add(edukitItem6);
        edukitItems.add(edukitItem7);
        edukitItems.add(edukitItem8);
    }*/

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
            Utility.showErrorMsg(EdukitBoardActivity.this);
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
        Call<GetJoinBoardListResponse> responseCall = api.getJoinBoardList(loginUserId, loginUserId);
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
        setRecyclerView();
    }

    private void setRecyclerView() {
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
        if (mBoardArrayList != null) {
            mBoardArrayList.add(0, new GetBoardListResponse.Board());
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(EdukitBoardActivity.this));
            BoardSelectionAdapter adapter = new BoardSelectionAdapter(EdukitBoardActivity.this, mBoardArrayList);
            mBinding.recyclerView.setAdapter(adapter);

            adapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(int position, Object obj) {
                    String boardId = "";
                    if (position == 0) {
                        boardId = "0";
                    } else {
                        boardId = mBoardArrayList.get(position).getBoardId();
                    }

                    Intent intent = new Intent();
                    intent.putExtra("board_id", boardId);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}