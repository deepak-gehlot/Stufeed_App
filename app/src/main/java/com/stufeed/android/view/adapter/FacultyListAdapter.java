package com.stufeed.android.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.FollowResponse;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.databinding.RowFacultyBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.UserProfileActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyListAdapter extends RecyclerView.Adapter<FacultyListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetCollegeUserResponse.User> userArrayList;
    private String loginUserId = "";
    String userType = "";
    private AQuery aQuery;

    public FacultyListAdapter(Context context, String userType, ArrayList<GetCollegeUserResponse.User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.userType = userType;
        loginUserId = Utility.getLoginUserId(context);
        aQuery = new AQuery(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFacultyBinding rowBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_faculty,
                parent, false);
        return new ViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final GetCollegeUserResponse.User user = userArrayList.get(position);

        Utility.setUserTypeIconColor(context, userType, holder.rowBinding.userTypeView);

        holder.rowBinding.txtName.setText(user.getFullName());
        if (user.getIsFollow().equals("1")) {
            holder.rowBinding.txtFollow.setText(context.getString(R.string.un_follow));
        } else {
            holder.rowBinding.txtFollow.setText(context.getString(R.string.follow));
        }

        if (user.getVerifyStatus().equals("1")) {
            holder.rowBinding.verifyStatus.setVisibility(View.VISIBLE);
        } else {
            holder.rowBinding.verifyStatus.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(user.getProfilePic())) {
            aQuery.id(holder.rowBinding.imageView).image(R.mipmap.ic_launcher_round);
        } else {
            aQuery.id(holder.rowBinding.imageView).image(
                    user.getProfilePic(), true, true, 60, R.mipmap.ic_launcher_round
            );
        }

        holder.rowBinding.txtFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCollegeUserResponse.User user = userArrayList.get(holder.getAdapterPosition());
                if (user.getIsFollow().equals("1")) {
                    setUnFollowConfirmation(user);
                } else {
                    setFollowConfirmation(user);
                }
            }
        });

        holder.rowBinding.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCollegeUserResponse.User user1 = userArrayList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.USER, user1);
                intent.putExtra(UserProfileActivity.TYPE, userType);
                context.startActivity(intent);
            }
        });
    }

    public void filterList(ArrayList<GetCollegeUserResponse.User> userArrayList1) {
        this.userArrayList = userArrayList1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userArrayList == null ? 0 : userArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowFacultyBinding rowBinding;

        public ViewHolder(RowFacultyBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
        }
    }

    private void setFollowConfirmation(final GetCollegeUserResponse.User user) {
        Utility.setDialog(context, "Message", "Do you want to follow this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        follow(user);
                    }
                });
    }

    private void follow(GetCollegeUserResponse.User user) {
        int position = userArrayList.indexOf(user);
        if (user.getIsFollow().equals("1")) {
            user.setIsFollow("0");
        } else {
            user.setIsFollow("1");
        }
        userArrayList.set(position, user);
        notifyItemChanged(position);

        Api api = APIClient.getClient().create(Api.class);
        Call<FollowResponse> responseCall = api.follow(user.getUserId(), loginUserId);
        responseCall.enqueue(new Callback<FollowResponse>() {
            @Override
            public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {

            }

            @Override
            public void onFailure(Call<FollowResponse> call, Throwable t) {

            }
        });
    }


    private void setUnFollowConfirmation(final GetCollegeUserResponse.User user) {
        Utility.setDialog(context, "Message", "Do you want to un-follow this.", "No", "Yes",
                new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        follow(user);
                    }
                });
    }

    private void unFollow(GetCollegeUserResponse.User user) {

    }
}