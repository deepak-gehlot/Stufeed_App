package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.BlockedUserListResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.RowBlockedUserBinding;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Deepak Gehlot on 4/9/2018.
 */
public class BlockedListAdapter extends RecyclerView.Adapter<BlockedListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BlockedUserListResponse.User> userArrayList;
    private String mLoginUserId = "";

    public BlockedListAdapter(Context context, ArrayList<BlockedUserListResponse.User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
        mLoginUserId = Utility.getLoginUserId(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBlockedUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.row_blocked_user, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setUser(userArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowBlockedUserBinding binding;

        public ViewHolder(RowBlockedUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Unblock user
     */
    public void onUnBlockClick(final BlockedUserListResponse.User user) {
        Api api = APIClient.getClient().create(Api.class);
        Call<Response> responseCall = api.unblockUser(mLoginUserId, "");
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response response1 = response.body();
                if (response1 != null) {
                    if (response1.getResponseCode().equals(Api.SUCCESS)) {
                        int index = userArrayList.indexOf(user);
                        userArrayList.remove(index);
                        notifyItemRemoved(index);
                    } else {
                        Utility.showToast(context, response1.getResponseMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Utility.showToast(context, context.getString(R.string.wrong));
            }
        });

    }
}