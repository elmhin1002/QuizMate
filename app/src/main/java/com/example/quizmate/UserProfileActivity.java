package com.example.quizmate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhoneNumber;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        etName = findViewById(R.id.name_edit_text);
        etEmail = findViewById(R.id.email_edit_text);
        etPhoneNumber = findViewById(R.id.phone_edit_text);
        Button btnSave = findViewById(R.id.save_button);
        Button btnLogout = findViewById(R.id.logout_button);
        Button btnDeleteAccount = findViewById(R.id.delete_button);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        loadUserProfile();

        btnSave.setOnClickListener(v -> saveUserProfile());
        btnLogout.setOnClickListener(v -> logoutUser());
        btnDeleteAccount.setOnClickListener(v -> deleteUserAccount());
    }

    private void loadUserProfile() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                if (userProfile != null) {
                    etName.setText(userProfile.getName());
                    etEmail.setText(userProfile.getEmail());
                    etPhoneNumber.setText(userProfile.getPhoneNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        UserProfile userProfile = new UserProfile(name, email, phoneNumber);
        databaseReference.setValue(userProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserProfileActivity.this, "Profile saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        auth.signOut();
        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
        finish();
    }

    private void deleteUserAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    user.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            databaseReference.removeValue();
                            Toast.makeText(UserProfileActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

}

