package com.example.aadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText editTextEmailLogin, editTextPasswordLogin;
    private Button buttonLogin, buttonRegister;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmailLogin = findViewById(R.id.enter_email_login);
        editTextPasswordLogin = findViewById(R.id.enter_password_login);
        buttonLogin = findViewById(R.id.login_button);
        buttonRegister = findViewById(R.id.create_account_button);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
    }

    public void onShiftToCreateAccountClick(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }

    public void onLoginClick(View view) {
        String email = editTextEmailLogin.getText().toString().trim();
        String password = editTextPasswordLogin.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmailLogin.setError("Email cannot be empty.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPasswordLogin.setError("Password cannot be empty.");
            return;
        }
        if (password.length() < 10) {
            editTextPasswordLogin.setError("Password must be at least 10 characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // authenticate the user

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
