package com.stufeed.android.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.stufeed.android.R;
import com.stufeed.android.bean.EdukitItem;
import com.stufeed.android.databinding.ActivityEdukitSelectionBinding;
import com.stufeed.android.listener.OnItemClickListener;
import com.stufeed.android.view.adapter.EdukitListAdapter;

import java.util.ArrayList;

public class EdukitSelectionActivity extends AppCompatActivity {

    private ActivityEdukitSelectionBinding mBinding;
    private ArrayList<EdukitItem> edukitItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edukit_selection);
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        createList();
        setRecyclerView();
    }

    private void setRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(EdukitSelectionActivity.this));
        EdukitListAdapter adapter = new EdukitListAdapter(EdukitSelectionActivity.this, edukitItems);
        mBinding.recyclerView.setAdapter(adapter);
        adapter.onItemClick(new OnItemClickListener() {
            @Override
            public void onClick(int position, Object obj) {
                Intent intent = new Intent();
                intent.putExtra("edukit_id", edukitItems.get(position).getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void createList() {
        edukitItems = new ArrayList<>();
        EdukitItem edukitItem1 = new EdukitItem("1", "Schedule");
        EdukitItem edukitItem2 = new EdukitItem("2", "Campus Calender");
        EdukitItem edukitItem3 = new EdukitItem("3", "Time Table");
        EdukitItem edukitItem4 = new EdukitItem("4", "Result");
        EdukitItem edukitItem5 = new EdukitItem("5", "Document");
        EdukitItem edukitItem6 = new EdukitItem("6", "Syllabus");
        EdukitItem edukitItem7 = new EdukitItem("7", "Study Material");
        EdukitItem edukitItem8 = new EdukitItem("8", "Events");
        EdukitItem edukitItem9 = new EdukitItem("9", "Question Paper");
        EdukitItem edukitItem10 = new EdukitItem("10", "Others");
        edukitItems.add(edukitItem1);
        edukitItems.add(edukitItem2);
        edukitItems.add(edukitItem3);
        edukitItems.add(edukitItem4);
        edukitItems.add(edukitItem5);
        edukitItems.add(edukitItem6);
        edukitItems.add(edukitItem7);
        edukitItems.add(edukitItem8);
        edukitItems.add(edukitItem9);
        edukitItems.add(edukitItem10);
    }
}
