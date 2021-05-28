package com.example.employeenew;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class SignUp extends AppCompatActivity {
    Button btnSignUp, btnChoose, btnUpload;
    EditText etPhoneNo, etSignUpEmailId, etSignUpPassword, etFullName, etEmpId, etDepartmentName;
    RadioGroup rgGender;
    RadioButton rbFemale, rbMale;
    TextView tvAlreadyRegister;
    ImageView ivChsImg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase Signup;
    DatabaseReference signup;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final String CHANNEL_ID = "Attendance Marking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new   NotificationChannel(CHANNEL_ID,
                    CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_ID);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        btnSignUp = findViewById(R.id.btnSignUp);
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        ivChsImg = findViewById(R.id.ivChsImg);
        etSignUpEmailId=findViewById(R.id.etSignUpEmailId);
        etSignUpPassword=findViewById(R.id.etSignUpPassword);
        etPhoneNo=findViewById(R.id.etPhoneNo);
        etEmpId=findViewById(R.id.etEmpId);
        etDepartmentName=findViewById(R.id.etDepartmentName);
        etFullName=findViewById(R.id.etFullName);
        rgGender=findViewById(R.id.rgGender);
        rbFemale= findViewById(R.id.rbFemale);
        rbMale = findViewById(R.id.rbMale);
        tvAlreadyRegister=findViewById(R.id.tvAlreadyRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        Signup = FirebaseDatabase.getInstance();
        signup = Signup.getReference("Employee Details");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        final Date Time = Calendar.getInstance().getTime();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String name = etFullName.getText().toString();
                final String email = etSignUpEmailId.getText().toString();
                final String password = etSignUpPassword.getText().toString();
                final String empid = etEmpId.getText().toString();
                final String phonenumber = etPhoneNo.getText().toString();
                final String department = etDepartmentName.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat=  new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                String dateTime = simpleDateFormat.format(calendar.getTime());



                if (name.length() == 0) {
                    etFullName.setError("Name is empty");
                    etFullName.requestFocus();
                    return;
                }

                if (email.length() == 0) {
                    etSignUpEmailId.setError("Email is is empty");
                    etSignUpEmailId.requestFocus();
                    return;
                }

                if (password.length() == 0) {
                    etSignUpPassword.setError("Password is empty");
                    etSignUpPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    etSignUpPassword.setError("Password must be greater than 5");
                    etSignUpPassword.requestFocus();
                    return;
                }

                if (empid.length() == 0) {
                    etEmpId.setError("Email is is empty");
                    etEmpId.requestFocus();
                    return;
                }

                if (phonenumber.length() == 0) {
                    etPhoneNo.setError("Phone Number is empty");
                    etPhoneNo.requestFocus();
                    return;
                }
                if (phonenumber.length() != 10) {
                    etPhoneNo.setError("Phone number Should contain atleast 10 digits");
                    etPhoneNo.requestFocus();
                    return;
                }

                if (department.length() == 0) {
                    etDepartmentName.setError("Email is is empty");
                    etDepartmentName.requestFocus();
                    return;
                }



                final SignUpData signUPdata = new SignUpData(name, email, empid, phonenumber,department,dateTime );
                final String randomkey = UUID.randomUUID().toString();
                final StorageReference riversRef = storageReference.child("images/" + randomkey);


                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    Signup.getReference("Employee Details").child(firebaseAuth.getUid()).setValue(signUPdata);
                                    displayNotification();
                                    Toast.makeText(SignUp.this,
                                            "Registered Successfully!!",Toast.LENGTH_SHORT).show();


                                    

                                    Intent a = new Intent(SignUp.this,
                                            UploadImage.class);
                                    a.putExtra("Name", name);
                                    startActivity(a);
                                    finish();
                                } else {
                                    Toast.makeText(SignUp.this,
                                            "invalid info " + task.getException()
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }




        });

    }




    private void displayNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message_black)
                .setContentTitle("Employee Attendance App")
                .setContentText("Registered Successfully!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,builder.build());
    }
}

