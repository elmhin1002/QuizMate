package com.example.quizmate.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmate.R;
import com.example.quizmate.main.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText edtLoginUsernameOrEmail, edtLoginPassword;
    private Button btnLogin;
    private ProgressBar progressBarLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginUsernameOrEmail = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        TextView tvToRegister = findViewById(R.id.tvToRegister);
        tvToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        final String usernameOrEmail = edtLoginUsernameOrEmail.getText().toString().trim();
        String password = edtLoginPassword.getText().toString().trim();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarLogin.setVisibility(View.VISIBLE);

        if (usernameOrEmail.contains("@")) {
            mAuth.signInWithEmailAndPassword(usernameOrEmail, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBarLogin.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Email chưa được xác thực. Vui lòng kiểm tra email của bạn", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBarLogin.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Login with username
            mDatabase.orderByChild("username").equalTo(usernameOrEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    String email = userSnapshot.child("email").getValue(String.class);
                                    if (email != null) {
                                        mAuth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                            if (user != null && user.isEmailVerified()) {
                                                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                progressBarLogin.setVisibility(View.GONE);
                                                                Toast.makeText(LoginActivity.this, "Email chưa được xác thực. Vui lòng kiểm tra email của bạn", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            progressBarLogin.setVisibility(View.GONE);
                                                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            } else {
                                progressBarLogin.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressBarLogin.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
