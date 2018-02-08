package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetCollegeUserResponse;
import com.stufeed.android.databinding.RowFacultyBinding;

import java.util.ArrayList;

public class FacultyListAdapter extends RecyclerView.Adapter<FacultyListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetCollegeUserResponse.User> userArrayList;

    public FacultyListAdapter(Context context, ArrayList<GetCollegeUserResponse.User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFacultyBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_faculty,
                parent, false);
        return new ViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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
}
