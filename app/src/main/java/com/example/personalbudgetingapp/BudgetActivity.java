package com.example.personalbudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalbudgetingapp.databinding.ActivityBudgetBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BudgetActivity extends AppCompatActivity {
    ActivityBudgetBinding binding;
    private DatabaseReference budgetRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    private String post_key = "";
    private String item = "";
    private int amount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("budget").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        binding.recyclerview.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;

                for (DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    totalAmount += data.getAmount();
                    String sTotal = String.valueOf("Valor Mensal: R$" + totalAmount);
                    binding.totalBudgetAmountTextView.setText(sTotal);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.fab.setOnClickListener(view -> {
            addItem();
        });

    }

    private void addItem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myView.findViewById(R.id.itensSpinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(view -> {
            String budgetAmount = amount.getText().toString();
            String budgetItem = itemSpinner.getSelectedItem().toString();

            if (TextUtils.isEmpty(budgetAmount)){
                amount.setError("Insira o valor");
                return;
            }

            if (budgetItem.equals("Escolher tipo")){
                Toast.makeText(BudgetActivity.this, "Escolha o tipo de depesa", Toast.LENGTH_SHORT).show();
            }

            else {
                loader.setMessage("Registrando...");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                String id = budgetRef.push().getKey();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epoch, now);
                Months months = Months.monthsBetween(epoch, now);

                String intemNday = budgetItem + date;
                String intemNweek = budgetItem + weeks.getWeeks();
                String intemNmonth = budgetItem + months.getMonths();

                Data data = new Data(budgetItem, date, id, intemNday, intemNweek, intemNmonth, Integer.parseInt(budgetAmount),
                        weeks.getWeeks(), months.getMonths(), null);
                budgetRef.child(id).setValue(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(BudgetActivity.this, "Despesa adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }

                    loader.dismiss();
                });
            }
            dialog.dismiss();

        });

        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(budgetRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {
                holder.setItemAmount("Valor: R$"+ model.getAmount());
                holder.setDate("Data: "+model.getDate());
                holder.setItemName("Tipo: "+model.getItem());

                holder.notes.setVisibility(View.GONE);

                switch (model.getItem()){
                    case "Transporte":
                        holder.imageView.setImageResource(R.drawable.ic_transport);
                        break;
                    case "Alimentação":
                        holder.imageView.setImageResource(R.drawable.ic_food);
                        break;
                    case "Casa":
                        holder.imageView.setImageResource(R.drawable.ic_house);
                        break;
                    case "Entretenimento":
                        holder.imageView.setImageResource(R.drawable.ic_entertainment);
                        break;
                    case "Educação":
                        holder.imageView.setImageResource(R.drawable.ic_education);
                        break;
                    case "Caridade":
                        holder.imageView.setImageResource(R.drawable.ic_consultancy);
                        break;
                    case "Vestuário":
                        holder.imageView.setImageResource(R.drawable.ic_shirt);
                        break;
                    case "Saúde":
                        holder.imageView.setImageResource(R.drawable.ic_health);
                        break;
                    case "Pessoal":
                        holder.imageView.setImageResource(R.drawable.ic_personalcare);
                        break;
                    case "Outros":
                        holder.imageView.setImageResource(R.drawable.ic_other);
                        break;

                }

                holder.mView.setOnClickListener(view -> {
                    post_key = getRef(position).getKey();
                    item = model.getItem();
                    amount = model.getAmount();
                    updateData();
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        binding.recyclerview.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageView imageView;
        public TextView notes, date;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            notes = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }

        public void setItemName(String itemName){
            TextView item = mView.findViewById(R.id.item);
            item.setText(itemName);
        }

        public void setItemAmount(String itemAmount){
            TextView item = mView.findViewById(R.id.amount);
            item.setText(itemAmount);
        }

        public void setDate(String itemDate){
            TextView date = mView.findViewById(R.id.date);
            date.setText(itemDate);
        }

    }

    private void updateData(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_layout, null);
        myDialog.setView(mView);

        final AlertDialog dialog = myDialog.create();

        final TextView mItem = mView.findViewById(R.id.itemName);
        final EditText mAmount = mView.findViewById(R.id.amount);
        final EditText mNotes = mView.findViewById(R.id.note);

        mNotes.setVisibility(View.GONE);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        Button delBut = mView.findViewById(R.id.btnDelete);
        Button btnUpdate = mView.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(view -> {
            amount = Integer.parseInt(mAmount.getText().toString());

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance();
            String date = dateFormat.format(calendar.getTime());

            MutableDateTime epoch = new MutableDateTime();
            epoch.setDate(0);
            DateTime now = new DateTime();
            Weeks weeks = Weeks.weeksBetween(epoch, now);
            Months months = Months.monthsBetween(epoch, now);

            String intemNday = item + date;
            String intemNweek = item + weeks.getWeeks();
            String intemNmonth = item + months.getMonths();

            Data data = new Data(item, date, post_key, intemNday, intemNweek, intemNmonth, amount,
                    weeks.getWeeks(), months.getMonths(), null);
            budgetRef.child(post_key).setValue(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(BudgetActivity.this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            });

            dialog.dismiss();

        });

        delBut.setOnClickListener(view -> {
            budgetRef.child(post_key).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(BudgetActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BudgetActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            });

            dialog.dismiss();

        });

        dialog.show();

    }

}