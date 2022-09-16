package com.example.personalbudgetingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MonthSpendingAdapter extends RecyclerView.Adapter<MonthSpendingAdapter.ViewHolder> {
    private Context mContext;
    private List<Data> myDataList;

    public MonthSpendingAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout, parent, false);
        return new MonthSpendingAdapter.ViewHolder(view);
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
