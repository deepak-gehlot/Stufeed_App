package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetAchievementListResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.RowAchivmentBinding;
import com.stufeed.android.databinding.RowFragmentAchivmentBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.AddAchievementActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AchivementFragmentListAdapter extends RecyclerView.Adapter<AchivementFragmentListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetAchievementListResponse.Achievement> achievementArrayList;

    public AchivementFragmentListAdapter(Context context, ArrayList<GetAchievementListResponse.Achievement>
            achievementArrayList) {
        this.context = context;
        this.achievementArrayList = achievementArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFragmentAchivmentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_fragment_achivment, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setModel(achievementArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return achievementArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowFragmentAchivmentBinding binding;

        private ViewHolder(RowFragmentAchivmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}