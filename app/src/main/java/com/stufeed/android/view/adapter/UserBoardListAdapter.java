package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.JoinBoardResponse;
import com.stufeed.android.databinding.RowBoardBinding;
import com.stufeed.android.util.ProgressDialog;
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
        GetBoardListResponse.Board board = boardArrayList.get(position);
        holder.rowBoardBinding.setModel(board);
        holder.rowBoardBinding.setAdapter(this);

        if (board.getIsPrivate().equals("1")) {
            holder.rowBoardBinding.iconLock.setVisibility(View.VISIBLE);
        } else {
            holder.rowBoardBinding.iconLock.setVisibility(View.GONE);
        }

        if (board.getIsCircle().equals("1")) {
            holder.rowBoardBinding.iconCircle.setVisibility(View.VISIBLE);
        } else {
            holder.rowBoardBinding.iconCircle.setVisibility(View.GONE);
        }

        holder.rowBoardBinding.iconSetting.setVisibility(View.GONE);

        //android:text='@{model.isPrivate.equals("1") ? "Request" : "Join"}'
        switch (board.getJoinType()) {
            case "0":   // not join
                if (board.getIsPrivate().equals("1")) {
                    holder.rowBoardBinding.btnJoin.setText("Join");
                } else {
                    holder.rowBoardBinding.btnJoin.setText("Join");
                }
                break;

            case "1":   // join
                holder.rowBoardBinding.btnJoin.setText("UnJoin");
                break;

            case "2":   //join request
                holder.rowBoardBinding.btnJoin.setText("Requested");
                break;
        }
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

    /**
     * On Join Button click
     */
    public void onJoinClick(GetBoardListResponse.Board board) {
        if (board.getIsPrivate().equals("1")) {  // private board
            requestJoinBoard(board);
        } else {  // public board
            joinBoard(board);
        }
    }

    /**
     * Join public board
     */
    private void joinBoard(final GetBoardListResponse.Board board) {
        ProgressDialog.getInstance().showProgressDialog(context);
        Api api = APIClient.getClient().create(Api.class);
        Call<JoinBoardResponse> responseCall = api.joinBoard(board.getUserId(), board.getBoardId(), loginUserId);
        responseCall.enqueue(new Callback<JoinBoardResponse>() {
            @Override
            public void onResponse(Call<JoinBoardResponse> call, Response<JoinBoardResponse> response) {
                if (board.getJoinType().equals("1")) {
                    board.setJoinType("0");
                } else {
                    board.setJoinType("1");
                }
                notifyItemChanged(boardArrayList.indexOf(board));
                ProgressDialog.getInstance().dismissDialog();
            }

            @Override
            public void onFailure(Call<JoinBoardResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    /**
     * Request private board join
     */
    private void requestJoinBoard(final GetBoardListResponse.Board board) {
        ProgressDialog.getInstance().showProgressDialog(context);
        Api api = APIClient.getClient().create(Api.class);
        Call<JoinBoardResponse> responseCall = api.requestJoinBoard(board.getUserId(), board.getBoardId(), loginUserId);
        responseCall.enqueue(new Callback<JoinBoardResponse>() {
            @Override
            public void onResponse(Call<JoinBoardResponse> call, Response<JoinBoardResponse> response) {
                if (board.getJoinType().equals("2")) {
                    board.setJoinType("0");
                } else {
                    board.setJoinType("2");
                }

                notifyItemChanged(boardArrayList.indexOf(board));
                ProgressDialog.getInstance().dismissDialog();
            }

            @Override
            public void onFailure(Call<JoinBoardResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}