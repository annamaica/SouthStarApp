package com.example.maica.southstarapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    Button view_medicine, store_locator, btn_orders, btn_logout, btn_transac, btn_sales, btn_med_dictionary, btn_chat;
    FirebaseAuth firebaseAuth;
    Dialog dialog;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    public static String notif_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        else{
            boolean emailVerified = user.isEmailVerified();
            if(emailVerified){
            }
            else{
                Toast.makeText(Dashboard.this, "You have to validate your account through email", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

        }

        notification = new NotificationCompat.Builder(getApplicationContext());
        notification.setAutoCancel(true);

        view_medicine = findViewById(R.id.view_medicine);
        store_locator = findViewById(R.id.store_locator);
        btn_logout = findViewById(R.id.btn_logout);
        btn_orders = findViewById(R.id.view_order);
        btn_transac = findViewById(R.id.transac);
        btn_sales = findViewById(R.id.sales);
        btn_med_dictionary = findViewById(R.id.btn_med_dictionary);
        btn_chat = findViewById(R.id.btn_chat);

        btn_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(Dashboard.this);
                dialog.setTitle("Order Transactions");
                dialog.setContentView(R.layout.order_dialog);

                Button orderlist = dialog.findViewById(R.id.order_list);
                Button pendinglist = dialog.findViewById(R.id.pending_list);
                Button btn_cancel = dialog.findViewById(R.id.cancel);

                orderlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), Order_NonPrescription.class));
                    }
                });

                pendinglist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), Order_Prescription.class));
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        view_medicine.setOnClickListener(this);
        store_locator.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_transac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(Dashboard.this);
                dialog.setTitle("Select Transaction");
                dialog.setContentView(R.layout.dialog_order);

                Button view_nonpres = dialog.findViewById(R.id.view_nonpre);
                Button view_pres = dialog.findViewById(R.id.view_pre);
                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

                view_nonpres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), Transaction_history.class));
                    }
                });

                view_pres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), Order_transac_prescription.class));
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        btn_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SalesModule.class));
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Chat2.class));
            }
        });

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Push_Notification");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //Toast.makeText(Dashboard.this, "hello", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        int incid = uniqueID + 1;
                        PushNotification pushNotification = snapshot.getValue(PushNotification.class);
                        String pd = pushNotification.getNotifComment();
                        notif_id = pushNotification.getNotifID_Order();

//                        Toast.makeText(getApplicationContext(), pd, Toast.LENGTH_SHORT).show();

                        notification.setSmallIcon(R.drawable.logo);
                        notification.setTicker(pd);
                        notification.setWhen(System.currentTimeMillis());
                        notification.setContentTitle("South Star Drug");
                        notification.setContentText(pd);

                        Intent intent = new Intent(Dashboard.this, Order_NonPrescription.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(Dashboard.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        notification.setContentIntent(pendingIntent);

                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        nm.notify(incid, notification.build());

                        snapshot.getRef().removeValue();
                  }
                }
                else{
//                    Toast.makeText(User_Dashboard.this, "none", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_med_dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Medicine_dictionary.class));
            }
        });

    }

    public void onClick(View view){
        if (view == view_medicine){
            Intent intent = new Intent(getApplicationContext(), View_Product.class);

            startActivity(intent);
        }
        if (view == store_locator){
            startActivity(new Intent(this, MapsActivity.class));
        }
        if (view == btn_logout){
            firebaseAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();
        }
    }
}
