package com.example.iic_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class scanner extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference ref;
    private String strDate;
    private String childdate;
    FirebaseUser user;
    public String txt;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = this;

        //starting scanner
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            //result contains the value scanned from the qr.
            final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null){
                    Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(scanner.this,Profile_page.class);
                    startActivity(intent);
                    finish();

                } else {
                    if (!result.getContents().contains("VEC-")) {  //constraint to scan only vec id's qr code.
                        Toast.makeText(this, "Scanner scans only VEC code. Don't scan other kind of QR codes!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(scanner.this, Profile_page.class);
                        startActivity(intent);
                        finish();
                    } else {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        DateFormat dateFormats = new SimpleDateFormat("yyyy_MM_dd");
                        Date date = new Date();
                        strDate = dateFormat.format(date);
                        childdate = dateFormats.format(date);
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference("Attendance").child(childdate).child(user.getUid());
                        ref = database.getReference("Overall_Attendance").child(childdate);
                        txt = result.getContents().toString().trim();

                        ref.orderByChild("id").equalTo(result.getContents()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Intent intent = new Intent(scanner.this, Profile_page.class);
                                    startActivity(intent);
                                    Toast.makeText(scanner.this, "Id Already scanned today. Cant scan again!", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    ref = database.getReference("Overall_Attendance").child(childdate).push();
                                    ref.child("id").setValue(result.getContents());
                                    ref.child("Date").setValue(strDate);
                                    ArrayList<String> n = new ArrayList<>();
                                    n.add(result.getContents());

                                    myRef.orderByChild("id").equalTo(result.getContents()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Intent intent = new Intent(scanner.this, Profile_page.class);
                                                startActivity(intent);
                                                Toast.makeText(scanner.this, "Id Already scanned today. Cant scan again!", Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                myRef = database.getReference("Attendance").child(childdate).child(user.getUid()).push();
                                                myRef.child("id").setValue(result.getContents());
                                                myRef.child("Date").setValue(strDate);
                                                ArrayList<String> n = new ArrayList<>();
                                                n.add(result.getContents());
                                                Intent intent = new Intent(scanner.this, Profile_page.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
}

