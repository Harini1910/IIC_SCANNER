package com.example.iic_scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<readdata> readdata;
    public MyAdapter(Context c, ArrayList<readdata> r){
        context=c;
        readdata=r;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview, parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         holder.Date.setText(readdata.get(position).getDate());
         holder.id.setText(readdata.get(position).getId());
    }
    @Override
    public int getItemCount() {
        return readdata.size();
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
