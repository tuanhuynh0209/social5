package com.example.social5.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.social5.AdapterNotification;
import com.example.social5.ModelNotification;
import com.example.social5.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {


    RecyclerView notificationsRv;

    private FirebaseAuth firebaseAuth;
    String myuid, cruser;

    private ArrayList<ModelNotification> notificationsList;
    private AdapterNotification adapterNotification;

    public NotificationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationsRv = view.findViewById(R.id.notificationsRv);

        firebaseAuth = FirebaseAuth.getInstance();



        getAllNotifications();

        return view;
    }
    private void getAllNotifications() {


        notificationsList = new ArrayList<>();

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Notifications")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        notificationsList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                            ModelNotification model = ds.getValue(ModelNotification.class);
                            if(!model.getsUid().equals(fUser.getUid()))
                            {
                            notificationsList.add(model);
                            }
                        }
                        adapterNotification = new AdapterNotification(getActivity(), notificationsList);
                        notificationsRv.setAdapter(adapterNotification);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}