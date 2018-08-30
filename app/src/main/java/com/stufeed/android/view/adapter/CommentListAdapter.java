package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAllCommentResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.RowCommentBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.CommentPostActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetAllCommentResponse.Comment> commentArrayList;
    private String mLoginUserId = "";

    public CommentListAdapter(Context context, ArrayList<GetAllCommentResponse.Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
        mLoginUserId = Utility.getLoginUserId(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCommentBinding rowCommentBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R
                        .layout.row_comment, parent, false);
        return new ViewHolder(rowCommentBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetAllCommentResponse.Comment comment = commentArrayList.get(position);
        holder.rowCommentBinding.setModel(comment);
        Utility.setUserTypeIconColor(context, comment.getUserType(), holder.rowCommentBinding.userTypeView);


        if (mLoginUserId.equals(comment.getUserId())) {
            holder.rowCommentBinding.delete.setVisibility(View.VISIBLE);
        } else {
            holder.rowCommentBinding.delete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return commentArrayList == null ? 0 : commentArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowCommentBinding rowCommentBinding;

        private ViewHolder(RowCommentBinding rowCommentBinding) {
            super(rowCommentBinding.getRoot());
            this.rowCommentBinding = rowCommentBinding;

            rowCommentBinding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String commentId = commentArrayList.get(position).getId();
                    String postId = commentArrayList.get(position).getPostId();
                    deleteCommentConfirmation(commentId, postId, position);
                }
            });

        }
    }

    private void deleteCommentConfirmation(final String commentId, final String postId, final int position) {
        Utility.setDialog(context, "Alert!", "Do you want to delete comment?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        ((CommentPostActivity) context).manageCount();
                        commentArrayList.remove(position);
                        notifyItemRemoved(position);
                        deleteComment(commentId, postId);
                    }
                });
    }

    private void deleteComment(String commentId, String postId) {
        Api api = APIClient.getClient().create(Api.class);
        Call<Response> call = api.deleteComment(mLoginUserId, commentId, postId);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d("ERROR",response.message());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }
}