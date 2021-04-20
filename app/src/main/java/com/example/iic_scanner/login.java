package com.example.iic_scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private Button login;
    private EditText Email ;
    private EditText Password;
    public  CheckBox checkBox;
    private FirebaseAuth mAuth;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.login);
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);
        checkBox=findViewById(R.id.checkBox);

        //using shared preference to check whether user already logged in or not.
        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");

        if (checkbox.equals("true")){
            Intent intent=new Intent(login.this,Profile_page.class);
            startActivity(intent);
            finish();
        }
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.buttonsound2);
        mAuth = FirebaseAuth.getInstance();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }else if(!compoundButton.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = Email.getText().toString();
                password = Password.getText().toString();
                mp.start();

                if (Email.length() == 0) {
                    Email.setError("Enter Mail-Id!");
                } else if (Password.length() == 0) {
                    Password.setError("Enter Password!");
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)//verifying authenticated user.
                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(login.this, Profile_page.class);
                                        startActivity(intent);
                                        Toast.makeText(login.this, " Login Success!",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(login.this, " Please provide valid MailId and Password!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent=new Intent(login.this,MainActivity.class);
        startActivity(intent);
    }
}
