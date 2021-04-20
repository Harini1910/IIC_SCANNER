package com.example.iic_scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_page extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private TextView t1,t2,t3,t4,t5;
    private  String Username,IDNo,Dept,Email,MobileNumber;
    public CircleImageView circle;
    private static final int PICK_IMAGE = 1;
    private static final String TAG ="Profile_page";
    public Uri imageUri;
    public Uri resultUri;
    DatabaseReference databaseReference,profile;
    private long backpressedtime;
    private Toast backtoast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationview);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.scanner:
                        startActivity(new Intent(getApplicationContext(), scanner.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.profile:
                        return true;
                    case R.id.attendance:
                        startActivity(new Intent(getApplicationContext(), Attendance_Result.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        circle = (CircleImageView) findViewById(R.id.circle);
        t1 = findViewById(R.id.username);
        t2 = findViewById(R.id.idno);
        t3 = findViewById(R.id.dept);
        t4 = findViewById(R.id.mail);
        t5 = findViewById(R.id.mobileno);

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(Profile_page.this)
                        .load(user.getPhotoUrl())
                        .into(circle);
            }
        }

        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("signup").child(user.getUid());
            profile = FirebaseDatabase.getInstance().getReference().child("profile").child("images");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Username = (Objects.requireNonNull(dataSnapshot.child("Username").getValue())).toString();
                    IDNo = (Objects.requireNonNull(dataSnapshot.child("IDNo").getValue())).toString();
                    Dept = (Objects.requireNonNull(dataSnapshot.child("Dept").getValue())).toString();
                    Email = (Objects.requireNonNull(dataSnapshot.child("Email").getValue())).toString();
                    MobileNumber = (Objects.requireNonNull(dataSnapshot.child("MobileNumber").getValue())).toString();

                    t1.setText(Username);
                    t2.setText(IDNo);
                    t3.setText(Dept);
                    t4.setText(Email);
                    t5.setText(MobileNumber);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(gallery, "select picture"), PICK_IMAGE);


                }

            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode  == RESULT_OK && data != null) {
            imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(result==null){
                Toast.makeText(Profile_page.this,"You cancelled uploading profile image!",Toast.LENGTH_SHORT).show();
            }else {
                resultUri = result.getUri();
                if(resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        circle.setImageBitmap(bitmap);
                        handleUpload(bitmap);
                        Toast.makeText(Profile_page.this,"Updated Successfully!",Toast.LENGTH_LONG).show();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child( ""+ ".jpeg");
        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e.getCause() );
                    }
                });
    }
    public void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setUserProfileUrl(Uri uri) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        Objects.requireNonNull(user).updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile_page.this, "Profile image failed...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void Signout(View v){
        PopupMenu popupMenu=new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();

        SharedPreferences sharedPreferences= getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editors=sharedPreferences.edit();
        editors.putBoolean("isFirsttime",true);
        editors.apply();

        Intent intent=new Intent(Profile_page.this,MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
    @Override
    public void onBackPressed(){
       if (backpressedtime+2000 > System.currentTimeMillis()){
           backtoast.cancel();
           super.onBackPressed();
           return;
       }else {
               backtoast=Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT);
               backtoast.show();
           }
       backpressedtime=System.currentTimeMillis();
    }
}
