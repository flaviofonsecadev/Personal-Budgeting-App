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
import com.example.personalbudgetingapp.databinding.ActivityMonthlyAnalyticsBinding;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;

public class MonthlyAnalyticsActivity extends AppCompatActivity {
    private ActivityMonthlyAnalyticsBinding binding;
    private DatabaseReference expensesRef, personalRef;
    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    Months months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMonthlyAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Análise de Gastos Mensais");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        months = Months.monthsBetween(epoch, now);


        getTotalMonthTransportExpenses();
        getTotalMonthFoodExpenses();
        getTotalMonthEntertainenmentExpenses();
        getTotalMonthHouseExpenses();
        getTotalMonthEducationExpenses();
        getTotalMonthApparelExpenses();
        getTotalMonthCharityExpenses();
        getTotalMonthHealthExpenses();
        getTotalMonthPersonalExpenses();
        getTotalMonthOtherExpenses();
        getTotalMonthSpending();

        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                loadGraph();
                setStatusAndImageResource();
            }
        }, 2000);

    }

    private void getTotalMonthTransportExpenses() {
        /*MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);*/

        String itemNmonth = "Transport" + months.getMonths();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthTrans").setValue(totalAmount);
                } else {
                    binding.linearLayoutTransport.setVisibility(View.GONE);
                    personalRef.child("monthTrans").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalMonthFoodExpenses() {

        String itemNmonth = "Food" + months.getMonths();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthFood").setValue(totalAmount);
                } else {
                    binding.linearLayoutFood.setVisibility(View.GONE);
                    personalRef.child("monthFood").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthHouseExpenses() {

        String itemNmonth = "House" + months.getMonths();
        //String itemNday = "House Expenses" + date;

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthHouse").setValue(totalAmount);
                } else {
                    binding.linearLayoutHouse.setVisibility(View.GONE);
                    personalRef.child("monthHouse").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthEntertainenmentExpenses() {
        String itemNmonth= "Entertainment" + months.getMonths();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthEnt").setValue(totalAmount);
                } else {
                    binding.linearLayoutEntertainment.setVisibility(View.GONE);
                    personalRef.child("monthEnt").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthEducationExpenses() {
        String itemNmonth = "Education" + months.getMonths();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthEdu").setValue(totalAmount);
                } else {
                    binding.linearLayoutEducation.setVisibility(View.GONE);
                    personalRef.child("monthEdu").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthCharityExpenses() {
        String itemNmonth = "Charity" + months.getMonths();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthCha").setValue(totalAmount);
                } else {
                    binding.linearLayoutCharity.setVisibility(View.GONE);
                    personalRef.child("monthCha").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthApparelExpenses() {
        String itemNmonth = "Apparel" + months.getMonths();
        //String itemNweek = "Apparel and Services" + weeks.getWeeks();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthApp").setValue(totalAmount);
                } else {
                    binding.linearLayoutApparel.setVisibility(View.GONE);
                    personalRef.child("monthApp").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthHealthExpenses() {
        String itemNmonth = "Health" + months.getMonths();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthHea").setValue(totalAmount);
                } else {
                    binding.linearLayoutHealth.setVisibility(View.GONE);
                    personalRef.child("monthHea").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthPersonalExpenses() {
        String itemNmonth = "Personal" + months.getMonths();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthPer").setValue(totalAmount);
                } else {
                    binding.linearLayoutPersonal.setVisibility(View.GONE);
                    personalRef.child("monthPer").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthOtherExpenses() {
        String itemNmonth = "Other" + months.getMonths();

        Query query = expensesRef.orderByChild("itemNmonth").equalTo(itemNmonth);
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
                    personalRef.child("monthOther").setValue(totalAmount);
                } else {
                    binding.linearLayoutOther.setVisibility(View.GONE);
                    personalRef.child("monthOther").setValue(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthSpending() {
        Locale localeBR = new Locale("pt", "br");
        NumberFormat currency = NumberFormat.getCurrencyInstance(localeBR);

        Query query = expensesRef.orderByChild("month").equalTo(months.getMonths());
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
                    binding.totalAmountSpentOn.setText("Valor total do mês: " + currency.format(totalAmount));
                    binding.monthSpentAmount.setText("Total gasto: " + currency.format(totalAmount));
                } else {
                    //binding.totalAmountSpentOn.setText("Você não tem gastos nesta semana");
                    binding.anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGraph() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    int traTotal;
                    if (snapshot.hasChild("monthTrans")){
                        traTotal = Integer.parseInt(snapshot.child("monthTrans").getValue().toString());
                    }else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("monthFood")){
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("monthHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("monthHouse").getValue().toString());
                    }else {
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("monthEnt")){
                        entTotal = Integer.parseInt(snapshot.child("monthEnt").getValue().toString());
                    }else {
                        entTotal = 0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("monthEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("monthEdu").getValue().toString());
                    }else {
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("monthCha")){
                        chaTotal = Integer.parseInt(snapshot.child("monthCha").getValue().toString());
                    }else {
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("monthApp")){
                        appTotal = Integer.parseInt(snapshot.child("monthApp").getValue().toString());
                    }else {
                        appTotal = 0;
                    }

                    int heaTotal;
                    if (snapshot.hasChild("monthHea")){
                        heaTotal = Integer.parseInt(snapshot.child("monthHea").getValue().toString());
                    }else {
                        heaTotal = 0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("monthPer")){
                        perTotal = Integer.parseInt(snapshot.child("monthPer").getValue().toString());
                    }else {
                        perTotal = 0;
                    }

                    int othTotal;
                    if (snapshot.hasChild("monthOther")){
                        othTotal = Integer.parseInt(snapshot.child("monthOther").getValue().toString());
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
                    pie.title("month Analytics");
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
                    Toast.makeText(MonthlyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
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
                    if (snapshot.hasChild("monthTrans")){
                        traTotal = Integer.parseInt(snapshot.child("monthTrans").getValue().toString());
                    }else {
                        traTotal = 0;
                    }

                    float foodTotal;
                    if (snapshot.hasChild("monthFood")){
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    float houseTotal;
                    if (snapshot.hasChild("monthHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("monthHouse").getValue().toString());
                    }else {
                        houseTotal = 0;
                    }

                    float entTotal;
                    if (snapshot.hasChild("monthEnt")){
                        entTotal = Integer.parseInt(snapshot.child("monthEnt").getValue().toString());
                    }else {
                        entTotal = 0;
                    }

                    float eduTotal;
                    if (snapshot.hasChild("monthEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("monthEdu").getValue().toString());
                    }else {
                        eduTotal = 0;
                    }

                    float chaTotal;
                    if (snapshot.hasChild("monthCha")){
                        chaTotal = Integer.parseInt(snapshot.child("monthCha").getValue().toString());
                    }else {
                        chaTotal = 0;
                    }

                    float appTotal;
                    if (snapshot.hasChild("monthApp")){
                        appTotal = Integer.parseInt(snapshot.child("monthApp").getValue().toString());
                    }else {
                        appTotal = 0;
                    }

                    float heaTotal;
                    if (snapshot.hasChild("monthHea")){
                        heaTotal = Integer.parseInt(snapshot.child("monthHea").getValue().toString());
                    }else {
                        heaTotal = 0;
                    }

                    float perTotal;
                    if (snapshot.hasChild("monthPer")){
                        perTotal = Integer.parseInt(snapshot.child("monthPer").getValue().toString());
                    }else {
                        perTotal = 0;
                    }

                    float othTotal;
                    if (snapshot.hasChild("monthOther")){
                        othTotal = Integer.parseInt(snapshot.child("monthOther").getValue().toString());
                    }else {
                        othTotal = 0;
                    }

                    int monthTotalSpentAmount;
                    if (snapshot.hasChild("month")){
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("month").getValue().toString());
                    }else {
                        monthTotalSpentAmount = 0;
                    }


                    //Getting ratios

                    float traRatio;
                    if (snapshot.hasChild("monthTranspRatio")){
                        traRatio = Integer.parseInt(snapshot.child("monthTranspRatio").getValue().toString());
                    }else {
                        traRatio = 0;
                    }

                    float foodRatio;
                    if (snapshot.hasChild("monthFoodRatio")){
                        foodRatio = Integer.parseInt(snapshot.child("monthFoodRatio").getValue().toString());
                    }else {
                        foodRatio = 0;
                    }

                    float houseRatio;
                    if (snapshot.hasChild("monthHouseRatio")){
                        houseRatio = Integer.parseInt(snapshot.child("monthHouseRatio").getValue().toString());
                    }else {
                        houseRatio = 0;
                    }

                    float entRatio;
                    if (snapshot.hasChild("monthEntRatio")){
                        entRatio = Integer.parseInt(snapshot.child("monthEntRatio").getValue().toString());
                    }else {
                        entRatio = 0;
                    }

                    float eduRatio;
                    if (snapshot.hasChild("monthEducRatio")){
                        eduRatio = Integer.parseInt(snapshot.child("monthEducRatio").getValue().toString());
                    }else {
                        eduRatio = 0;
                    }

                    float charRatio;
                    if (snapshot.hasChild("monthCharRatio")){
                        charRatio = Integer.parseInt(snapshot.child("monthCharRatio").getValue().toString());
                    }else {
                        charRatio = 0;
                    }

                    float apparelRatio;
                    if (snapshot.hasChild("monthApparelRatio")){
                        apparelRatio = Integer.parseInt(snapshot.child("monthApparelRatio").getValue().toString());
                    }else {
                        apparelRatio = 0;
                    }

                    float heaRatio;
                    if (snapshot.hasChild("monthHealthRatio")){
                        heaRatio = Integer.parseInt(snapshot.child("monthHealthRatio").getValue().toString());
                    }else {
                        heaRatio = 0;
                    }

                    float perRatio;
                    if (snapshot.hasChild("monthPersRatio")){
                        perRatio = Integer.parseInt(snapshot.child("monthPersRatio").getValue().toString());
                    }else {
                        perRatio = 0;
                    }

                    float othRatio;
                    if (snapshot.hasChild("monthOtherRatio")){
                        othRatio = Integer.parseInt(snapshot.child("monthOtherRatio").getValue().toString());
                    }else {
                        othRatio = 0;
                    }

                    int monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("budget")){
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    }else {
                        monthTotalSpentAmountRatio = 0;
                    }


                    int monthPercent = (monthTotalSpentAmount / monthTotalSpentAmountRatio) * 100;
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
                    Toast.makeText(MonthlyAnalyticsActivity.this, "SetStatusAndImageResource error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, "SetStatusAndImageResource error", Toast.LENGTH_SHORT).show();
            }
        });

    }


}