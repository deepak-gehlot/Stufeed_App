package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.GetJoinBoardListResponse;
import com.stufeed.android.api.response.JoinBoardResponse;
import com.stufeed.android.api.response.UserDetail;
import com.stufeed.android.databinding.RowJoinBoardBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.BoardDetailsMainActivity;
import com.stufeed.android.view.activity.InstitutePostActivity;
import com.stufeed.android.view.activity.PostActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 3/4/2018.
 */

public class JoinBoardListAdapter extends RecyclerView.Adapter<JoinBoardListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetJoinBoardListResponse.Board> list;
    private String loginUserId = "";
    private boolean isLoginUser = false;
    private String userType = "";

    public JoinBoardListAdapter(Context context, ArrayList<GetJoinBoardListResponse.Board> list,
                                boolean isLoginUser) {
        this.context = context;
        this.list = list;
        loginUserId = Utility.getLoginUserId(context);
        this.isLoginUser = isLoginUser;
        userType = Utility.getLoginUserDetail(context).getUserType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowJoinBoardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.row_join_board, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GetJoinBoardListResponse.Board board = list.get(position);
        holder.binding.setAdapter(this);
        holder.binding.setModel(list.get(position));
        holder.binding.iconSetting.setVisibility(View.INVISIBLE);

        holder.binding.btnJoin.setText("UnJoin");

        switch (board.getJoinType()) {
            case "0":   // not join
                if (board.getIsPrivate().equals("1")) {
                    holder.binding.btnJoin.setText("Join");
                } else {
                    holder.binding.btnJoin.setText("Join");
                }
                break;

            case "1":   // join
                holder.binding.btnJoin.setText("UnJoin");
                break;

            case "2":   //join request
                holder.binding.btnJoin.setText("Requested");
                break;
        }

        if (loginUserId.equals(board.getUserId())) {
            holder.binding.btnJoin.setText("Post");
        } else if (isLoginUser) {
            holder.binding.btnJoin.setText("UnJoin");
        }

        if (board.getIsPrivate().equals("1")) {
            holder.binding.iconLock.setVisibility(View.VISIBLE);
        } else {
            holder.binding.iconLock.setVisibility(View.INVISIBLE);
        }

        if (board.getIsCircle().equals("1")) {
            holder.binding.iconCircle.setVisibility(View.VISIBLE);
        } else {
            holder.binding.iconCircle.setVisibility(View.INVISIBLE);
        }

        Utility.setUserTypeColor(context, board.getUserType(), holder.binding.titleText);

        holder.binding.memberCount.setText(board.getMemberCount());
        holder.binding.postCount.setText(board.getPostCount());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardDetailsMainActivity.class);
                String boardId = list.get(holder.getAdapterPosition()).getBoardId();
                intent.putExtra("board_id", boardId);
                intent.putExtra("is_admin", false);
                intent.putExtra("BoardName",list.get(holder.getAdapterPosition()).getBoardName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowJoinBoardBinding binding;

        private ViewHolder(RowJoinBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * unJoin public board
     */
    public void unJoinBoard(final GetJoinBoardListResponse.Board board) {
        if (loginUserId.equals(board.getUserId())) {
            if (userType.equals("4")) {
                context.startActivity(new Intent(context, InstitutePostActivity.class));
            } else {
                context.startActivity(new Intent(context, PostActivity.class));
            }
        } else if (board.getIsPrivate().equals("1")) {  // private board
            requestJoinBoard(board);
        } else {  // public board
            ProgressDialog.getInstance().showProgressDialog(context);
            Api api = APIClient.getClient().create(Api.class);
            Call<JoinBoardResponse> responseCall = api.joinBoard(board.getUserId(), board.getBoardId(), loginUserId);
            responseCall.enqueue(new Callback<JoinBoardResponse>() {
                @Override
                public void onResponse(Call<JoinBoardResponse> call, Response<JoinBoardResponse> response) {
                    ProgressDialog.getInstance().dismissDialog();
                    int position = list.indexOf(board);
                    if (isLoginUser) {
                        list.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        if (board.getJoinType().equals("1")) {
                            board.setJoinType("0");
                        } else {
                            board.setJoinType("1");
                        }
                        //list.add(position, board);
                        notifyItemChanged(position);
                    }
                }

                @Override
                public void onFailure(Call<JoinBoardResponse> call, Throwable t) {
                    ProgressDialog.getInstance().dismissDialog();
                }
            });
        }
    }

    /**
     * Request private board join
     */
    private void requestJoinBoard(final GetJoinBoardListResponse.Board board) {
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
                notifyItemChanged(list.indexOf(board));
                ProgressDialog.getInstance().dismissDialog();
            }

            @Override
            public void onFailure(Call<JoinBoardResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}