package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetJoinBoardRequestResponse;
import com.stufeed.android.databinding.RowNotificationBinding;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;

/**
 * Created by HP on 2/28/2018.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetJoinBoardRequestResponse.Request> requestArrayList;
    private String mLoginUserId = "";

    public NotificationListAdapter(Context context, ArrayList<GetJoinBoardRequestResponse.Request> requestArrayList) {
        this.context = context;
        this.requestArrayList = requestArrayList;
        mLoginUserId = Utility.getLoginUserId(context);
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

    public void acceptRequest(GetJoinBoardRequestResponse.Request request) {
        Api api = APIClient.getClient().create(Api.class);
        ///api.joinBoard(mLoginUserId,request.getBoardId(),);
    }
}
