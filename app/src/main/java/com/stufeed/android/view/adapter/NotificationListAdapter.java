package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetJoinBoardRequestResponse;
import com.stufeed.android.api.response.JoinBoardResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.RowNotificationBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by HP on 2/28/2018.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetJoinBoardRequestResponse.Request> requestArrayList;
    private String mLoginUserId = "";
    private AQuery aQuery;

    public NotificationListAdapter(Context context, ArrayList<GetJoinBoardRequestResponse.Request> requestArrayList) {
        this.context = context;
        this.requestArrayList = requestArrayList;
        mLoginUserId = Utility.getLoginUserId(context);
        aQuery = new AQuery(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                        .row_notification, parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetJoinBoardRequestResponse.Request request = requestArrayList.get(position);
        holder.binding.setAdapter(this);
        holder.binding.setModel(request);
        holder.binding.textUserName.setText(request.getFullName());
        holder.binding.textMsg.setText(Html.fromHtml("Requested to join <b>" + request.getBoardName() + "</b> board"));

        if (TextUtils.isEmpty(request.getProfilePic())) {
            aQuery.id(holder.binding.profilePic).image(R.mipmap.ic_launcher_round);
        } else {
            aQuery.id(holder.binding.profilePic).image(request.getProfilePic(), true, true, 60, R.mipmap.ic_launcher_round);//R.drawable.person_icon
        }
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RowNotificationBinding binding;

        private ViewHolder(RowNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Accept join board request
     */
    public void acceptRequest(final GetJoinBoardRequestResponse.Request request) {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(context);
        Call<JoinBoardResponse> responseCall = api.acceptJoinBoard(mLoginUserId, request.getBoardId(), request.getJoinerId());
        responseCall.enqueue(new Callback<JoinBoardResponse>() {
            @Override
            public void onResponse(Call<JoinBoardResponse> call, retrofit2.Response<JoinBoardResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                int position = requestArrayList.indexOf(request);
                requestArrayList.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onFailure(Call<JoinBoardResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    /**
     * Reject join board request
     */
    public void rejectRequest(final GetJoinBoardRequestResponse.Request request) {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(context);
        Call<JoinBoardResponse> responseCall = api.rejectJoinBoard(mLoginUserId, request.getBoardId(), request.getJoinerId());
        responseCall.enqueue(new Callback<JoinBoardResponse>() {
            @Override
            public void onResponse(Call<JoinBoardResponse> call, retrofit2.Response<JoinBoardResponse> response) {
                ProgressDialog.getInstance().dismissDialog();
                int position = requestArrayList.indexOf(request);
                requestArrayList.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onFailure(Call<JoinBoardResponse> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}
