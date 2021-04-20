package com.example.iic_scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Calendar_Adapter extends RecyclerView.Adapter<Calendar_Adapter.MyViewHolders> {
    Context context;
    ArrayList<readcalendar> readcalendar;
    public Calendar_Adapter(Context c, ArrayList<readcalendar> rc){
        context=c;
        readcalendar=rc;
    }
    @NonNull
    @Override
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolders(LayoutInflater.from(context).inflate(R.layout.cardview, parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolders holder, int position) {
        holder.Date.setText(readcalendar.get(position).getDate());
        holder.id.setText(readcalendar.get(position).getId());
    }
    @Override
    public int getItemCount() {
        return readcalendar.size();
    }
    class MyViewHolders extends  RecyclerView.ViewHolder{
        TextView Date, id;
        public MyViewHolders(@NonNull View itemView) {
            super(itemView);
            Date=(TextView) itemView.findViewById(R.id.Date);
            id=(TextView) itemView.findViewById(R.id.id);
        }
    }
}
