package com.stufeed.android.view.fragment.connect;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;

public class AcademyFragment extends Fragment {


    public AcademyFragment() {
        // Required empty public constructor
    }

    public static AcademyFragment newInstance() {

        Bundle args = new Bundle();

        AcademyFragment fragment = new AcademyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_academy, container, false);
    }

}
