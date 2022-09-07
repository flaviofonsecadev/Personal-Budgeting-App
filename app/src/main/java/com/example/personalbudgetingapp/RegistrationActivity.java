package com.example.personalbudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.personalbudgetingapp.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        binding.RegisterQuestion.setOnClickListener(view -> startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)));

        binding.registerBtn.setOnClickListener(view -> {
            String emailString = binding.email.getText().toString();
            String passwordString = binding.password.getText().toString();

            if (TextUtils.isEmpty(emailString)){
                binding.email.setError("Insira um email!");
            }
            else if(TextUtils.isEmpty(passwordString)){
                binding.password.setError("Insira a senha!");
            }

            else {
                progressDialog.setMessage("Cadastrando...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        finish();
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                });
            }

        });

    }
}