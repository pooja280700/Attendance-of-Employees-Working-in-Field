package com.example.employeenew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    TextView tvLocation;
    Button btnValidateLocation;
    FirebaseAuth firebaseAuth;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        btnValidateLocation = findViewById(R.id.btnValidateLocation);
        tvLocation =findViewById(R.id.tvLocation);
        firebaseAuth = FirebaseAuth.getInstance();
        mref= FirebaseDatabase.getInstance().getReference("Employee Details");
        btnValidateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddressActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddressActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }



                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationEnabled();
                getLocation();

            }
        });



    }

    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(AddressActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            final double latitude=addresses.get(0).getLatitude();
            final double longitude = addresses.get(0).getLongitude();
            String address=addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String City = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String Full_Address = "Your Location is,\n"+""+ address + latitude + longitude;
            tvLocation.setText(Full_Address);

            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double latitudefromdb = dataSnapshot.child(firebaseAuth.getUid()).child("latitude").getValue(double.class);
                    double longitudefromdb = dataSnapshot.child(firebaseAuth.getUid()).child("longitude").getValue(double.class);

                    if (latitudefromdb == latitude && longitudefromdb== longitude){
                        Toast.makeText(AddressActivity.this, "Location Matched", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(AddressActivity.this, FaceRecognition.class);
                        startActivity(a);
                        finish();
                    }else{
                        Toast.makeText(AddressActivity.this, "Location not Matched", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







        } catch (Exception e) {
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

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

            Intent b = new Intent(AddressActivity.this, ProfileActivity.class);
            startActivity(b);
            finish();
        }


        if (R.id.Logout == item.getItemId())
        {
            firebaseAuth.signOut();
            Intent b = new Intent(AddressActivity.this, LogIn.class);
            startActivity(b);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}