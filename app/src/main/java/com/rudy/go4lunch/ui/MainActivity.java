package com.rudy.go4lunch.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ActivityMainBinding;
import com.rudy.go4lunch.databinding.NavHeaderBinding;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.ui.dialog.PermissionDialogFragment;
import com.rudy.go4lunch.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private UserManager userManager = UserManager.getInstance();
    private MainViewModel mMainViewModel;

    private final static int REQUEST_CODE_UPDATE_LOCATION = 541;
    private final static String DIALOG = "dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void initViewModel() {
        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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

    private void showLunch() {
        Snackbar.make(binding.getRoot(), "You didn't choose a restaurant yet!", Snackbar.LENGTH_SHORT).show();
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
            setTextUserData(user);
            getUserData();
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
}