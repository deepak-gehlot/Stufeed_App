package com.stufeed.android.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.CreateBoardResponse;
import com.stufeed.android.databinding.DialogCreateBoardBinding;
import com.stufeed.android.databinding.FragmentBoardBinding;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.adapter.ViewPagerAdapter;
import com.stufeed.android.view.fragment.board.CreateBoardFragment;
import com.stufeed.android.view.fragment.board.JoinBoardFragment;
import com.stufeed.android.view.fragment.connect.AcademyFragment;
import com.stufeed.android.view.fragment.connect.DepartmentFragment;
import com.stufeed.android.view.fragment.connect.FacultyFragment;
import com.stufeed.android.view.fragment.connect.StudentFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BoardFragment extends Fragment {

    public BoardFragment() {
        // Required empty public constructor
    }

    private FragmentBoardBinding binding;
    private String loginUserId = "";

    public static BoardFragment newInstance() {
        Bundle args = new Bundle();
        BoardFragment fragment = new BoardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        setupViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        loginUserId = Utility.getLoginUserId(getActivity());
    }

    /**
     * Set view pager adapter
     *
     * @param viewPager @ViewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(CreateBoardFragment.newInstance(), getString(R.string.create_board));
        adapter.addFragment(JoinBoardFragment.newInstance(), getString(R.string.join_board));
        viewPager.setAdapter(adapter);
    }


    /**
     * Show create board dialog
     */
    public void showCreateDialog() {
        final DialogCreateBoardBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity())
                , R.layout.dialog_create_board, null, false);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setTitle("Create Board");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel_board:
                        dialog.dismiss();
                        break;
                    case R.id.done_board:

                        String title = dialogBinding.edtNameBoard.getText().toString().trim();
                        String description = dialogBinding.edtDescriptionBoard.getText().toString().trim();
                        boolean isPrivate = dialogBinding.switchPrivate.isChecked();
                        boolean isCircle = dialogBinding.switchCircle.isChecked();

                        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                            dialog.dismiss();
                            createBoard(title, description, isPrivate, isCircle);
                        } else {
                            Utility.showToast(getActivity(), getString(R.string.all_required));
                        }

                        break;
                }
            }
        };

        dialogBinding.cancelBoard.setOnClickListener(onClickListener);
        dialogBinding.doneBoard.setOnClickListener(onClickListener);

        dialog.show();
    }

    /**
     * Create board service
     *
     * @param title       board title
     * @param description board description
     * @param isPrivate   true if board is private
     * @param isCircle    true if allow user to post
     */
    private void createBoard(String title, String description, boolean isPrivate, boolean isCircle) {
        int boardPrivate = 0, boardCircle = 0;
        if (isPrivate) {
            boardPrivate = 1;
        }
        if (isCircle) {
            boardCircle = 1;
        }

        Api api = APIClient.getClient().create(Api.class);
        Call<CreateBoardResponse> responseCall = api.createBoard(loginUserId, title,
                description, boardPrivate, boardCircle);
        ProgressDialog.getInstance().showProgressDialog(getActivity());
        responseCall.enqueue(new Callback<CreateBoardResponse>() {
            @Override
            public void onResponse(Call<CreateBoardResponse> call, Response<CreateBoardResponse> response) {
                handleCreateBoardResponse(response.body());
            }

            @Override
            public void onFailure(Call<CreateBoardResponse> call, Throwable t) {
                handleCreateBoardResponse(null);
            }
        });
    }

    /**
     * Handle create board response
     *
     * @param response {@link CreateBoardResponse}
     */
    private void handleCreateBoardResponse(CreateBoardResponse response) {
        ProgressDialog.getInstance().dismissDialog();
        if (response == null) {
            Utility.showErrorMsg(getActivity());
        } else if (response.getResponseCode().equals(Api.SUCCESS)) {
            Utility.showToast(getActivity(), response.getResponseMessage());
        } else {
            Utility.showToast(getActivity(), response.getResponseMessage());
        }
    }
}