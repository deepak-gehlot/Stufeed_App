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
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.AddAchievementActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AchivementListAdapter extends RecyclerView.Adapter<AchivementListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetAchievementListResponse.Achievement> achievementArrayList;

    public AchivementListAdapter(Context context, ArrayList<GetAchievementListResponse.Achievement>
            achievementArrayList) {
        this.context = context;
        this.achievementArrayList = achievementArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowAchivmentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_achivment, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setAdapter(this);
        holder.binding.setModel(achievementArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return achievementArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowAchivmentBinding binding;

        private ViewHolder(RowAchivmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void onEditButtonClick(GetAchievementListResponse.Achievement achievement) {
        Intent intent = new Intent(context, AddAchievementActivity.class);
        intent.putExtra("from", "1");
        intent.putExtra("item", achievement);
        context.startActivity(new Intent(intent));
    }

    public void onDeleteButtonClick(final GetAchievementListResponse.Achievement achievement) {
        Utility.setDialog(context, "Alert!", "Do you want to delete?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        int position = achievementArrayList.indexOf(achievement);
                        String id = achievementArrayList.get(position).getAchievmentId();
                        achievementArrayList.remove(position);
                        notifyItemRemoved(position);
                        deleteAchievement(id);
                    }
                });
    }

    public void deleteAchievement(String id) {
        Api api = APIClient.getClient().create(Api.class);
        Call<Response> responseCall = api.deleteAchievements(id);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }
}