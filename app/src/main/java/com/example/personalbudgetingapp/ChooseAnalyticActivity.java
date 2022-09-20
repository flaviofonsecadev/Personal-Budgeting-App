package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.personalbudgetingapp.databinding.ActivityChooseAnalyticBinding;

public class ChooseAnalyticActivity extends AppCompatActivity {
    ActivityChooseAnalyticBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseAnalyticBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.todayCardView.setOnClickListener(view -> startActivity(new Intent(ChooseAnalyticActivity.this, DailyAnalyticsActivity.class)));
    }
}