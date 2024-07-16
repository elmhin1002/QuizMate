package com.example.quizmate.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtRegisterUsername, edtRegisterEmail, edtRegisterPassword;
    private Button btnRegister;
    private ProgressBar progressBarRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton btnBackRegister = findViewById(R.id.btnBackRegister);
        btnBackRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtRegisterUsername = findViewById(R.id.edtRegisterUsername);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBarRegister = findViewById(R.id.progressBarRegister);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String username = edtRegisterUsername.getText().toString().trim();
        final String email = edtRegisterEmail.getText().toString().trim();
        String password = edtRegisterPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarRegister.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendEmailVerification(user, username, email);
                        }
                    } else {
                        progressBarRegister.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user, String username, String email) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        User newUser = new User(username, email);
                        mDatabase.child(user.getUid()).setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    progressBarRegister.setVisibility(View.GONE);
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công. Vui lòng kiểm tra email để xác thực", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        progressBarRegister.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Không thể gửi email xác thực. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class User {
        public String username;
        public String email;

        public User() {
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }
}
