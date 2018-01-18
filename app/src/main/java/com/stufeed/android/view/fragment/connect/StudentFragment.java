package com.stufeed.android.view.fragment.connect;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stufeed.android.R;
import com.stufeed.android.databinding.FragmentStudentBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFragment extends Fragment {


    public StudentFragment() {
        // Required empty public constructor
    }

    private FragmentStudentBinding binding;

    public static StudentFragment newInstance() {

        Bundle args = new Bundle();

        StudentFragment fragment = new StudentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student, container, false)
        return binding.getRoot();
    }

}
