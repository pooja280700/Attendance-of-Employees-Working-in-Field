package com.example.collegenew;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class FaceRecogTrial extends AppCompatActivity {
    Button takepicture;
    private DatabaseReference mref;
    FirebaseAuth firebaseAuth;
    TextView text,text1;
    ImageView imageView,ivImage;
    BitmapDrawable drawable, drawable1;
    private FirebaseDatabase Attendance;
    DatabaseReference attendanceref;
    Bitmap bitmap, bitmap1;
    Uri imageuri;
    private FirebaseDatabase firebaseDatabase;
    String Imagestring1 = "", Imagestring2 = "";
    public static final int CAMERA_REQUEST = 9999;
    Python py;
    private static final String CHANNEL_ID = "Attendance Marking";
    public int count9 = 0;
    public int count11 = 0;
    public int count13 = 0;
    public int count15 = 0;
    public int count17 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new   NotificationChannel(CHANNEL_ID,
                    CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_ID);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Attendance = FirebaseDatabase.getInstance();
        attendanceref = Attendance.getReference("Employee Attendance");
        takepicture = (Button)findViewById(R.id.takepicture);
        imageView = (ImageView)findViewById(R.id.imageView) ;
        ivImage = (ImageView) findViewById(R.id.ivImage);
        text = (TextView)findViewById(R.id.text);
        text1 = (TextView)findViewById(R.id.text1);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mref = firebaseDatabase.getReference();


        if (!Python.isStarted())
            Python.start(new AndroidPlatform(this));
         py = Python.getInstance();

        mref= FirebaseDatabase.getInstance().getReference("Employee Details");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child(firebaseAuth.getUid()).child("image").child("imageURL").getValue(String.class);

                Picasso.get().load(url).into(new Target(){
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from){
                        ivImage.setImageBitmap(bitmap);

                        drawable1 = (BitmapDrawable) ivImage.getDrawable();
                        bitmap1 = drawable1.getBitmap();

                        //text1.setText("Database Image fetched");
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);




            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAMERA_REQUEST ) {


            bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            drawable = (BitmapDrawable) imageView.getDrawable();
            //bitmap = drawable.getBitmap();
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            //text.setText("Image Clicked");
            callfun();









        }


    }
    public void callfun() {
        Imagestring1 = getStringImage(bitmap);
        Imagestring2 = getStringImage(bitmap1);
        PyObject pyobj = py.getModule("script");

        PyObject obj = pyobj.callAttr("main", Imagestring1, Imagestring2);

        if (obj.toString().equals("Same images")) {
            mref = FirebaseDatabase.getInstance().getReference("Employee Details");
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String empid = snapshot.child(firebaseAuth.getUid()).child("empid").getValue(String.class);
                    String name = snapshot.child(firebaseAuth.getUid()).child("name").getValue(String.class);
                    String email = firebaseAuth.getCurrentUser().getEmail();
                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat a = new SimpleDateFormat("a");
                    String am_or_pm = a.format(calendar.getTime());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    String dateTime = simpleDateFormat.format(calendar.getTime());
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                    String date = simpleDateFormat1.format(calendar.getTime());
                    SimpleDateFormat time_hr = new SimpleDateFormat("hh");
                    int hr = Integer.parseInt(time_hr.format(calendar.getTime()));
                    SimpleDateFormat time_minute = new SimpleDateFormat("mm");
                    int min = Integer.parseInt(time_hr.format(calendar.getTime()));
                    String Attendance = "Absent";
                    if (am_or_pm == "AM") {
                            if (hr == 9 && min >= 0 && min <= 30 && count9==0) {
                                count9++;
                                //Attendance = "Full day Present";
                                //displayNotificationPresent();
                            } if (hr == 11 && min >= 0 && min <= 30 && count9==1 || count9 ==0) {
                                count11++;
                                //Attendance = "Full day Present";
                                //displayNotificationPresent();
                            }
                        }
                         if (am_or_pm == "PM") {   

                            if (hr == 22 && min >= 20 && min <= 25 && count11==1 || count11==0) {
                                count13++;
                                //Attendance = "Full day Present";
                                //displayNotificationPresent();
                            }
                            if (hr == 22 && min >= 26 && min <= 30 && count13==1) {
                                count15++;
                                //Attendance = "Full day Present";
                                //displayNotificationPresent();
                            }
                            if (hr == 22 && min >= 31 && min <= 35 && count15==1) {
                                count17++;
                                //Attendance = "Full day Present";
                                //displayNotificationPresent();
                            }
                            if(count9 == 1 && count11 == 1 && count13 == 1 && count15 == 1 && count17 == 1){
                                Attendance = "Full day present";
AttendanceData attendanceData = new AttendanceData(email, dateTime, Attendance, empid, name);
                    attendanceref.child(firebaseAuth.getUid()).child(date).setValue(attendanceData);
                    displayNotification();
                            }
                             if(count9 == 0 && count11 == 1 && count13 == 1 && count15 == 1 && count17 == 1){
                                Attendance = "half day present";
                                AttendanceData attendanceData = new AttendanceData(email, dateTime, Attendance, empid, name);
                    attendanceref.child(firebaseAuth.getUid()).child(date).setValue(attendanceData);
                    displayNotification();
                            }
                             if(count9 == 0 && count11 == 0 && count13 == 1 && count15 == 1 && count17 == 1){
                                Attendance = "half day present";
                                AttendanceData attendanceData = new AttendanceData(email, dateTime, Attendance, empid, name);
                    attendanceref.child(firebaseAuth.getUid()).child(date).setValue(attendanceData);
                    displayNotification();
                            }
                             if(count9 == 0 && count11 == 0 && count13 == 0 ){
                                Attendance = "Absent";
                                AttendanceData attendanceData = new AttendanceData(email, dateTime, Attendance, empid, name);
                    attendanceref.child(firebaseAuth.getUid()).child(date).setValue(attendanceData);
                    displayNotification();
                            }

                        
                    }

                    

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





        }
        text.setText(obj.toString());
    }




    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;


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



    private void displayNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message_black)
                .setContentTitle("Employee Attendance App")
                .setContentText("Attendance Marked Successfully")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,builder.build());
    }

    private void displayNotificationPresent(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message_black)
                .setContentTitle("Employee Attendance App")
                .setContentText("You are marked Present Today!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,builder.build());
    }

    private void displayNotificationAbsent(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message_black)
                .setContentTitle("Employee Attendance App")
                .setContentText("You are marked Absent Today!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,builder.build());
    }
    private void displayNotificationHalfDay(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message_black)
                .setContentTitle("Employee Attendance App")
                .setContentText("Your Attendance has Marked for Half Day!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,builder.build());
    }





}
