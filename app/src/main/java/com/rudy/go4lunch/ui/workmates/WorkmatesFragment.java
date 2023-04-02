package com.rudy.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rudy.go4lunch.R;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.ui.MainActivity;
import com.rudy.go4lunch.ui.chat.ChatActivity;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkmatesFragment extends Fragment implements
        WorkmatesAdapter.WorkmateClickListener {

    private RecyclerView mRecyclerView;
    private final List<User> users = new ArrayList<>();
    private MainViewModel mViewModel;
    private final UserManager userManager = UserManager.getInstance();

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {
        mViewModel.getDataBaseInstanceUser();
        mViewModel.getAllUsers().observe(requireActivity(), users1 -> {
            users.clear();
            users.addAll(users1);
            Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workmates, container, false);
        ((MainActivity) requireContext()).fragmentSelected = R.layout.fragment_workmates;
        initRecyclerView(root);
        initData();
        return root;
    }

    private void initRecyclerView(View root) {
        mRecyclerView = root.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        WorkmatesAdapter mAdapter = new WorkmatesAdapter(users, requireActivity(), this, mViewModel);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onWorkmateClick(String workmateId, String workmateName) {
        String currentUid = userManager.getCurrentUser().getUid();
        ChatActivity.navigate(requireActivity(), currentUid, workmateId, workmateName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) requireActivity();
        if (mainActivity.searchItem != null) {
            mainActivity.searchItem.setVisible(false);
        }
    }
}