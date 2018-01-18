package com.stufeed.android.view.fragment.connect;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.FragmentFacultyBinding;

public class FacultyFragment extends Fragment {

    public FacultyFragment() {
        // Required empty public constructor
    }

    private FragmentFacultyBinding binding;

    public static FacultyFragment newInstance() {

        Bundle args = new Bundle();

        FacultyFragment fragment = new FacultyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_faculty, container, false);
        return binding.getRoot();
    }
}
