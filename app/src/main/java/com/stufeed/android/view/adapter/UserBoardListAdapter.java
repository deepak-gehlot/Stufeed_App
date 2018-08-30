package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.stufeed.android.view.activity.BoardDetailsMainActivity;
import com.stufeed.android.view.activity.InstitutePostActivity;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GetBoardListResponse.Board board = boardArrayList.get(position);
        holder.rowBoardBinding.setModel(board);
        holder.rowBoardBinding.setAdapter(this);

        if (board.getIsPrivate().equals("1")) {
            holder.rowBoardBinding.iconLock.setVisibility(View.VISIBLE);
        } else {
            holder.rowBoardBinding.iconLock.setVisibility(View.INVISIBLE);
        }

        if (board.getIsCircle().equals("1")) {
            holder.rowBoardBinding.iconCircle.setVisibility(View.VISIBLE);
        } else {
            holder.rowBoardBinding.iconCircle.setVisibility(View.INVISIBLE);
        }

        Utility.setUserTypeColor(context, board.getUserType(), holder.rowBoardBinding.titleText);

        holder.rowBoardBinding.iconSetting.setVisibility(View.INVISIBLE);

        holder.rowBoardBinding.memberCount.setText(board.getJoinCount());
        holder.rowBoardBinding.postCount.setText(board.getPostCount());

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

        if (board.getUserId().equals(loginUserId)) {
            holder.rowBoardBinding.btnJoin.setText("Post");
        }

        holder.rowBoardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(board.getJoinType()) &&
                        board.getJoinType().equals("1")) {
                    Intent intent = new Intent(context, BoardDetailsMainActivity.class);
                    String boardId = boardArrayList.get(holder.getAdapterPosition()).getBoardId();
                    intent.putExtra("board_id", boardId);
                    intent.putExtra("is_admin", false);
                    intent.putExtra("BoardName", boardArrayList.get(holder.getAdapterPosition()).getBoardName());
                    context.startActivity(intent);
                }
            }
        });
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
        if (board.getUserId().equals(loginUserId)) {
            onClickPostInBoard();
        } else if (board.getIsPrivate().equals("1")) {  // private board
            if (!board.getJoinType().equals("2")) {
                requestJoinBoard(board);                // request join
            } else {
                cancelRequestJoinBoard(board);          // cancel request join
            }
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
                int position = boardArrayList.indexOf(board);
                if (board.getJoinType().equals("1")) {
                    board.setJoinType("0");
                } else {
                    board.setJoinType("1");
                }
                boardArrayList.set(position, board);
                notifyItemChanged(position);
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
                int position = boardArrayList.indexOf(board);
                if (board.getJoinType().equals("2") || board.getJoinType().equals("1")) {
                    board.setJoinType("0");
                } else {
                    board.setJoinType("2");
                }
                boardArrayList.set(position, board);
                notifyItemChanged(position);
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
    private void cancelRequestJoinBoard(final GetBoardListResponse.Board board) {
        ProgressDialog.getInstance().showProgressDialog(context);
        Api api = APIClient.getClient().create(Api.class);
        Call<JoinBoardResponse> responseCall = api.cancelRequestJoinBoard(board.getUserId(), board.getBoardId(), loginUserId);
        responseCall.enqueue(new Callback<JoinBoardResponse>() {
            @Override
            public void onResponse(Call<JoinBoardResponse> call, Response<JoinBoardResponse> response) {
                int position = boardArrayList.indexOf(board);
                if (board.getJoinType().equals("2") || board.getJoinType().equals("1")) {
                    board.setJoinType("0");
                } else {
                    board.setJoinType("2");
                }
                boardArrayList.set(position, board);
                notifyItemChanged(position);
                ProgressDialog.getInstance().dismissDialog();
            }

            @Override
            public void onFailure(Call<JoinBoardResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    /**
     * Select board click
     */
    public void onClickPostInBoard() {
        Intent intent = new Intent(context, InstitutePostActivity.class);
        intent.putExtra("code", InstitutePostActivity.SELECT_BOARD);
        context.startActivity(intent);
    }
}