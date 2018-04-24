package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetBoardMemberListResponse;
import com.stufeed.android.databinding.RowBoardMemberBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;

import java.util.ArrayList;

/**
 * Created by Deepak Gehlot on 4/9/2018.
 */

public class BoardMemberListAdapter extends RecyclerView.Adapter<BoardMemberListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetBoardMemberListResponse.User> userArrayList;
    private boolean isAdmin;

    public BoardMemberListAdapter(Context context,
                                  ArrayList<GetBoardMemberListResponse.User> userArrayList,
                                  boolean isAdmin) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.isAdmin = isAdmin;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBoardMemberBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout
                .row_board_member, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setUser(userArrayList.get(position));
        holder.binding.setAdapter(this);
        holder.binding.setRemove(isAdmin);

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

    public void onClickRemove() {
        Utility.setDialog(context, "Alert", "Do you want to remove from board?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                        ProgressDialog.getInstance().showProgressDialog(context);
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }
}
