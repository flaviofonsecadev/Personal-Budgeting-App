package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.personalbudgetingapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authStateListener = firebaseAuth -> {
            //TODO - não carregar a tela de login se já estiver logado.
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        };

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        binding.loginQuestion.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));

        binding.loginBtn.setOnClickListener(view -> {
            String emailString = binding.email.getText().toString();
            String passwordString = binding.password.getText().toString();

            if (TextUtils.isEmpty(emailString)){
                binding.email.setError("Insira um email!");
            }
            else if(TextUtils.isEmpty(passwordString)){
                binding.password.setError("Insira a senha!");
            }

            else {
                progressDialog.setMessage("Entrando...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                });
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(authStateListener);
    }
}