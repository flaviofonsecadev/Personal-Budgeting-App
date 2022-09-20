package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.personalbudgetingapp.databinding.ActivityDailyAnalyticsBinding;

public class DailyAnalyticsActivity extends AppCompatActivity {

    private ActivityDailyAnalyticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailyAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}