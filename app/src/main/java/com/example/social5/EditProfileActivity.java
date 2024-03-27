package com.example.social5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.social5.Fragment.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtUsername, edtPhonenumber;
    Button btnSave, btnEditImage;
    ImageButton btnExit;
    ImageView avartCurrent;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    Uri imageUri;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtUsername = findViewById(R.id.edt_username);
        edtPhonenumber = findViewById(R.id.edtPhonenumber);
        btnSave = findViewById(R.id.btnSave);
        btnEditImage = findViewById(R.id.btnEdit_image);
        avartCurrent = findViewById(R.id.avart_current);

        btnExit = findViewById(R.id.btnExit);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String name = currentUser.getDisplayName();
            String phone = "";

            edtUsername.setText(name);
            edtPhonenumber.setText(phone);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameUpdate = edtUsername.getText().toString().trim();
                    String phoneUpdate = edtPhonenumber.getText().toString().trim();
                    updateData(userId, nameUpdate, phoneUpdate);
                }
            });
        } else {
            Toast.makeText(this, "Không có người dùng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
        }
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileFragment.class);
                startActivity(intent);
            }
        });
    }

    private void updateData(String userID, String nameUpdate, String phoneUpdate) {
        if (nameUpdate.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Vui lòng điền tên", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> user = new HashMap<>();
        user.put("name", nameUpdate);
        if (!phoneUpdate.isEmpty()) {
            user.put("phone", phoneUpdate);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.HashMap;
//
//
//public class EditProfileActivity extends AppCompatActivity {
//
//    EditText edtID, edtUsername, edtPhonenumber;
//    Button btnSave, btnEditImage;
//
//    ImageView avartCurrent;
//    private static final int IMAGE_PICK_CODE = 1000;
//    private static final int PERMISSION_CODE = 1001;
//    Uri imageUri;
//
//    DatabaseReference databaseReference;
//
//    private FirebaseAuth mAuth;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_profile);
//
//        edtUsername = findViewById(R.id.edt_username);
//        edtPhonenumber = findViewById(R.id.edtPhonenumber);
//        btnSave = findViewById(R.id.btnSave);
//        btnEditImage = findViewById(R.id.btnEdit_image);
//        avartCurrent = findViewById(R.id.avart_current);
//
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        String userId = currentUser.getUid();
//
//        String name = edtUsername.getText().toString();
//        String phone = edtPhonenumber.getText().toString();
//
//        updateData(userId,name, phone);
//    }
//
//    private void updateData(String userID, String nameUpdate, String phoneUpdate) {
//        HashMap user = new HashMap();
//        user.put("name", nameUpdate);
//        user.put("phone", phoneUpdate);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        databaseReference.child(userID).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
//            @Override
//            public void onComplete(@NonNull Task task) {
//
//            }
//        });
//
//
//    }
//
//
//}


