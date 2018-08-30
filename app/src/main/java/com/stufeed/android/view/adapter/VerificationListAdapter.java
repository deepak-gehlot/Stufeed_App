package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.api.response.UnVerifyUserListResponse;
import com.stufeed.android.databinding.RowVerifyRequestBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.UserProfileActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class VerificationListAdapter extends RecyclerView.Adapter<VerificationListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<UnVerifyUserListResponse.Data> dataArrayList;
    private AQuery aQuery;

    public VerificationListAdapter(Context context, ArrayList<UnVerifyUserListResponse.Data> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        aQuery = new AQuery(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowVerifyRequestBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_verify_request, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setAdapter(this);

        UnVerifyUserListResponse.Data item = dataArrayList.get(position);
        holder.binding.textUserName.setText(item.getFullName());

        if (item.getUserType().equals("2")) {
            holder.binding.textMsg.setText("Request to join institute as Faculty.");
        } else if (item.getUserType().equals("3")) {
            holder.binding.textMsg.setText("Request to join institute as Department.");
        }

        holder.binding.setPosition(position);

        if (TextUtils.isEmpty(item.getProfilePic())) {
            aQuery.id(holder.binding.profilePic).image(R.mipmap.ic_launcher_round);
        } else {
            aQuery.id(holder.binding.profilePic).image(item.getProfilePic(), true, true, 60, R.mipmap.ic_launcher_round);//R.drawable.person_icon
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RowVerifyRequestBinding binding;

        public ViewHolder(RowVerifyRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }

    /**
     * Accept join board request
     */
    public void acceptRequest(final int position) {
        Utility.setDialog(context, "Alert!", "Do you want to verify this?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        String userId = dataArrayList.get(position).getUserId();
                        updateStatus(position, userId, "1");
                    }
                });
    }

    /**
     * Reject join board request
     */
    public void rejectRequest(final int position) {
        Utility.setDialog(context, "Alert!", "Do you want to reject this?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        String userId = dataArrayList.get(position).getUserId();
                        updateStatus(position, userId, "2");
                    }
                });
    }

    private void updateStatus(final int position, String userId, String status) {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(context);
        Call<Response> responseCall = api.updateVerifyStatus(userId, status);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                ProgressDialog.getInstance().dismissDialog();
                dataArrayList.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    public void onClickUserName( int position) {
        UnVerifyUserListResponse.Data item = dataArrayList.get(position);

        GetCollegeUserResponse.User user = new GetCollegeUserResponse.User();
        user.setUserId(item.getUserId());
        user.setFullName(item.getFullName());
        user.setIsFollow("0");
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(UserProfileActivity.USER, user);

        context.startActivity(intent);
    }
}
