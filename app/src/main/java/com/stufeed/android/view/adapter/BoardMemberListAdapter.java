package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetBoardMemberListResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.RowBoardMemberBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.UserProfileActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Deepak Gehlot on 4/9/2018.
 */

public class BoardMemberListAdapter extends RecyclerView.Adapter<BoardMemberListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetBoardMemberListResponse.User> userArrayList;
    private boolean isAdmin;
    private String mLoginUserId = "";
    private String boardId = "";

    public BoardMemberListAdapter(Context context, String loginUserId,
                                  ArrayList<GetBoardMemberListResponse.User> userArrayList,
                                  String boardId, boolean isAdmin) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.isAdmin = isAdmin;
        this.boardId = boardId;
        mLoginUserId = loginUserId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBoardMemberBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout
                .row_board_member, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetBoardMemberListResponse.User user = userArrayList.get(position);
        holder.binding.setUser(user);
        holder.binding.setAdapter(this);
        holder.binding.setRemove(isAdmin);
        Utility.setUserTypeIconColor(context, user.getUserType(), holder.binding.userTypeView);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowBoardMemberBinding binding;

        public ViewHolder(RowBoardMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void onClickRemove(final GetBoardMemberListResponse.User user) {
        Utility.setDialog(context, "Alert", "Do you want to remove from board?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        removeUser(user);
                    }
                });
    }

    private void removeUser(final GetBoardMemberListResponse.User user) {
        ProgressDialog.getInstance().showProgressDialog(context);
        Api api = APIClient.getClient().create(Api.class);
        Call<Response> responseCall = api.removeBoardMember(mLoginUserId, boardId, user.getUserId());
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                ProgressDialog.getInstance().dismissDialog();
                int index = userArrayList.indexOf(user);
                userArrayList.remove(index);
                notifyItemRemoved(index);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showToast(context, context.getString(R.string.wrong));

            }
        });
    }
    public void onClickUserName(GetBoardMemberListResponse.User user){
        if (!TextUtils.isEmpty(user.getUserId()) && !mLoginUserId.equals(user.getUserId())) {
            GetCollegeUserResponse.User users = new GetCollegeUserResponse.User();
            users.setUserId(user.getUserId());
            users.setFullName(user.getFullName());
            users.setIsFollow("0");
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra(UserProfileActivity.USER, users);
            context.startActivity(intent);
        }

    }
}
