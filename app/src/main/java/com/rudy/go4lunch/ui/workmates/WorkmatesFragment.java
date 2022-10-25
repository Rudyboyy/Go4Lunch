package com.rudy.go4lunch.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rudy.go4lunch.R;
import com.rudy.go4lunch.model.Workmate;

import java.util.ArrayList;

public class WorkmatesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Workmate> workmates;

    private void initData() {
        workmates = new ArrayList<>(Workmate.getWorkmates());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workmates, container, false);
        initData();
        initRecyclerView(root);
        return root;
    }

    private void initRecyclerView(View root) {
        mRecyclerView = root.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        WorkmatesAdapter mAdapter = new WorkmatesAdapter(workmates);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                layoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
    }
}