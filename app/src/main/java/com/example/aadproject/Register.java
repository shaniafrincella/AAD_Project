package com.example.aadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    private EditText editTextFullName, editTextEmail, editTextPassword;
    private Button buttonRegister, buttonLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private String userID;

    private SharedPreferences sharedPreferences;
    public static final String EMAIL_KEY = "";
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFullName = findViewById(R.id.enter_full_name);
        editTextEmail = findViewById(R.id.enter_email);
        editTextPassword = findViewById(R.id.enter_password);
        buttonRegister = findViewById(R.id.register_button);
        buttonLogin = findViewById(R.id.login_page_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progress_bar);

        sharedPreferences = getSharedPreferences("SHARED", Context.MODE_PRIVATE);
    }

    public void onShiftToLoginClick(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void onCreateAccountClick(View view) {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String fullName = editTextFullName.getText().toString();


        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email cannot be empty.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password cannot be empty.");
            return;
        }
        if (password.length() < 10) {
            editTextPassword.setError("Password must be at least 10 characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                    // To store user data in Firebase database
                    userID = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
                    final Map<String, Object> user = new HashMap<>();
                    user.put("fullName", fullName);
                    user.put("email", email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user profile is created for " + userID);
                        }
                    });
                    //
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(EMAIL_KEY, editTextEmail.getText().toString());
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), MapActivity.class));
                } else {
                    Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedPreferences.contains(EMAIL_KEY)) {
            editTextEmail.setText(sharedPreferences.getString(EMAIL_KEY, " "));
        }
    }
}
