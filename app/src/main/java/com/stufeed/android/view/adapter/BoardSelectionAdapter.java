package com.stufeed.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.api.response.GetBoardListResponse;
import com.stufeed.android.databinding.RowBoardNameBinding;
import com.stufeed.android.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by HP on 3/8/2018.
 */
public class BoardSelectionAdapter extends RecyclerView.Adapter<BoardSelectionAdapter.ViewHolder> {

    private ArrayList<GetBoardListResponse.Board> boardArrayList;
    private Context context;
    private OnItemClickListener itemClickListener;
    private ArrayList<Boolean> checkList = new ArrayList<>();

    public BoardSelectionAdapter(Context context, ArrayList<GetBoardListResponse.Board> boardArrayList) {
        this.context = context;
        this.boardArrayList = boardArrayList;

        for (int i = 0; i < boardArrayList.size(); i++) {
            checkList.add(false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBoardNameBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_board_name,
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == 0) {
            holder.binding.textViewBoardName.setText("Post in Public");
            holder.binding.layoutRow.setVisibility(View.GONE);
        } else {
            holder.binding.layoutRow.setVisibility(View.VISIBLE);
            holder.binding.textViewBoardName.setText(boardArrayList.get(position).getBoardName());
            holder.binding.textViewMemberCount.setText(" Members : " + boardArrayList.get(position).getMemberCount());
            holder.binding.textViewTotalPostCount.setText(" Posts : " + boardArrayList.get(position).getPostCount());
        }

        if (checkList.get(position)) {
            holder.binding.textViewBoardName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_box_checl, 0, 0, 0);
        } else {
            holder.binding.textViewBoardName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_box_uncheck, 0, 0, 0);
        }

        holder.binding.textViewBoardName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkList.set(holder.getAdapterPosition(), !checkList.get(holder.getAdapterPosition()));
                notifyItemChanged(holder.getAdapterPosition());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < boardArrayList.size(); i++) {
                    if (checkList.get(i)) {
                        if (!TextUtils.isEmpty(stringBuilder.toString())) {
                            stringBuilder.append(",");
                        }
                        if (TextUtils.isEmpty(boardArrayList.get(i).getBoardId())) {
                            stringBuilder.append("0");
                        } else {
                            stringBuilder.append(boardArrayList.get(i).getBoardId());
                        }
                    }
                }
                itemClickListener.onClick(0, stringBuilder.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RowBoardNameBinding binding;

        public ViewHolder(RowBoardNameBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
