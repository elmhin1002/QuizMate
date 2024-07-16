package com.example.quizmate.auth;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmate.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



    public class ChangePasswordActivity extends AppCompatActivity {

        private EditText edtCurrentPassword, edtNewPassword, edtNewPasswordConfirm;
        private Button btnUpdatePassword;
        private ProgressBar progressBar;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_password);

            edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
            edtNewPassword = findViewById(R.id.edtNewPassword);
            edtNewPasswordConfirm = findViewById(R.id.edtNewPasswordConfirm);
            btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
            progressBar = findViewById(R.id.progressBarChangePassword);
            ImageButton btnBackChangePassword = findViewById(R.id.btnBackChangePassword);

            mAuth = FirebaseAuth.getInstance();

            btnBackChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePassword();
                }
            });
            EditText edtNewPasswordConfirm = findViewById(R.id.edtNewPasswordConfirm);
            edtNewPasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            });
        }

        private void updatePassword() {
            String currentPassword = edtCurrentPassword.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmPassword = edtNewPasswordConfirm.getText().toString().trim();

            if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null && user.getEmail() != null) {
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                            progressBar.setVisibility(View.GONE);
                            if (task1.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChangePasswordActivity.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
