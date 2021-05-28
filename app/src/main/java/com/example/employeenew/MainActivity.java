package com.example.employeenew;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import android.app.AlarmManager;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Notification ;
import android.os.SystemClock ;
import java.util.Calendar;
import android.content.Context;
import android.app.PendingIntent;
import java.util.Date;
import java.util.TimeZone;
public class MainActivity extends AppCompatActivity {
    Button btnMarkAttendance;
    TextView tvWelEmail;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase Employee_Attendace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance);
        firebaseAuth = FirebaseAuth.getInstance();
        tvWelEmail = findViewById(R.id.tvWelEmail);
        Employee_Attendace = FirebaseDatabase.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();
        tvWelEmail.setText("Welcome " + email);


        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, AddressActivity.class);
                startActivity(intent1);
                finish();

            }
        }); }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.ProfileActivity == item.getItemId()) {
            Intent b = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(b);
            finish();
        }
        if (R.id.Logout == item.getItemId()) {
            firebaseAuth.signOut();
            Intent b = new Intent(MainActivity.this, LogIn.class);
            startActivity(b);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exit");
        alertDialog.setMessage("Do you want to exit");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(1);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog a = alertDialog.create();
        a.show();
    }

    }

