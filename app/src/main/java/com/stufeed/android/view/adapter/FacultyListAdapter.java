package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.RowFacultyBinding;

public class FacultyListAdapter extends RecyclerView.Adapter<FacultyListAdapter.ViewHolder> {

    private Context context;

    public FacultyListAdapter(Context context) {
        this.context = context;
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
        return 20;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private RowFacultyBinding rowBinding;

        public ViewHolder(RowFacultyBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
        }
    }
}
