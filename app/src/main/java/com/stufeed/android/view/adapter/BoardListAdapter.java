package com.stufeed.android.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.CreateBoardResponse;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.api.response.JoinBoardResponse;
import com.stufeed.android.databinding.DialogEditBoardBinding;
import com.stufeed.android.databinding.RowMyBoardBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.BlockedUserListActivity;
import com.stufeed.android.view.activity.BoardDetailsMainActivity;
import com.stufeed.android.view.activity.HomeActivity;
import com.stufeed.android.view.activity.InstitutePostActivity;
import com.stufeed.android.view.activity.PostActivity;
import com.stufeed.android.view.fragment.BoardFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<GetBoardListResponse.Board> boardArrayList;
    private String mLoginUserId = "";
    private String userType = "";

    public BoardListAdapter(Activity context, ArrayList<GetBoardListResponse.Board> boardArrayList) {
        this.context = context;
        this.boardArrayList = boardArrayList;
        mLoginUserId = Utility.getLoginUserId(context);
        userType = Utility.getLoginUserDetail(context).getUserType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowMyBoardBinding rowBoardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                        .row_my_board, parent,
                false);
        return new ViewHolder(rowBoardBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GetBoardListResponse.Board board = boardArrayList.get(position);
        holder.rowBoardBinding.setModel(boardArrayList.get(position));
        holder.rowBoardBinding.setAdapter(this);

        // holder.rowBoardBinding.btnJoin.setText("Post");

        if (board.getUserId().equals(mLoginUserId)) {
            holder.rowBoardBinding.btnJoin.setText("Post");
        } else {
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

        if (board.getUserId().equals(mLoginUserId)) {
            holder.rowBoardBinding.iconSetting.setVisibility(View.VISIBLE);
        } else {
            holder.rowBoardBinding.iconSetting.setVisibility(View.INVISIBLE);
        }

        Utility.setUserTypeColor(context, board.getUserType(), holder.rowBoardBinding.titleText);

        holder.rowBoardBinding.memberCount.setText(board.getJoinCount());
        holder.rowBoardBinding.postCount.setText(board.getPostCount());

        holder.rowBoardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardDetailsMainActivity.class);
                String boardId = boardArrayList.get(holder.getAdapterPosition()).getBoardId();
                String boardName=boardArrayList.get(holder.getAdapterPosition()).getBoardName();
                intent.putExtra("board_id", boardId);
                intent.putExtra("is_admin", true);
                intent.putExtra("BoardName",boardName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardArrayList == null ? 0 : boardArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowMyBoardBinding rowBoardBinding;

        private ViewHolder(RowMyBoardBinding rowBoardBinding) {
            super(rowBoardBinding.getRoot());
            this.rowBoardBinding = rowBoardBinding;
        }
    }

    /**
     * On Post button click method
     */
    public void onJoinClick(GetBoardListResponse.Board board) {
        if (board.getUserId().equals(mLoginUserId)) {
            if (userType.equals("4")) {
                Intent intent = new Intent(context, InstitutePostActivity.class);
                intent.putExtra("code", InstitutePostActivity.SELECT_BOARD);
                context.startActivity(intent);
            } else {
                context.startActivity(new Intent(context, PostActivity.class));
            }
        } else if (board.getIsPrivate().equals("1")) {  // private board
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
        Call<JoinBoardResponse> responseCall = api.joinBoard(board.getUserId(), board.getBoardId(), mLoginUserId);
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
        Call<JoinBoardResponse> responseCall = api.requestJoinBoard(board.getUserId(), board.getBoardId(), mLoginUserId);
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

    /**
     * Show create board dialog
     */
    public void onSettingClick(final GetBoardListResponse.Board board) {
        final DialogEditBoardBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context)
                , R.layout.dialog_edit_board, null, false);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setTitle("Edit Board");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialogBinding.edtNameBoard.setText(board.getBoardName());
        dialogBinding.edtDescriptionBoard.setText(board.getBoardDescription());

        if (board.getIsPrivate().equals("1")) {
            dialogBinding.switchPrivate.setChecked(true);
        } else {
            dialogBinding.switchPrivate.setChecked(false);
        }

        if (board.getIsCircle().equals("1")) {
            dialogBinding.switchCircle.setChecked(true);
        } else {
            dialogBinding.switchCircle.setChecked(false);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switch (v.getId()) {
                    case R.id.done_board:

                        String title = dialogBinding.edtNameBoard.getText().toString().trim();
                        String description = dialogBinding.edtDescriptionBoard.getText().toString().trim();
                        boolean isPrivate = dialogBinding.switchPrivate.isChecked();
                        boolean isCircle = dialogBinding.switchCircle.isChecked();

                        if (!TextUtils.isEmpty(title)) {
                            dialog.dismiss();
                            editBoard(title, description, isPrivate, isCircle, board);
                        } else {
                            Utility.showToast(context, context.getString(R.string.all_required));
                        }

                        break;

                    case R.id.textViewArchiveBoard:
                        archiveBoard(board);
                        break;

                    case R.id.textViewDeleteBoard:
                        deleteBoard(board);
                        break;
                }
            }
        };

        dialogBinding.textViewArchiveBoard.setOnClickListener(onClickListener);
        dialogBinding.textViewDeleteBoard.setOnClickListener(onClickListener);
        dialogBinding.cancelBoard.setOnClickListener(onClickListener);
        dialogBinding.doneBoard.setOnClickListener(onClickListener);

        dialog.show();
    }

    /**
     * Create board service
     *
     * @param title       board title
     * @param description board description
     * @param isPrivate   true if board is private
     * @param isCircle    true if allow user to post
     */

    private void editBoard(String title, String description, boolean isPrivate, boolean isCircle,
                           final GetBoardListResponse.Board board) {
        int boardPrivate = 0, boardCircle = 0;
        if (isPrivate) {
            boardPrivate = 1;
        }
        if (isCircle) {
            boardCircle = 1;
        }

        board.setIsPrivate("" + boardPrivate);
        board.setIsCircle("" + boardCircle);
        board.setBoardName(title);
        board.setBoardDescription(description);

        Api api = APIClient.getClient().create(Api.class);
        Call<CreateBoardResponse> responseCall = api.updateBoard(board.getBoardId(), title,
                description, boardPrivate, boardCircle);
        ProgressDialog.getInstance().showProgressDialog(context);
        responseCall.enqueue(new Callback<CreateBoardResponse>() {
            @Override
            public void onResponse(Call<CreateBoardResponse> call, Response<CreateBoardResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                int position = boardArrayList.indexOf(board);
                boardArrayList.set(position, board);
                notifyItemChanged(position);
                //handleCreateBoardResponse(response.body());
            }

            @Override
            public void onFailure(Call<CreateBoardResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showToast(context, context.getString(R.string.wrong));
            }
        });
    }

    /**
     * Archive board
     */
    private void archiveBoard(final GetBoardListResponse.Board board) {
        ProgressDialog.getInstance().showProgressDialog(context);
        Api api = APIClient.getClient().create(Api.class);
        Call<com.stufeed.android.api.response.Response> responseCall = api.archiveBoard(board.getBoardId());
        responseCall.enqueue(new Callback<com.stufeed.android.api.response.Response>() {
            @Override
            public void onResponse(Call<com.stufeed.android.api.response.Response> call, Response<com.stufeed.android
                    .api.response.Response> response) {
                com.stufeed.android.api.response.Response response1 = response.body();
                ProgressDialog.getInstance().dismissDialog();
                if (response1 != null) {
                    if (response1.getResponseCode().equals(Api.SUCCESS)) {
                        int position = boardArrayList.indexOf(board);
                        boardArrayList.remove(position);
                        notifyItemRemoved(position);
                        Utility.showToast(context, response1.getResponseMessage());
                    } else {
                        Utility.showToast(context, response1.getResponseMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {
                Utility.showErrorMsg(context);
            }
        });
    }

    /**
     * Delete board
     */
    private void deleteBoard(final GetBoardListResponse.Board board) {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(context);
        Call<com.stufeed.android.api.response.Response> responseCall = api.deleteBoard(board.getBoardId());
        responseCall.enqueue(new Callback<com.stufeed.android.api.response.Response>() {
            @Override
            public void onResponse(Call<com.stufeed.android.api.response.Response> call, Response<com.stufeed.android
                    .api.response.Response> response) {
                com.stufeed.android.api.response.Response response1 = response.body();
                ProgressDialog.getInstance().dismissDialog();
                if (response1 != null) {
                    if (response1.getResponseCode().equals(Api.SUCCESS)) {
                        int position = boardArrayList.indexOf(board);
                        boardArrayList.remove(position);
                        notifyItemRemoved(position);
                        Utility.showToast(context, response1.getResponseMessage());
                        BoardFragment.boardCount = boardArrayList.size();
                    } else {
                        Utility.showToast(context, response1.getResponseMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {
                Utility.showErrorMsg(context);
            }
        });
    }
}