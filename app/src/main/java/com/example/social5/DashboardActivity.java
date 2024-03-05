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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private int selectedTab = 1;

    FragmentManager fragmentManager;


//    BottomNavigationView bottomNavigationView, topNavi;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();


        final LinearLayout newFeedsLayout = findViewById(R.id.homeLayout);
        final LinearLayout searchLayout = findViewById(R.id.searchLayout);
        final LinearLayout notifiLayout = findViewById(R.id.notifiLayout);
        final LinearLayout accountLayout = findViewById(R.id.accountLayout);

        final ImageView newFeedsImage = findViewById(R.id.homeImage);
        final ImageView searchImage = findViewById(R.id.searchImage);
        final ImageView notifiImage = findViewById(R.id.notifiImage);
        final ImageView accountImage = findViewById(R.id.accountImage);

        final TextView newFeedstv = findViewById(R.id.hometv);
        final TextView searchtv = findViewById(R.id.searchtv);
        final TextView notifitv = findViewById(R.id.notifitv);
        final TextView accounttv = findViewById(R.id.accounttv);



        //xét mặc định là newfeeds fragment
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, NewsfeedFragment.class, null)
                .commit();

        newFeedsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kiểm tra nếu tab newfeeds được chọn
                if (selectedTab != 1){

                    //đặt newfeeds fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, NewsfeedFragment.class, null)
                            .commit();

                    searchtv.setVisibility(View.GONE);
                    notifitv.setVisibility(View.GONE);
                    accounttv.setVisibility(View.GONE);

                    searchImage.setImageResource(R.drawable.followuser);
                    notifiImage.setImageResource(R.drawable.notifications_24);
                    accountImage.setImageResource(R.drawable.account_24);

                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notifiLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    accountLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select newfeeds tab
                    newFeedstv.setVisibility(View.VISIBLE);
                    newFeedsImage.setImageResource(R.drawable.home_selected_24);
                    newFeedsLayout.setBackgroundResource(R.drawable.round_back_home);

                    //animation nè
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f,1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    newFeedsLayout.startAnimation(scaleAnimation);

                    //xét tab 1 là tab đầu tiên
                    selectedTab = 1;
                }
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kiểm tra nếu tab search được chọn
                if (selectedTab != 2){

                    //đặt search fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileFragment.class, null)
                            .commit();

                    newFeedstv.setVisibility(View.GONE);
                    notifitv.setVisibility(View.GONE);
                    accounttv.setVisibility(View.GONE);

                    newFeedsImage.setImageResource(R.drawable.home_24);
                    notifiImage.setImageResource(R.drawable.account_24);
                    accountImage.setImageResource(R.drawable.notifications_24);

                    newFeedsLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notifiLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    accountLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //chọn tab search
                    searchtv.setVisibility(View.VISIBLE);
                    searchImage.setImageResource(R.drawable.followuser);
                    searchLayout.setBackgroundResource(R.drawable.round_back_search);

                    //animation nè
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    searchLayout.startAnimation(scaleAnimation);

                    //đặt tab thứ 2 là tab được chọn
                    selectedTab = 2;
                }
            }
        });

        notifiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kiểm tra nếu tab notifi được chọn
                if (selectedTab != 3){

                    //đặt notifi fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, UsersFragment.class, null)
                            .commit();

                    newFeedstv.setVisibility(View.GONE);
                    searchtv.setVisibility(View.GONE);
                    accounttv.setVisibility(View.GONE);

                    newFeedsImage.setImageResource(R.drawable.home_24);
                    searchImage.setImageResource(R.drawable.followuser);
                    accountImage.setImageResource(R.drawable.notifications_24);

                    newFeedsLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    accountLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //chọn tab notifi
                    notifitv.setVisibility(View.VISIBLE);
                    notifiImage.setImageResource(R.drawable.account_selected_24);
                    notifiLayout.setBackgroundResource(R.drawable.round_back_notifi);

                    //animation nè
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f,1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    notifiLayout.startAnimation(scaleAnimation);

                    //đặt tab thứ 3 là tab được chọn
                    selectedTab = 3;
                }
            }
        });

        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kiểm tra nếu tab search được chọn
                if (selectedTab != 4){

                    //đặt account fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, NotificationFragment.class, null)
                            .commit();

                    newFeedstv.setVisibility(View.GONE);
                    searchtv.setVisibility(View.GONE);
                    notifitv.setVisibility(View.GONE);

                    newFeedsImage.setImageResource(R.drawable.home_24);
                    searchImage.setImageResource(R.drawable.followuser);
                    notifiImage.setImageResource(R.drawable.account_24);

                    newFeedsLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    searchLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notifiLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //chọn tab account
                    accounttv.setVisibility(View.VISIBLE);
                    accountImage.setImageResource(R.drawable.notifications_selected_24);
                    accountLayout.setBackgroundResource(R.drawable.round_back_account);

                    //animation nè
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f,1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    accountLayout.startAnimation(scaleAnimation);

                    //đặt tab thứ 4 là tab được chọn
                    selectedTab = 4;
                }
            }
        });


//        bottomNavigationView = findViewById(R.id.navigation_home);
//
//        bottomNavigationView.setOnItemSelectedListener(selectedListener);
//
//
//
//
//        FragmentTransaction trangbangtin = getSupportFragmentManager().beginTransaction();
//        NewsfeedFragment bangtinFragment = new NewsfeedFragment();
//        trangbangtin.replace(R.id.frameLayout, bangtinFragment);
//        trangbangtin.commit();
    }



//    private NavigationBarView.OnItemSelectedListener selectedListener =
//            new NavigationBarView.OnItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.newsfeed:
//                            FragmentTransaction trangbangtin = getSupportFragmentManager().beginTransaction();
//                            NewsfeedFragment bangtinFragment = new NewsfeedFragment();
//                            trangbangtin.replace(R.id.frameLayout, bangtinFragment);
//                            trangbangtin.commit();
//                            return true;
//
//                        case R.id.profile:
//                            FragmentTransaction trangprofile = getSupportFragmentManager().beginTransaction();
//                            ProfileFragment profileFragment = new ProfileFragment();
//                            trangprofile.replace(R.id.frameLayout, profileFragment);
//                            trangprofile.commit();
//                            return true;
//
//                        case R.id.users:
//                            FragmentTransaction trangsetting = getSupportFragmentManager().beginTransaction();
//                            UsersFragment settingFragment = new UsersFragment();
//                            trangsetting.replace(R.id.frameLayout, settingFragment);
//                            trangsetting.commit();
//                            return true;
//
//                        case R.id.notifications:
//                            FragmentTransaction trangthongbao = getSupportFragmentManager().beginTransaction();
//                            NotificationFragment notificationFragment = new NotificationFragment();
//                            trangthongbao.replace(R.id.frameLayout, notificationFragment);
//                            trangthongbao.commit();
//                            return true;
//
//                    }
//                    return false;
//                }
//            };

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
