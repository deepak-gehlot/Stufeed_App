package com.stufeed.android.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.stufeed.android.R;
import com.stufeed.android.databinding.ActivityBlockedUserListBinding;
import com.stufeed.android.view.adapter.BlockedListAdapter;

public class BlockedUserListActivity extends AppCompatActivity {

    private ActivityBlockedUserListBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_blocked_user_list);
        setRecyclerView();
    }

    private void setRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(BlockedUserListActivity.this));
        mBinding.recyclerView.setAdapter(new BlockedListAdapter(BlockedUserListActivity.this));
    }
}
