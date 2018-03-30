package com.example.maica.southstarapp;

import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import static com.example.maica.southstarapp.Reminder.alarmManager;


import static com.example.maica.southstarapp.User_View_Product.medkey;

public class User_View_medicine extends AppCompatActivity {

    TextView user_view_id, user_view_code, user_view_name, user_view_price, user_view_desc, user_quantity, user_view_dosage;
    Button user_add_quantity, user_minus_quantity, addcart;
    FirebaseAuth firebaseAuth;
    ImageView user_medicine_image;


    public static int quan= 0;
    int hoursToAdd = 24;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__view_medicine);
        setTitle("View Medicine");

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");

        user_view_id = findViewById(R.id.user_view_id);
        user_view_code = findViewById(R.id.user_view_code);
        user_view_name = findViewById(R.id.user_view_name);
        user_view_price = findViewById(R.id.user_view_price);
        user_view_desc = findViewById(R.id.user_view_desc);
        user_quantity = findViewById(R.id.user_quantity);
        user_view_dosage = findViewById(R.id.user_view_dosage);
        addcart = findViewById(R.id.addcart);

        user_medicine_image = findViewById(R.id.user_medicineImage);


        user_add_quantity = findViewById(R.id.user_add_quantity);
        user_minus_quantity = findViewById(R.id.user_minus_quantity);

        user_add_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quan++;

                String val = Integer.toString(quan);
                user_quantity.setText(val);
            }
        });

        user_minus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String val = Integer.toString(quan);
                user_quantity.setText(val);

                if (quan > 0) {
                    quan--;

                } else {
                    //
                }
            }
        });

        String id = medkey;

        Query search = db.orderByChild("medicine_id").equalTo(id);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                        try {
                            user_view_id.setText(medinfo.getMedicine_id());
                            user_view_code.setText(medinfo.getMedicine_code());
                            user_view_name.setText(medinfo.getMedicine_name());
                            user_view_price.setText(medinfo.getMedicine_price());
                            user_view_desc.setText(medinfo.getMedicine_desc());
                            user_view_dosage.setText(medinfo.getMedicine_dosage());
                            Glide.with(User_View_medicine.this).load(medinfo.getUrl()).into(user_medicine_image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String med_id = user_view_id.getText().toString();
                String med_qua = user_quantity.getText().toString();
                String price = user_view_price.getText().toString();

                float a, b, multiply;

                a = Float.parseFloat(price);
                b = Float.parseFloat(med_qua);
                multiply = a*b;

                String total = Float.toString(multiply);



                DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Pending_List").child(firebaseAuth.getCurrentUser().getUid());
                String order_id = db2.push().getKey();
                Cart_info order = new Cart_info(order_id,med_id, med_qua, total);

                db2.child(order_id).setValue(order);

                Toast.makeText(User_View_medicine.this, "Medicine added to cart!", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

}
