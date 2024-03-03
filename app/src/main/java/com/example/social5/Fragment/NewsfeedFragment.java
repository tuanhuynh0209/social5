package com.example.social5.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social5.AdapterPosts;
import com.example.social5.AddPostActivity;
import com.example.social5.MainActivity;
import com.example.social5.ModelPost;
import com.example.social5.ModelUser;
import com.example.social5.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsfeedFragment extends Fragment {


    private SearchView searchView;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;

    String uid;

    Button btncreate;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        searchView = view.findViewById(R.id.searchpost);
        btncreate = view.findViewById(R.id.button51);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return false;
            }
        });
        recyclerView = view.findViewById(R.id.post);

        postList = new ArrayList<>();
        checkUserStatus();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);




        loadPosts();
        return view;
    }

    private void loadFollowPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Follow");
       ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            postList.clear();
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    String hisUid = "" + ds.getRef().getKey();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    Query query = ref.orderByChild("uid").equalTo(hisUid);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                ModelPost modelPost = ds.getValue(ModelPost.class);
                                postList.add(modelPost);
                            }
                            adapterPosts = new AdapterPosts(getActivity(), postList);
                            recyclerView.setAdapter(adapterPosts);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

       });
    }
    private void getPost(String hisUid) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = ref.orderByChild("uid").equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            ModelPost modelPost = ds.getValue(ModelPost.class);
                            postList.add(modelPost);
                        }
                        adapterPosts = new AdapterPosts(getActivity(), postList);
                        recyclerView.setAdapter(adapterPosts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void filterList(String newText) {
        List<ModelPost> filteredList = new ArrayList<>();
        for(ModelPost item : postList){
            if((item.getpDescr().toLowerCase().contains(newText.toLowerCase())) || (item.getpTitle().toLowerCase().contains(newText.toLowerCase())) ){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getActivity(), "Không tìm thấy", Toast.LENGTH_SHORT).show();
        }
        else {
            adapterPosts.setFilteredList(filteredList);
        }
    }

    private void loadPosts() {


        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Follow");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                postList.clear();
                for (DataSnapshot ds: datasnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);
                    adapterPosts = new AdapterPosts(getActivity(), postList);
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){

            uid = user.getUid();
        }
        else{
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

}
