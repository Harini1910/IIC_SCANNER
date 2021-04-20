package com.example.iic_scanner.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iic_scanner.Calendar_Adapter;
import com.example.iic_scanner.MyAdapter;
import com.example.iic_scanner.R;
import com.example.iic_scanner.readcalendar;
import com.example.iic_scanner.readdata;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlaceholderFragment extends Fragment {
    DatabaseReference reference;
    RecyclerView recyclerView;
    FirebaseUser user;
    private String childdate;
    ArrayList<readdata> list;
    ArrayList<readcalendar> lists;
    MyAdapter adapter;
    Calendar_Adapter calendar_adapter;
    public CalendarView calendar;
    public long selectedDate;
    public TextView attendance_header;
    private String datesetter;
    private View view;

    public  PlaceholderFragment(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance__result, container, false);
        calendar =view.findViewById(R.id.calenderview);
        attendance_header=view.findViewById(R.id.attendace_header);
        recyclerView=(RecyclerView)view.findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list=new ArrayList<readdata>();
        lists=new ArrayList<readcalendar>();
        user= FirebaseAuth.getInstance().getCurrentUser();
        final DateFormat dateFormats = new SimpleDateFormat("yyyy_MM_dd");
        selectedDate = calendar.getDate();
        childdate=dateFormats.format(selectedDate);

        reference= FirebaseDatabase.getInstance().getReference().child("Attendance").child(childdate).child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    readdata r = dataSnapshot1.getValue(readdata.class);
                    list.add(r);
                }
                attendance_header.setText(list.size() == 0 ? "Attendance Not Taken" : "Attendance");
                adapter = new MyAdapter(getActivity(), list);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy_M_d");
                try {
                    Date selected = dateFormat.parse(i + "_" + (i1 + 1) + "_" + i2);
                    dateFormat = new SimpleDateFormat("yyyy_MM_dd");
                    datesetter = dateFormat.format(selected);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                reference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(datesetter).child(user.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lists.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            readcalendar rc = dataSnapshot1.getValue(readcalendar.class);
                            lists.add(rc);
                        }
                        attendance_header.setText(lists.size() == 0 ? "Attendance Not Taken" : "Attendance" );
                        calendar_adapter = new Calendar_Adapter(getActivity(), lists);
                        recyclerView.setAdapter(calendar_adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

}