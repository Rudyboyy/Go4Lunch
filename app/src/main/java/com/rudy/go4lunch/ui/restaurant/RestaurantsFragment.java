package com.rudy.go4lunch.ui.restaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rudy.go4lunch.R;
import com.rudy.go4lunch.model.Restaurant;
import com.rudy.go4lunch.model.Workmate;
import com.rudy.go4lunch.ui.workmates.WorkmatesAdapter;

import java.util.ArrayList;

public class RestaurantsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Restaurant> mRestaurants;

    private void initData() {
        mRestaurants = new ArrayList<>(Restaurant.getRestaurants());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurants, container, false);
        initData();
        initRecyclerView(root);
        return root;
    }

    private void initRecyclerView(View root) {
        mRecyclerView = root.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        RestaurantsAdapter mAdapter = new RestaurantsAdapter(mRestaurants);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
    }
}