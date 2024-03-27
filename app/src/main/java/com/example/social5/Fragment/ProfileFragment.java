package com.example.social5.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social5.AdapterPosts;
import com.example.social5.AddPostActivity;
import com.example.social5.EditProfileActivity;
import com.example.social5.MainActivity;
import com.example.social5.ModelPost;
import com.example.social5.R;
import com.example.social5.Singleton.ColorManager;
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


public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    RecyclerView postRecyclerView;

    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    String uid;


    DatabaseReference databaseReference;

    ImageView avatarIv;
    TextView nameTV, emailTV,phoneTV,tvFollowing,following,tvFollowerCount,userfollow,tvPostCount,tvposted;

    Button logoutbtn, btnEdit;
    ImageButton btncreate, imageButton2;

    LinearLayout linearlayout12;
    private DatabaseReference userRef;

    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> launcher;
    Uri image_uri;

    @Nullable
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        linearlayout12 = view.findViewById(R.id.linearlayout12);

        tvFollowing = view.findViewById(R.id.tvFollowing);
        following = view.findViewById(R.id.following);

        tvFollowerCount = view.findViewById(R.id.tvFollowerCount);
        userfollow = view.findViewById(R.id.userfollow);
        tvPostCount = view.findViewById(R.id.tvPostCount);

        tvposted = view.findViewById(R.id.tvposted);
        imageButton2 = view.findViewById(R.id.imageButton2);

        avatarIv = view.findViewById(R.id.imageAvatar);
        nameTV = view.findViewById(R.id.textName);
        emailTV = view.findViewById(R.id.textEmail);
        phoneTV = view.findViewById(R.id.textPhone);
        logoutbtn = view.findViewById(R.id.btnLogout);
        btnEdit = view.findViewById(R.id.btnEdit);




        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btncreate = view.findViewById(R.id.button51);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser();
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị menu khi người dùng nhấn vào ImageButton
                showPopupMenu(v);
            }
        });

        postRecyclerView = view.findViewById(R.id.recyclerview_posts);
        postList = new ArrayList<>();


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();

                    nameTV.setText(name);
                    emailTV.setText(email);
                    phoneTV.setText(phone);
                    Picasso.get().load(image).into(avatarIv);
                    try {
                        Picasso.get().load(image).into(avatarIv);
                    }
                    catch (Exception e)
                    {
                        Picasso.get().load(R.drawable.ic_action_name).into(avatarIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        checkUserStatus();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        postRecyclerView.setLayoutManager(layoutManager);
        loadMyPosts();

        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        DatabaseReference followRef = FirebaseDatabase.getInstance().getReference().child("Follow").child(userId);
        followRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long followingCount = dataSnapshot.getChildrenCount();
                tvFollowing.setText(String.valueOf(followingCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int postCount = 0;
                // tạo vòng for lặp qua các dòng dữ liệu để lụm những thg có userId là ng dùng hiện tại
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("uid").getValue(String.class).equals(userId)) {
                        postCount++;
                    }
                }
                tvPostCount.setText(String.valueOf(postCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Lỗi: " + databaseError.getMessage());
            }
        });

        return view;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFragmentBackgroundColor(ColorManager.getInstance().getBackgroundColor());
    }


    private void signOutUser() {

        Intent mainActivity = new Intent(getActivity(),MainActivity.class );
        startActivity(mainActivity);

    }

    private void loadMyPosts() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = ref.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost myPosts = ds.getValue(ModelPost.class);
                    postList.add(myPosts);

                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    postRecyclerView.setAdapter(adapterPosts);
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

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Xử lý sự kiện khi người dùng chọn một mục từ menu
                switch (item.getItemId()) {
                    case R.id.color_white:
                        ColorManager.getInstance().setBackgroundColor(Color.WHITE);
                        setFragmentBackgroundColor(ColorManager.getInstance().getBackgroundColor());
//                        postList.setTabIconTintResource(isColorDark(ColorManager.getInstance()
//                                .getBackgroundColor()) ? R.color.white : R.color.text);
                        break;
                    case R.id.color_yellow:
                        ColorManager.getInstance().setBackgroundColor(Color.YELLOW);
                        setFragmentBackgroundColor(ColorManager.getInstance().getBackgroundColor());
//                        tlPostType.setTabIconTintResource(isColorDark(ColorManager.getInstance()
//                                .getBackgroundColor()) ? R.color.white : R.color.text);
                        break;
                    case R.id.color_red:
                        ColorManager.getInstance().setBackgroundColor(Color.RED);
                        setFragmentBackgroundColor(ColorManager.getInstance().getBackgroundColor());
//                        tlPostType.setTabIconTintResource(isColorDark(ColorManager.getInstance()
//                                .getBackgroundColor()) ? R.color.white : R.color.text);
                        break;

                    case R.id.color_blue:
                        ColorManager.getInstance().setBackgroundColor(Color.BLUE);
                        setFragmentBackgroundColor(ColorManager.getInstance().getBackgroundColor());
//                        tlPostType.setTabIconTintResource(isColorDark(ColorManager.getInstance()
//                                .getBackgroundColor()) ? R.color.white : R.color.text);
                        break;
                    default:
                        return false;
                }
                String postCount = tvPostCount.getText().toString();
                int j = Integer.parseInt(postCount);
                for(int i = 0 ; i < j ; i++) {
                    adapterPosts.notifyItemChanged(i);
                }




                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_color);
        popupMenu.show();
    }

    public void setFragmentBackgroundColor(int color) {
        getView().setBackgroundColor(color);
        linearlayout12.setBackgroundColor(color);


        int textColor = Color.BLACK; // Mặc định là màu đen
        if (isColorDark(color)) {
            textColor = Color.WHITE; // Chuyển sang màu trắng nếu nền là màu tối
        }
        nameTV.setTextColor(textColor);
        emailTV.setTextColor(textColor);
        phoneTV.setTextColor(textColor);
        tvFollowing.setTextColor(textColor);
        following.setTextColor(textColor);
        tvFollowerCount.setTextColor(textColor);
        userfollow.setTextColor(textColor);
        tvPostCount.setTextColor(textColor);
        tvposted.setTextColor(textColor);

//        tvborder1.setBackgroundColor(textColor);
//        tvborder2.setBackgroundColor(textColor);

    }
    private boolean isColorDark(int color) {
        // Lấy giá trị trung bình của các thành phần màu (R, G, B) để xác định màu sáng hay màu tối
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5; // Trả về true nếu màu là màu tối, ngược lại trả về false
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Không làm gì ở đây để tránh hiển thị menu mặc định
        super.onCreateOptionsMenu(menu, inflater);
    }
}

