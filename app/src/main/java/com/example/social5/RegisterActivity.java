package com.example.social5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, name, phone;
    Button BtnRegister;
    TextView login;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = findViewById(R.id.textView3);
        email = findViewById(R.id.editTextText);
        password = findViewById(R.id.editTextText2);
        BtnRegister = findViewById(R.id.button3);
        name = findViewById(R.id.editTextName);
        phone = findViewById(R.id.editTextPhone);

        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            }
        });

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();
                String Name = name.getText().toString().trim();
                String Phone = phone.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    email.setError("Nhập không đúng mẫu");
                    password.setFocusable(true);
                }
                else if(password.length() < 6)
                {
                    password.setError("Mật khẩu phải có ít nhất 6 ký tự");
                    password.setFocusable(true);
                }

                else{
                    registerUser(Email,Pass,Name,Phone);
                }
            }
        });
    }

    private void registerUser(String email, String pass, String name1, String phone1) {

        mAuth.createUserWithEmailAndPassword(email , pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();


                            String email = user.getEmail();
                            String uid = user.getUid();
                            String phone2 = phone1;
                            String name2 = name1;

                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("email" , email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name2);
                            hashMap.put("phone", phone2);
                            hashMap.put("image", "https://th.bing.com/th/id/R.4a5415ba9a7191b0481f05c688fa8061?rik=P0hZpkRFkwOZvQ&pid=ImgRaw&r=0");


                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("Users");

                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}