package com.example.social5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtUsername, edtPhonenumber;
    Button btnSave, btnEditImage;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ImageView avartCurrent;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    Uri imageUri;

    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtUsername = findViewById(R.id.edt_username);
        edtPhonenumber = findViewById(R.id.edtPhonenumber);
        btnSave = findViewById(R.id.btnSave);
        btnEditImage = findViewById(R.id.btnEdit_image);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        avartCurrent = findViewById(R.id.avart_current);


//        if (currentUser != null) {
//            edtUsername.setText(currentUser.getDisplayName());
//            edtPhonenumber.setText("0828907967");
//            Picasso.get().load(currentUser.getPhotoUrl()).into(avartCurrent);
//        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                } else {
                    pickImageFromGallery();
                }
            }
        });
    }

    private void updateUserProfile() {
        String username = edtUsername.getText().toString().trim();
        String phonenumber = edtPhonenumber.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(imageUri)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Thông tin cá nhân đã được cập nhật", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Cập nhật thông tin cá nhân thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void pickImageFromGallery() {
        // Mở gallery intent
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data.getData();
            avartCurrent.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, "Quyền truy cập ảnh từ điện thoại bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }


}


