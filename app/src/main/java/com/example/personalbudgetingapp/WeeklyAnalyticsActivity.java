package com.example.personalbudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.personalbudgetingapp.databinding.ActivityWeeklyAnalyticsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;

public class WeeklyAnalyticsActivity extends AppCompatActivity {
    private ActivityWeeklyAnalyticsBinding binding;
    private DatabaseReference expensesRef, personalRef;
    private FirebaseAuth mAuth;
    private String onlineUserId = "";

    // TODO - MutableDateTime epoch = new MutableDateTime();
    //        epoch.setDate(0); //Set to Epoch time
    //        DateTime now = new DateTime();
    //        Weeks weeks = Weeks.weeksBetween(epoch, now);



    //TODO -  Locale localeBR = new Locale("pt", "br");
    //        NumberFormat currency = NumberFormat.getCurrencyInstance(localeBR);
    //        ...
    //        binding.totalAmountSpentOn.setText("Valor total do dia: " + currency.format(totalAmount));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeeklyAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Análise de Gastos Semanais");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        getTotalWeekTransportExpenses();
        getTotalWeekFoodExpenses();
        getTotalWeekHouseExpenses();
        getTotalWeekEntertainenmentExpenses();
        getTotalWeekEducationExpenses();
        getTotalWeekCharityExpenses();
        getTotalWeekApparelExpenses();
        getTotalWeekHealthExpenses();
        getTotalWeekPersonalExpenses();
        getTotalWeekOtherExpenses();
        getTotalWeekSpending();

        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                loadGraph();
                setStatusAndImageResource();
            }
        }, 2000);

    }

    private void getTotalWeekTransportExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Transport" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsTransportAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekTrans").setValue(totalAmount);
                } else {
                    binding.linearLayoutTransport.setVisibility(View.GONE);
                    personalRef.child("weekTrans").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalWeekFoodExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);
        String itemNweek = "Food" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsFoodAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekFood").setValue(totalAmount);
                } else {
                    binding.linearLayoutFood.setVisibility(View.GONE);
                    personalRef.child("weekFood").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHouseExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "House" + weeks.getWeeks();
        //String itemNday = "House Expenses" + date;

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsHouseAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekHouse").setValue(totalAmount);
                } else {
                    binding.linearLayoutHouse.setVisibility(View.GONE);
                    personalRef.child("weekHouse").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEntertainenmentExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Entertainment" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsEntertainmentAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekEnt").setValue(totalAmount);
                } else {
                    binding.linearLayoutEntertainment.setVisibility(View.GONE);
                    personalRef.child("weekEnt").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEducationExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Education" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsEducationAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekEdu").setValue(totalAmount);
                } else {
                    binding.linearLayoutEducation.setVisibility(View.GONE);
                    personalRef.child("weekEdu").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekCharityExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Charity" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsCharity.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekCha").setValue(totalAmount);
                } else {
                    binding.linearLayoutCharity.setVisibility(View.GONE);
                    personalRef.child("weekCha").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekApparelExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Apparel" + weeks.getWeeks();
        //String itemNweek = "Apparel and Services" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsApparelAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekApp").setValue(totalAmount);
                } else {
                    binding.linearLayoutApparel.setVisibility(View.GONE);
                    personalRef.child("weekApp").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHealthExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Health" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsHealthAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekHea").setValue(totalAmount);
                } else {
                    binding.linearLayoutHealth.setVisibility(View.GONE);
                    personalRef.child("weekHea").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekPersonalExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Personal" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsPersonalAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekPer").setValue(totalAmount);
                } else {
                    binding.linearLayoutPersonal.setVisibility(View.GONE);
                    personalRef.child("weekPer").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekOtherExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Other" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        binding.analyticsOtherAmount.setText("Gasto: " + totalAmount);
                    }
                    personalRef.child("weekOther").setValue(totalAmount);
                } else {
                    binding.linearLayoutOther.setVisibility(View.GONE);
                    personalRef.child("weekOther").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekSpending() {
        Locale localeBR = new Locale("pt", "br");
        NumberFormat currency = NumberFormat.getCurrencyInstance(localeBR);

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        Query query = expensesRef.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                    }
                    binding.totalAmountSpentOn.setText("Valor total da semana: " + currency.format(totalAmount));
                    binding.monthSpentAmount.setText("Total gasto: " + currency.format(totalAmount));
                } else {
                    //binding.totalAmountSpentOn.setText("Você não tem gastos nesta semana");
                    binding.anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGraph() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    int traTotal;
                    if (snapshot.hasChild("weekTrans")){
                        traTotal = Integer.parseInt(snapshot.child("weekTrans").getValue().toString());
                    }else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("weekFood")){
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("weekHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("weekHouse").getValue().toString());
                    }else {
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("weekEnt")){
                        entTotal = Integer.parseInt(snapshot.child("weekEnt").getValue().toString());
                    }else {
                        entTotal = 0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("weekEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("weekEdu").getValue().toString());
                    }else {
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("weekCha")){
                        chaTotal = Integer.parseInt(snapshot.child("weekCha").getValue().toString());
                    }else {
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("weekApp")){
                        appTotal = Integer.parseInt(snapshot.child("weekApp").getValue().toString());
                    }else {
                        appTotal = 0;
                    }

                    int heaTotal;
                    if (snapshot.hasChild("weekHea")){
                        heaTotal = Integer.parseInt(snapshot.child("weekHea").getValue().toString());
                    }else {
                        heaTotal = 0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("weekPer")){
                        perTotal = Integer.parseInt(snapshot.child("weekPer").getValue().toString());
                    }else {
                        perTotal = 0;
                    }

                    int othTotal;
                    if (snapshot.hasChild("weekOther")){
                        othTotal = Integer.parseInt(snapshot.child("weekOther").getValue().toString());
                    }else {
                        othTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport", traTotal));
                    data.add(new ValueDataEntry("House", houseTotal));
                    data.add(new ValueDataEntry("Food", foodTotal));
                    data.add(new ValueDataEntry("Entertainment", entTotal));
                    data.add(new ValueDataEntry("Education", eduTotal));
                    data.add(new ValueDataEntry("Charity", chaTotal));
                    data.add(new ValueDataEntry("Apparel", appTotal));
                    data.add(new ValueDataEntry("Health", heaTotal));
                    data.add(new ValueDataEntry("Personal", perTotal));
                    data.add(new ValueDataEntry("Other", othTotal));

                    pie.data(data);
                    pie.title("Week Analytics");
                    pie.labels().position("outside");
                    pie.legend().title().enabled(true);
                    pie.legend().title()
                            .text("Itens Spent On")
                            .padding(0d, 0d, 10d, 0d);
                    pie.legend()
                            .position("center-bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);

                    binding.anyChartView.setChart(pie);

                }else {
                    Toast.makeText(WeeklyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setStatusAndImageResource() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    //TODO - ver se precisa mudar os totais para float no loadGraph method ou para int aqui
                    float traTotal;
                    if (snapshot.hasChild("weekTrans")){
                        traTotal = Integer.parseInt(snapshot.child("weekTrans").getValue().toString());
                    }else {
                        traTotal = 0;
                    }

                    float foodTotal;
                    if (snapshot.hasChild("weekFood")){
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    float houseTotal;
                    if (snapshot.hasChild("weekHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("weekHouse").getValue().toString());
                    }else {
                        houseTotal = 0;
                    }

                    float entTotal;
                    if (snapshot.hasChild("weekEnt")){
                        entTotal = Integer.parseInt(snapshot.child("weekEnt").getValue().toString());
                    }else {
                        entTotal = 0;
                    }

                    float eduTotal;
                    if (snapshot.hasChild("weekEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("weekEdu").getValue().toString());
                    }else {
                        eduTotal = 0;
                    }

                    float chaTotal;
                    if (snapshot.hasChild("weekCha")){
                        chaTotal = Integer.parseInt(snapshot.child("weekCha").getValue().toString());
                    }else {
                        chaTotal = 0;
                    }

                    float appTotal;
                    if (snapshot.hasChild("weekApp")){
                        appTotal = Integer.parseInt(snapshot.child("weekApp").getValue().toString());
                    }else {
                        appTotal = 0;
                    }

                    float heaTotal;
                    if (snapshot.hasChild("weekHea")){
                        heaTotal = Integer.parseInt(snapshot.child("weekHea").getValue().toString());
                    }else {
                        heaTotal = 0;
                    }

                    float perTotal;
                    if (snapshot.hasChild("weekPer")){
                        perTotal = Integer.parseInt(snapshot.child("weekPer").getValue().toString());
                    }else {
                        perTotal = 0;
                    }

                    float othTotal;
                    if (snapshot.hasChild("weekOther")){
                        othTotal = Integer.parseInt(snapshot.child("weekOther").getValue().toString());
                    }else {
                        othTotal = 0;
                    }

                    float monthTotalSpentAmount;
                    if (snapshot.hasChild("week")){
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("week").getValue().toString());
                    }else {
                        monthTotalSpentAmount = 0;
                    }


                    //Getting ratios

                    float traRatio;
                    if (snapshot.hasChild("weekTranspRatio")){
                        traRatio = Integer.parseInt(snapshot.child("weekTranspRatio").getValue().toString());
                    }else {
                        traRatio = 0;
                    }

                    float foodRatio;
                    if (snapshot.hasChild("weekFoodRatio")){
                        foodRatio = Integer.parseInt(snapshot.child("weekFoodRatio").getValue().toString());
                    }else {
                        foodRatio = 0;
                    }

                    float houseRatio;
                    if (snapshot.hasChild("weekHouseRatio")){
                        houseRatio = Integer.parseInt(snapshot.child("weekHouseRatio").getValue().toString());
                    }else {
                        houseRatio = 0;
                    }

                    float entRatio;
                    if (snapshot.hasChild("weekEntRatio")){
                        entRatio = Integer.parseInt(snapshot.child("weekEntRatio").getValue().toString());
                    }else {
                        entRatio = 0;
                    }

                    float eduRatio;
                    if (snapshot.hasChild("weekEducRatio")){
                        eduRatio = Integer.parseInt(snapshot.child("weekEducRatio").getValue().toString());
                    }else {
                        eduRatio = 0;
                    }

                    float charRatio;
                    if (snapshot.hasChild("weekCharRatio")){
                        charRatio = Integer.parseInt(snapshot.child("weekCharRatio").getValue().toString());
                    }else {
                        charRatio = 0;
                    }

                    float apparelRatio;
                    if (snapshot.hasChild("weekApparelRatio")){
                        apparelRatio = Integer.parseInt(snapshot.child("weekApparelRatio").getValue().toString());
                    }else {
                        apparelRatio = 0;
                    }

                    float heaRatio;
                    if (snapshot.hasChild("weekHealthRatio")){
                        heaRatio = Integer.parseInt(snapshot.child("weekHealthRatio").getValue().toString());
                    }else {
                        heaRatio = 0;
                    }

                    float perRatio;
                    if (snapshot.hasChild("weekPersRatio")){
                        perRatio = Integer.parseInt(snapshot.child("weekPersRatio").getValue().toString());
                    }else {
                        perRatio = 0;
                    }

                    float othRatio;
                    if (snapshot.hasChild("weekOtherRatio")){
                        othRatio = Integer.parseInt(snapshot.child("weekOtherRatio").getValue().toString());
                    }else {
                        othRatio = 0;
                    }

                    float monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("weeklyBudget")){
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("weeklyBudget").getValue().toString());
                    }else {
                        monthTotalSpentAmountRatio = 0;
                    }


                    float monthPercent = (monthTotalSpentAmount / monthTotalSpentAmountRatio) * 100;
                    binding.monthRatioSpending.setText(monthPercent + "% used of " + monthTotalSpentAmountRatio + ". Status: " );
                    if (monthPercent < 50){
                        binding.monthRatioSpendingImage.setImageResource(R.drawable.green);
                    } else if (monthPercent >= 50 && monthPercent < 100){
                        binding.monthRatioSpendingImage.setImageResource(R.drawable.brown);
                    } else {
                        binding.monthRatioSpendingImage.setImageResource(R.drawable.red);
                    }

                    float transportPercent = (traTotal / traRatio) * 100;
                    binding.progressRatioTransport.setText(transportPercent + "% used of " + traRatio + ". Status: " );
                    if (transportPercent < 50){
                        binding.transportStatus.setImageResource(R.drawable.green);
                    } else if (transportPercent >= 50 && transportPercent < 100){
                        binding.transportStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.transportStatus.setImageResource(R.drawable.red);
                    }

                    float foodPercent = (foodTotal / foodRatio) * 100;
                    binding.progressRatioFood.setText(foodPercent + "% used of " + foodRatio + ". Status: " );
                    if (foodPercent < 50){
                        binding.foodStatus.setImageResource(R.drawable.green);
                    } else if (foodPercent >= 50 && foodPercent < 100){
                        binding.foodStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.foodStatus.setImageResource(R.drawable.red);
                    }

                    float housePercent = (houseTotal / houseRatio) * 100;
                    binding.progressRatioHouse.setText(housePercent + "% used of " + houseRatio + ". Status: " );
                    if (housePercent < 50){
                        binding.houseStatus.setImageResource(R.drawable.green);
                    } else if (housePercent >= 50 && housePercent < 100){
                        binding.houseStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.houseStatus.setImageResource(R.drawable.red);
                    }

                    float entPercent = (entTotal / entRatio) * 100;
                    binding.progressRatioEntertainment.setText(entPercent + "% used of " + entRatio + ". Status: " );
                    if (entPercent < 50){
                        binding.entertainmentStatus.setImageResource(R.drawable.green);
                    } else if (entPercent >= 50 && entPercent < 100){
                        binding.entertainmentStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.entertainmentStatus.setImageResource(R.drawable.red);
                    }

                    float educPercent = (eduTotal / eduRatio) * 100;
                    binding.progressRatioEducation.setText(educPercent + "% used of " + eduRatio + ". Status: " );
                    if (educPercent < 50){
                        binding.educationStatus.setImageResource(R.drawable.green);
                    } else if (educPercent >= 50 && educPercent < 100){
                        binding.educationStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.educationStatus.setImageResource(R.drawable.red);
                    }

                    float chaPercent = (chaTotal / charRatio) * 100;
                    binding.progressRatioCharity.setText(chaPercent + "% used of " + charRatio + ". Status: " );
                    if (chaPercent < 50){
                        binding.charityStatus.setImageResource(R.drawable.green);
                    } else if (chaPercent >= 50 && chaPercent < 100){
                        binding.charityStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.charityStatus.setImageResource(R.drawable.red);
                    }

                    float appPercent = (appTotal / apparelRatio) * 100;
                    binding.progressRatioApparel.setText(appPercent + "% used of " + apparelRatio + ". Status: " );
                    if (appPercent < 50){
                        binding.apparelStatus.setImageResource(R.drawable.green);
                    } else if (appPercent >= 50 && appPercent < 100){
                        binding.apparelStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.apparelStatus.setImageResource(R.drawable.red);
                    }

                    float healPercent = (heaTotal / heaRatio) * 100;
                    binding.progressRatioHealth.setText(healPercent + "% used of " + heaRatio + ". Status: " );
                    if (healPercent < 50){
                        binding.healthStatus.setImageResource(R.drawable.green);
                    } else if (healPercent >= 50 && healPercent < 100){
                        binding.healthStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.healthStatus.setImageResource(R.drawable.red);
                    }

                    float persPercent = (perTotal / perRatio) * 100;
                    binding.progressRatioPersonal.setText(persPercent + "% used of " + perRatio + ". Status: " );
                    if (persPercent < 50){
                        binding.personalStatus.setImageResource(R.drawable.green);
                    } else if (persPercent >= 50 && persPercent < 100){
                        binding.personalStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.personalStatus.setImageResource(R.drawable.red);
                    }

                    float otherPercent = (othTotal / othRatio) * 100;
                    binding.progressRatioOther.setText(otherPercent + "% used of " + othRatio + ". Status: " );
                    if (otherPercent < 50){
                        binding.otherStatus.setImageResource(R.drawable.green);
                    } else if (otherPercent >= 50 && otherPercent < 100){
                        binding.otherStatus.setImageResource(R.drawable.brown);
                    } else {
                        binding.otherStatus.setImageResource(R.drawable.red);
                    }


                }else {
                    Toast.makeText(WeeklyAnalyticsActivity.this, "SetStatusAndImageResource error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalyticsActivity.this, "SetStatusAndImageResource error", Toast.LENGTH_SHORT).show();
            }
        });

    }


}