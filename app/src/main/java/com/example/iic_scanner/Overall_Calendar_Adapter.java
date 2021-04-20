package com.example.iic_scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Overall_Calendar_Adapter extends RecyclerView.Adapter< Overall_Calendar_Adapter.MyViewHolder> {

    Context context;
    ArrayList<read_overall_calander> read_overall_calander;
    public  Overall_Calendar_Adapter(Context c, ArrayList<read_overall_calander> rm){
        context=c;
        read_overall_calander=rm;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview, parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Date.setText(read_overall_calander.get(position).getDate());
        holder.id.setText(read_overall_calander.get(position).getId());
    }
    @Override
    public int getItemCount() {
        return read_overall_calander.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Date, id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Date=(TextView) itemView.findViewById(R.id.Date);
            id=(TextView) itemView.findViewById(R.id.id);
        }
    }
}
