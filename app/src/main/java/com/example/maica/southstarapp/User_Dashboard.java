package com.example.maica.southstarapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

public class User_Dashboard extends AppCompatActivity {

    Button user_view_medicine, user_view_location, user_reminder, user_logout, user_cart, user_list, btn_dictionary, user_btn_chat;
    FirebaseAuth firebaseAuth;
    int hoursToAdd = 24;
    Dialog dialog;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    public static String notif_id;


    FirebaseUser userloggedin = FirebaseAuth.getInstance().getCurrentUser();
    boolean emailVerified = userloggedin.isEmailVerified();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__dashboard);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(!emailVerified){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        notification = new NotificationCompat.Builder(getApplicationContext());
        notification.setAutoCancel(true);


        user_view_medicine = findViewById(R.id.user_view_medicine);
        user_view_location = findViewById(R.id.user_store_locator);
        user_reminder = findViewById(R.id.user_reminder);
        user_logout = findViewById(R.id.user_btn_logout);
        user_cart = findViewById(R.id.user_view_cart);
        user_list = findViewById(R.id.user_btn_list);
        btn_dictionary = findViewById(R.id.user_btn_dictionary);
        user_btn_chat = findViewById(R.id.user_btn_chat);

        //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        //datetry.setText(currentDateTimeString);

//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.HOUR, hoursToAdd);
 //       Date cc = c.getTime();
  //      datetry.setText(cc.toString());

        user_view_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(User_Dashboard.this);
                dialog.setTitle("Order Transactions");
                dialog.setContentView(R.layout.dialog_order);

                Button view_nonpres = dialog.findViewById(R.id.view_nonpre);
                Button view_pres = dialog.findViewById(R.id.view_pre);
                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

                view_nonpres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), User_View_Product.class);

                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                view_pres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Prescription.class);

                        startActivity(intent);
                        dialog.dismiss();                 }
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

        user_view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        user_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), View_cart.class));
            }
        });

        user_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Reminder.class));

            }
        });

        user_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), User_Order_List.class);

                startActivity(intent);
            }
        });

        btn_dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Medicine_dictionary.class);

                startActivity(intent);
            }
        });

        user_btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Chat.class);

                startActivity(intent);
            }
        });

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Push_Notification");
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Push_Notification");
        Query search5 = databaseReference2.orderByChild("notifUser").equalTo(firebaseAuth.getCurrentUser().getUid());
        search5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
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

                    Intent intent = new Intent(User_Dashboard.this, User_Order_List.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(User_Dashboard.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
//        databaseReference2.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Intent intent = new Intent(User_Dashboard.this, User_Dashboard.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                //Toast.makeText(getApplicationContext(),"Change",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                //Toast.makeText(getApplicationContext(),"Move",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
}
