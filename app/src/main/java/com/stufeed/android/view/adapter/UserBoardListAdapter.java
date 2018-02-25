package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.JoinBoardResponse;
import com.stufeed.android.databinding.RowBoardBinding;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBoardListAdapter extends RecyclerView.Adapter<UserBoardListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetBoardListResponse.Board> boardArrayList;
    private String loginUserId = "";

    public UserBoardListAdapter(Context context, ArrayList<GetBoardListResponse.Board> boardArrayList) {
        this.context = context;
        this.boardArrayList = boardArrayList;
        loginUserId = Utility.getLoginUserId(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBoardBinding rowBoardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                        .row_board, parent,
                false);
        return new ViewHolder(rowBoardBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rowBoardBinding.setModel(boardArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return boardArrayList == null ? 0 : boardArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowBoardBinding rowBoardBinding;

        private ViewHolder(RowBoardBinding rowBoardBinding) {
            super(rowBoardBinding.getRoot());
            this.rowBoardBinding = rowBoardBinding;
        }
    }

    public void onJoinClick(GetBoardListResponse.Board board) {
        Api api = APIClient.getClient().create(Api.class);
        Call<JoinBoardResponse> responseCall = api.joinBoard(board.getUserId(), board.getBoardId(), loginUserId);
        responseCall.enqueue(new Callback<JoinBoardResponse>() {
            @Override
            public void onResponse(Call<JoinBoardResponse> call, Response<JoinBoardResponse> response) {

            }

            @Override
            public void onFailure(Call<JoinBoardResponse> call, Throwable t) {

            }
        });
    }
}