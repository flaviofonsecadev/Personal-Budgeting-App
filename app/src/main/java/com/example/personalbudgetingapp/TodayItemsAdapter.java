package com.example.personalbudgetingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TodayItemsAdapter extends RecyclerView.Adapter<TodayItemsAdapter.ViewHolder>{
    private Context mContext;
    private List<Data> myDataList;
    private String post_key = "";
    private String item = "";
    private String note = "";
    private int amount = 0;

    public TodayItemsAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout, parent, false);
        return new TodayItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Data data = myDataList.get(position);

        holder.item.setText("Tipo: "+ data.getItem());
        holder.amount.setText("Valor: R$"+ data.getAmount());
        holder.date.setText("Data: "+ data.getDate());
        holder.notes.setText("Descrição: "+ data.getNotes());

        switch (data.getItem()){
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

        holder.itemView.setOnClickListener(view -> {
            post_key = data.getId();
            item = data.getItem();
            amount = data.getAmount();
            note = data.getNotes();
            updateData();
        });

    }

    private void updateData() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.update_layout, null);
        myDialog.setView(mView);

        final AlertDialog dialog = myDialog.create();

        final TextView mItem = mView.findViewById(R.id.itemName);
        final EditText mAmount = mView.findViewById(R.id.amount);
        final EditText mNotes = mView.findViewById(R.id.note);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        mNotes.setText(note);
        mNotes.setSelection(note.length());

        Button delBut = mView.findViewById(R.id.btnDelete);
        Button btnUpdate = mView.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(view -> {
            amount = Integer.parseInt(mAmount.getText().toString());
            note = mNotes.getText().toString();

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
                    weeks.getWeeks(), months.getMonths(), note);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.child(post_key).setValue(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(mContext, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            });

            dialog.dismiss();

        });

        delBut.setOnClickListener(view -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.child(post_key).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(mContext, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            });

            dialog.dismiss();

        });

        dialog.show();

    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item, amount, date, notes;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

}
