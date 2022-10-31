package com.example.personalbudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.personalbudgetingapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private DatabaseReference expensesRef, budgetRef, personalRef;
    private FirebaseAuth mAuth;
    private String onlineUserId = "";

    private int totalAmountMonth = 0;
    private int totalAmountBudget = 0;
    private int totalAmountBudgetB = 0;
    private int totalAmountBudgetC = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Personal Budgeting App");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);
        budgetRef = FirebaseDatabase.getInstance().getReference("budget").child(onlineUserId);

        binding.budgetBtnImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BudgetActivity.class)));
        binding.todayCardView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TodaySpendingActivity.class)));
        binding.weekBtnImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, WeekSpendingActivity.class)));
        binding.monthBtnImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MonthSpendingActivity.class)));
        binding.analyticsImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ChooseAnalyticActivity.class)));
        binding.historyCardView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmountBudgetB += pTotal;
                    }
                    totalAmountBudgetC = totalAmountBudgetB;
                    personalRef.child("budget").setValue(totalAmountBudgetC);
                }else {
                    personalRef.child("budget").setValue(0);
                    Toast.makeText(MainActivity.this, "Insira um valor", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getBudgetAmount();
        getTodaySpentAmount();
        getWeekSpentAmount();
        getMonthSpentAmount();
        getSavings();

    }

    private void getSavings() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int budget;
                    if (snapshot.hasChild("budget")){
                        budget = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    }else {
                        budget = 0;
                    }

                    int monthSpending;
                    if (snapshot.hasChild("month")){
                        monthSpending = Integer.parseInt(Objects.requireNonNull(snapshot.child("month").getValue().toString()));
                    }else {
                        monthSpending = 0;
                    }
                    int savings = budget - monthSpending;
                    binding.savingsTv.setText("R$" + savings);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getWeekSpentAmount() {

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;
                for (DataSnapshot ds: snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    binding.weekTv.setText("R$" + totalAmount);
                }
                personalRef.child("week").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getMonthSpentAmount() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;
                for (DataSnapshot ds: snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    binding.monthTv.setText("R$" + totalAmount);
                }
                personalRef.child("month").setValue(totalAmount);
                totalAmountMonth = totalAmount;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTodaySpentAmount() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;
                for (DataSnapshot ds: snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    binding.todayTv.setText("R$" + totalAmount);
                }
                personalRef.child("today").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getBudgetAmount() {
        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudget += pTotal;
                        binding.budgetTv.setText("R$" + String.valueOf(totalAmountBudget));
                    }
                }else {
                    totalAmountBudget = 0;
                    binding.budgetTv.setText("R$" + String.valueOf(0));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.account){
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}