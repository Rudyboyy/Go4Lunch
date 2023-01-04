package com.rudy.go4lunch.ui;

import static com.rudy.go4lunch.ui.restaurant.RestaurantsFragment.RESTAURANT_INFO;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ActivityMainBinding;
import com.rudy.go4lunch.databinding.NavHeaderBinding;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.RestaurantDto;
import com.rudy.go4lunch.service.ProcessRestaurantDto;
import com.rudy.go4lunch.ui.dialog.PermissionDialogFragment;
import com.rudy.go4lunch.ui.restaurant.DetailRestaurantActivity;
import com.rudy.go4lunch.ui.workmates.SuggestionAdapter;
import com.rudy.go4lunch.viewmodel.MainViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ProcessRestaurantDto {

    ActivityMainBinding binding;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private UserManager userManager = UserManager.getInstance();
    private MainViewModel mViewModel;
    private FusedLocationProviderClient locationClient;
    private FrameLayout container;;
    private SuggestionAdapter adapter;

    private final static int REQUEST_CODE_UPDATE_LOCATION = 541;
    private final static String DIALOG = "dialog";
    private final String search = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        requestLocationPermission();
        setUpLogin();
        initUi();
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    },
                    REQUEST_CODE_UPDATE_LOCATION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_UPDATE_LOCATION) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionDialogFragment permissionDialogFragment = new PermissionDialogFragment();
                permissionDialogFragment.show(getSupportFragmentManager(), DIALOG);
            }
        }
    }

    private void initUi() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initToolbar();
        initBottomNavigationView();
        initDrawerLayout();
        initMenuNavigationView();
        updateUIWithUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpLogin();
        getUserData();
    }

    private void initBottomNavigationView() {
        BottomNavigationView navView = binding.navView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(
                R.id.navigation_map, R.id.navigation_restaurant, R.id.navigation_workmate)
                .setOpenableLayout(binding.mainDrawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private final ActivityResultLauncher<Intent> searchLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent returnedIntent = result.getData();
                    //todo get string extra
                    if (returnedIntent != null) {
//                        String selectedRestaurantID = returnedIntent.getStringExtra(KEY_SELECTED_RESTAURANT_ID);
//                        String selectedRestaurantName = returnedIntent.getStringExtra(KEY_SELECTED_RESTAURANT_NAME);
                    }
                    }
            }
    );

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        container = binding.recyclerViewContainer;

        return super.onCreateOptionsMenu(menu);
//        searchView.setBackgroundColor(getResources().getColor(R.color.black));
//        searchView.setGravity(Gravity.START);
//        searchView.setIconifiedByDefault(false);
//        searchView.setQueryHint("search");
//        searchView.getOverlay();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Effectuez une action lorsque l'utilisateur soumet sa recherche
//                searchItem.collapseActionView();
//                return false;
//            }
//
//            //todo ajouter la SearchView a la toolbar et l'enlever du menu (ou pas)
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (newText.isEmpty()) {
//                    container.removeAllViews();
//                } else {
//                    if (container.getChildCount() == 0) {
//                        initPredictionRecyclerView();
//                    }
//                }
//                return true;
//            }
//        });
//        return true;
    }

    private void initPredictionRecyclerView() {
        adapter = new SuggestionAdapter();
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        container.addView(recyclerView);
    }

    private void setUpLogin() {
        if (!userManager.isCurrentUserLogged()) {
            WelcomeScreen.navigate(MainActivity.this);
            finish();
        }
    }

    private void initToolbar() {
        this.toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
    }

    private void initDrawerLayout() {
        this.drawerLayout = binding.mainDrawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initMenuNavigationView() {
        this.navigationView = binding.navigationView;
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_lunch:
                showLunch();
                break;
            case R.id.activity_main_drawer_settings:
                showSettings();
                break;
            case R.id.activity_main_drawer_logout:
                logOut();
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("MissingPermission")
    private void showLunch() {
        userManager.getUserData().addOnSuccessListener(user -> {
            if (user.getBookedRestaurantPlaceId() != null) {
                locationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                mViewModel.getRestaurantLocation(this, location);
                            }
                        });
            } else {
                Snackbar.make(binding.getRoot(), "You didn't choose a restaurant yet!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void showSettings() {
        Intent settingsActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
        this.startActivity(settingsActivityIntent);
    }

    private void logOut() {
        userManager.signOut(this).addOnSuccessListener(Avoid -> {
            WelcomeScreen.navigate(this);
            finish();
        });
    }

    private void updateUIWithUserData() {
        if (userManager.isCurrentUserLogged()) {
            FirebaseUser user = userManager.getCurrentUser();

            if (user.getPhotoUrl() != null) {
                setProfilePicture(user.getPhotoUrl());
            }
            getUserData();
            setTextUserData(user);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl) {
        View headerView = binding.navigationView.getHeaderView(0);
        NavHeaderBinding headerBinding = NavHeaderBinding.bind(headerView);
        ImageView profilePicture = headerBinding.avatar;
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(profilePicture);
    }

    private void setTextUserData(FirebaseUser user) {
        View headerView = binding.navigationView.getHeaderView(0);
        NavHeaderBinding headerBinding = NavHeaderBinding.bind(headerView);
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();
        headerBinding.name.setText(username);
        headerBinding.email.setText(email);
    }

    private void getUserData() {
        View headerView = binding.navigationView.getHeaderView(0);
        NavHeaderBinding headerBinding = NavHeaderBinding.bind(headerView);
        userManager.getUserData().addOnSuccessListener(user -> {
            String username = TextUtils.isEmpty(user.getUsername()) ? getString(R.string.info_no_username_found) : user.getUsername();
            headerBinding.name.setText(username);
        });
    }

    @Override
    public void processRestaurantDto(List<RestaurantDto> restaurantDtoList) {
        userManager.getUserData().addOnSuccessListener(user -> {
            for (RestaurantDto restaurantDto : restaurantDtoList) {
                if (Objects.equals(restaurantDto.getPlaceId(), user.getBookedRestaurantPlaceId())) {
                    Intent detailRestaurantActivityIntent = new Intent(this, DetailRestaurantActivity.class);
                    detailRestaurantActivityIntent.putExtra(RESTAURANT_INFO, restaurantDto);
                    detailRestaurantActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(detailRestaurantActivityIntent);
                }
            }
        });
    }
}