package com.example.iic_scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Overall_Calendar_adapters extends RecyclerView.Adapter< Overall_Calendar_adapters.MyViewHolders> {
    Context context;
    ArrayList<read_overall_calendars>read_overall_calendars ;
    public Overall_Calendar_adapters(Context c, ArrayList<read_overall_calendars> rcm){
        context=c;
        read_overall_calendars=rcm;
    }
    @NonNull
    @Override
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolders(LayoutInflater.from(context).inflate(R.layout.cardview, parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolders holder, int position) {
        holder.Date.setText(read_overall_calendars.get(position).getDate());
        holder.id.setText(read_overall_calendars.get(position).getId());
    }
    @Override
    public int getItemCount() {
        return read_overall_calendars.size();
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
