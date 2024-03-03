package com.example.social5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ThereProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView postRecyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    ImageView image1;

    String uid;
    TextView name1,email1, phone1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);

        name1 = findViewById(R.id.textName);
        email1= findViewById(R.id.textEmail);
        phone1 = findViewById(R.id.textPhone);
        image1 = findViewById(R.id.imageAvatar);


        postRecyclerView = findViewById(R.id.recyclerview_posts);
        firebaseAuth = FirebaseAuth.getInstance();
        postList = new ArrayList<>();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        checkUserStatus();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();


                    name1.setText(name);
                    email1.setText(email);
                    phone1.setText(phone);
                    try{
                        Picasso.get().load(image).into(image1);
                    }
                    catch (Exception e)
                    {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        loadListPosts();
        postRecyclerView.setLayoutManager(layoutManager);

    }



    private void loadListPosts() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = ref.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                postList.clear();
                for (DataSnapshot ds: datasnapshot.getChildren()){
                    ModelPost myPosts = ds.getValue(ModelPost.class);
                    postList.add(myPosts);

                    adapterPosts = new AdapterPosts(ThereProfileActivity.this,postList);

                    postRecyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereProfileActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){

        }
        else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}