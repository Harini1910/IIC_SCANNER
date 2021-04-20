package com.example.iic_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashscreen extends AppCompatActivity {
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        img=(ImageView)findViewById(R.id.logo);
        Animation vecl= (Animation) AnimationUtils.loadAnimation(splashscreen.this,R.anim.mytransition);
        img.startAnimation(vecl);

        Thread timer =new Thread(){
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    boolean isFirsttime;
                    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editors = sharedPreferences.edit();
                    isFirsttime = sharedPreferences.getBoolean("isFirsttime", true);
                    if (isFirsttime) {
                        editors.putBoolean("isFirsttime", false);
                        editors.apply();
                        Intent intent = new Intent(splashscreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        String checkbox = preferences.getString("remember", "");
                        if (checkbox.equals("true")) {
                            Intent intent = new Intent(splashscreen.this, Profile_page.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(splashscreen.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        };
        timer.start();
    }
}
