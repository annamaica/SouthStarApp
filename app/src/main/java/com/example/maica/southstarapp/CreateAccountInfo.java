package com.example.maica.southstarapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class CreateAccountInfo extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private String userId;
    TextView txt_date, txt_age;
    DatePickerDialog datePickerDialog;

    EditText txtfname, txtlname, txtmname, txtaddress;
    Button btnsubmit, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_info);
        setTitle("Setup Information");

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        userId = firebaseAuth.getCurrentUser().getUid();

        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(CreateAccountInfo.this, MainActivity.class);
            startActivity(intent);
        }

        txtfname = findViewById(R.id.txtfname);
        txtlname = findViewById(R.id.txtlname);
        txtmname = findViewById(R.id.txtmname);
        txtaddress = findViewById(R.id.txtaddress);

        btnsubmit = findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);

        // initiate the date picker and a button
        txt_date = findViewById(R.id.txt_date);
        txt_age = findViewById(R.id.txt_age);
        date = findViewById(R.id.date);
        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                final int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateAccountInfo.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the text view
                                txt_date.setText((monthOfYear + 1) + "/"
                                        + dayOfMonth + "/" + year);

                                Calendar today = Calendar.getInstance();
                                int age = today.get(Calendar.YEAR) - year;

                                String total = Integer.toString(age);

                                txt_age.setText(total);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }

        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnsubmit){
            String fname = txtfname.getText().toString();
            String lname = txtlname.getText().toString();
            String mname = txtmname.getText().toString();
            String address = txtaddress.getText().toString();
            String age = txt_age.getText().toString();
            String bday = txt_date.getText().toString();

            if (fname.length() == 0 ||
                    lname.length() == 0 ||
                    mname.length() == 0 ||
                    address.length() == 0 ||
                    age.length() == 0 ||
                    bday.length() == 0){

                Toast.makeText(CreateAccountInfo.this, "Fill all necessary fields!", Toast.LENGTH_SHORT).show();
            }else{
                addUserInfo(fname, lname, mname, address, age, bday);
            }
        }
    }

    private void addUserInfo(String fname, String lname, String mname, String address, String age, String bday) {

        UserObject userObject = new UserObject(userId, fname, lname, mname, "User", address, age, bday);
        mDatabase.child("Users").child(userId).setValue(userObject);

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Verification", "Email sent.");
                            firebaseAuth.signOut();
                            Toast.makeText(CreateAccountInfo.this, "A verification email was sent to your account", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateAccountInfo.this, MainActivity.class));
                            finish();
                        }
                    }
                });


    }
}
