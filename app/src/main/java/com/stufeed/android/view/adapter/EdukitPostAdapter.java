package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.stufeed.android.R;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetEdukitResponse;
import com.stufeed.android.databinding.RowEdukitItemBinding;
import com.stufeed.android.view.viewmodel.EdukitPostModel;

import java.util.ArrayList;

public class EdukitPostAdapter extends RecyclerView.Adapter<EdukitPostAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetEdukitResponse.EdukitPost> postArrayList;
    private AQuery aQuery;

    public EdukitPostAdapter(Context context, ArrayList<GetEdukitResponse.EdukitPost> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        aQuery = new AQuery(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowEdukitItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_edukit_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetEdukitResponse.EdukitPost model = postArrayList.get(position);
        holder.binding.setModel(model);

        String url = "";
        if (TextUtils.isEmpty(model.getImage())) {
            url = Api.IMAGE_URL + model.getFile();
        } else {
            url = Api.IMAGE_URL + model.getImage();
        }
        aQuery.id(holder.itemView).image(url, true, true, 200, R.drawable.user_default);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowEdukitItemBinding binding;

        public ViewHolder(final RowEdukitItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.textViewTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int visibility = binding.layoutFullView.getVisibility();
                    binding.layoutFullView.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
}