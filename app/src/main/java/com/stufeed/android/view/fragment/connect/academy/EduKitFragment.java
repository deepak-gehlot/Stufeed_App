package com.stufeed.android.view.fragment.connect.academy;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.FragmentEduKitBinding;
import com.stufeed.android.view.activity.EdukitPostActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EduKitFragment extends Fragment {

    public EduKitFragment() {
        // Required empty public constructor
    }

    private FragmentEduKitBinding mBinding;

    public static EduKitFragment newInstance() {
        Bundle args = new Bundle();
        EduKitFragment fragment = new EduKitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edu_kit, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);
    }

    public void onItemClick(int positoin) {
        String id = "";
        String name = "";
        switch (positoin) {
            case 1:
                name = "Schedule";
                break;
            case 2:
                name = "Campus Calender";
                break;
            case 3:
                name = "Time Table";
                break;
            case 4:
                name = "Result";
                break;
            case 5:
                name = "Document";
                break;
            case 6:
                name = "Syllabus";
                break;
            case 7:
                name = "Study Material";
                break;
            case 8:
                name = "Events";
                break;
            case 9:
                name = "Question Paper";
                break;
            case 10:
                name = "Others";
                break;
        }
        Intent intent = new Intent(getActivity(), EdukitPostActivity.class);
        intent.putExtra("edukit_id", "" + positoin);
        intent.putExtra("Name", name);
        startActivity(intent);


    }

}