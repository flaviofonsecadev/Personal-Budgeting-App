package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.personalbudgetingapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.budgetBtnImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BudgetActivity.class)));
        
        binding.todayBtnImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TodaySpendingActivity.class)));

        binding.weekBtnImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, WeekSpendingActivity.class)));


    }
}