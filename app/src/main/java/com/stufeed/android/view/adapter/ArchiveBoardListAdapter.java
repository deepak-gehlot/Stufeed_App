package com.stufeed.android.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.CreateBoardResponse;
import com.stufeed.android.api.response.GetArchiveBoardListResponse;
import com.stufeed.android.databinding.DialogEditBoardBinding;
import com.stufeed.android.databinding.RowArchiveBoardBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.BoardDetailsMainActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Deepak Gehlot on 3/8/2018.
 */

public class ArchiveBoardListAdapter extends RecyclerView.Adapter<ArchiveBoardListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetArchiveBoardListResponse.Board> boardArrayList;
    private String mLoginUserId = "";

    public ArchiveBoardListAdapter(Context context, ArrayList<GetArchiveBoardListResponse.Board> boardArrayList) {
        this.context = context;
        this.boardArrayList = boardArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowArchiveBoardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout
                .row_archive_board, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.setAdapter(this);
        holder.binding.setModel(boardArrayList.get(position));
        final GetArchiveBoardListResponse.Board board = boardArrayList.get(position);

        if (board.getIsPrivate().equals("1")) {
            holder.binding.iconLock.setVisibility(View.VISIBLE);
        } else {
            holder.binding.iconLock.setVisibility(View.GONE);
        }

        if (board.getIsCircle().equals("1")) {
            holder.binding.iconCircle.setVisibility(View.VISIBLE);
        } else {
            holder.binding.iconCircle.setVisibility(View.GONE);
        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardDetailsMainActivity.class);
                String boardId = boardArrayList.get(holder.getAdapterPosition()).getBoardId();
                intent.putExtra("board_id", boardId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowArchiveBoardBinding binding;

        public ViewHolder(RowArchiveBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    /**
     * Show create board dialog
     */
    public void onSettingClick(final GetArchiveBoardListResponse.Board board) {
        final DialogEditBoardBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context)
                , R.layout.dialog_edit_board, null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setTitle("Edit Board");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialogBinding.edtNameBoard.setText(board.getBoardName());
        dialogBinding.edtDescriptionBoard.setText(board.getBoardDescription());
        dialogBinding.textViewArchiveBoard.setText("Move To Profile");

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

                        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                            dialog.dismiss();
                            editBoard(title, description, isPrivate, isCircle, board);
                        } else {
                            Utility.showToast(context, context.getString(R.string.all_required));
                        }

                        break;

                    case R.id.textViewArchiveBoard: // move to profile button
                        moveToProfileBoard(board);
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
                           final GetArchiveBoardListResponse.Board board) {
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
            }

            @Override
            public void onFailure(Call<CreateBoardResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showToast(context, context.getString(R.string.wrong));
            }
        });
    }

    /**
     * Delete board
     */
    private void deleteBoard(final GetArchiveBoardListResponse.Board board) {
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
                    } else {
                        Utility.showToast(context, response1.getResponseMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<com.stufeed.android.api.response.Response> call, Throwable t) {
                Utility.showToast(context, context.getString(R.string.wrong));
            }
        });
    }


    /**
     * Move to profile board
     */
    private void moveToProfileBoard(final GetArchiveBoardListResponse.Board board) {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(context);
        Call<com.stufeed.android.api.response.Response> responseCall = api.unarchiveBoard(board.getBoardId());
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
                Utility.showToast(context, context.getString(R.string.wrong));
            }
        });
    }
}