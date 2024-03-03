package com.example.social5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.example.social5.Fragment.NewsfeedFragment;
import com.example.social5.Fragment.NotificationFragment;
import com.example.social5.Fragment.ProfileFragment;
import com.example.social5.Fragment.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    FragmentManager fragmentManager;


    BottomNavigationView bottomNavigationView, topNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();


        bottomNavigationView = findViewById(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(selectedListener);




        FragmentTransaction trangbangtin = getSupportFragmentManager().beginTransaction();
        NewsfeedFragment bangtinFragment = new NewsfeedFragment();
        trangbangtin.replace(R.id.frameLayout, bangtinFragment);
        trangbangtin.commit();
    }



    private NavigationBarView.OnItemSelectedListener selectedListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.newsfeed:
                            FragmentTransaction trangbangtin = getSupportFragmentManager().beginTransaction();
                            NewsfeedFragment bangtinFragment = new NewsfeedFragment();
                            trangbangtin.replace(R.id.frameLayout, bangtinFragment);
                            trangbangtin.commit();
                            return true;

                        case R.id.profile:
                            FragmentTransaction trangprofile = getSupportFragmentManager().beginTransaction();
                            ProfileFragment profileFragment = new ProfileFragment();
                            trangprofile.replace(R.id.frameLayout, profileFragment);
                            trangprofile.commit();
                            return true;

                        case R.id.users:
                            FragmentTransaction trangsetting = getSupportFragmentManager().beginTransaction();
                            UsersFragment settingFragment = new UsersFragment();
                            trangsetting.replace(R.id.frameLayout, settingFragment);
                            trangsetting.commit();
                            return true;

                        case R.id.notifications:
                            FragmentTransaction trangthongbao = getSupportFragmentManager().beginTransaction();
                            NotificationFragment notificationFragment = new NotificationFragment();
                            trangthongbao.replace(R.id.frameLayout, notificationFragment);
                            trangthongbao.commit();
                            return true;

                    }
                    return false;
                }
            };

    private void checkUserStatus() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {


        } else {
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
        }
    }

    public boolean onOptionItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.item_logout) {
                firebaseAuth.signOut();
                checkUserStatus();
        }
        return super.onOptionsItemSelected(item);


    }


}
