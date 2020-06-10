package com.example.aadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private EditText editTextFullName, editTextEmail, editTextPassword;
    private Button buttonRegister, buttonLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFullName = findViewById(R.id.enter_full_name);
        editTextEmail = findViewById(R.id.enter_email);
        editTextPassword = findViewById(R.id.enter_password);
        buttonRegister = findViewById(R.id.register_button);
        buttonLogin = findViewById(R.id.login_button);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);

        /*buttonRegister.setOnClickListener(View.OnClickListener
        ));*/

    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
