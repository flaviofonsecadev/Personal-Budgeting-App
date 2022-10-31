package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.personalbudgetingapp.databinding.ActivityAccountBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Minha Conta");

        binding.userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        binding.logoutBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(AccountActivity.this)
                    .setTitle("Personal Budgeting App")
                    .setMessage("Quer deslogar da conta?")
                    .setCancelable(true)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                            finish();

                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });

    }
}