package com.example.employeenew;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    TextView tvName, tvPhoneNumber, tvEmailId, tvDeptName, tvEmpId, tvLocation;
    Button btnBack;
    ImageView ivImage;
    private DatabaseReference mref;
    private FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvDeptName=findViewById(R.id.tvDeptName);
        tvEmailId=findViewById(R.id.tvEmailId);
        tvEmpId=findViewById(R.id.tvEmpId);
        tvName=findViewById(R.id.tvName);
        tvLocation=findViewById(R.id.tvLocation);
        ivImage = findViewById(R.id.ivImage);
        tvPhoneNumber=findViewById(R.id.tvPhoneNumber);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mref = firebaseDatabase.getReference();
        btnBack=findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        mref= FirebaseDatabase.getInstance().getReference("Employee Details");

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(firebaseAuth.getUid()).child("name").getValue(String.class);
                String email = dataSnapshot.child(firebaseAuth.getUid()).child("email").getValue(String.class);
                String phonenumber = dataSnapshot.child(firebaseAuth.getUid()).child("phonenumber").getValue(String.class);
                String empid = dataSnapshot.child(firebaseAuth.getUid()).child("empid").getValue(String.class);
                String deptname = dataSnapshot.child(firebaseAuth.getUid()).child("department").getValue(String.class);
                String url = dataSnapshot.child(firebaseAuth.getUid()).child("image").child("imageURL").getValue(String.class);
                Double latitude=dataSnapshot.child(firebaseAuth.getUid()).child("latitude").getValue(Double.class);
                Double longitude=dataSnapshot.child(firebaseAuth.getUid()).child("longitude").getValue(Double.class);
                Geocoder myLocation = new Geocoder(ProfileActivity.this, Locale.getDefault());
                List<Address> myList = null;
                try {
                    myList = myLocation.getFromLocation(latitude,longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = (Address) myList.get(0);
                String addressStr = "";
                addressStr += address.getAddressLine(0) ;
                Picasso.get().load(url).into(ivImage);
                tvName.setText(name);
                tvEmailId.setText(email);
                tvEmpId.setText(empid);
                tvPhoneNumber.setText(phonenumber);
                tvDeptName.setText(deptname);
                tvLocation.setText(addressStr);
                /*ivImage.setImageURI(image);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (R.id.ProfileActivity == item.getItemId())
        {

            Intent b = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(b);
            finish();
        }


        if (R.id.Logout == item.getItemId())
        {
            firebaseAuth.signOut();
            Intent b = new Intent(ProfileActivity.this, LogIn.class);
            startActivity(b);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}