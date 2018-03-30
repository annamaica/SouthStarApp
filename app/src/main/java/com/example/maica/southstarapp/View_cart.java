package com.example.maica.southstarapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.maica.southstarapp.Reminder.alarmManager;

public class View_cart extends AppCompatActivity {


    Button checkout;
    ListView pendinglist;
    List<Cart_info> cart;
    View_Order_Adapter pendingList_adapter;
    FirebaseAuth auth;
    Dialog dialog;
    int hoursToAdd = 24;
    public static String cart_id;
    NotificationCompat.Builder notification1;
    private static final int uniqueID = 45612;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        auth = FirebaseAuth.getInstance();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Pending_List").child(auth.getCurrentUser().getUid());
        final DatabaseReference db_notif = FirebaseDatabase.getInstance().getReference("Push_Notification");
        final DatabaseReference db_history = FirebaseDatabase.getInstance().getReference("Transaction_history");

        cart = new ArrayList<>();

        checkout = findViewById(R.id.checkout);
        pendinglist = findViewById(R.id.list_view_cart);

        notification1 = new NotificationCompat.Builder(getApplicationContext());
        notification1.setAutoCancel(true);


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cart.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Cart_info view_cart = snapshot.getValue(Cart_info.class);
                    cart.add(view_cart);

                }
                pendingList_adapter = new View_Order_Adapter(View_cart.this, R.layout.user_cart, cart);
                pendinglist.setAdapter(pendingList_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        pendinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                dialog = new Dialog(View_cart.this);
//                dialog.setTitle("Select Option:");
//                dialog.setContentView(R.layout.cart_options);
//
//                Button edit = dialog.findViewById(R.id.cart_edit);
//                Button delete = dialog.findViewById(R.id.cart_delete);
//
//                edit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(View_cart.this, "edit", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                dialog.show();
//            }
//        });

        pendinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView order_id  = view.findViewById(R.id.order_id);

                cart_id = order_id.getText().toString();

                dialog = new Dialog(View_cart.this);
                dialog.setTitle("Select Option:");
                dialog.setContentView(R.layout.cart_options);

                Button edit = dialog.findViewById(R.id.cart_edit);
                Button delete = dialog.findViewById(R.id.cart_delete);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Cart_Edit.class);

                        startActivity(intent);

                        dialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(View_cart.this);

                        builder.setTitle("Confirm Delete");
                        builder.setMessage("Are you sure?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                String order = order_id.getText().toString();

                                DatabaseReference dbref2 = FirebaseDatabase.getInstance().getReference("Pending_List");
                                Query queryRef = dbref2.child(auth.getCurrentUser().getUid()).orderByChild("order_id").equalTo(order);

                                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                            snapshot.getRef().removeValue();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                dialog.show();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(View_cart.this, "hello", Toast.LENGTH_SHORT).show();


                String user_id = auth.getCurrentUser().getUid();

                final DatabaseReference db_order = FirebaseDatabase.getInstance().getReference("Pending_List").child(auth.getCurrentUser().getUid());
                final DatabaseReference db_order2 = FirebaseDatabase.getInstance().getReference("Order_List");
                DatabaseReference db_order3 = FirebaseDatabase.getInstance().getReference().child("Users");
                final Query search =  db_order3.orderByChild("userID").equalTo(user_id);

                db_order.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Cart_info view_cart = snapshot.getValue(Cart_info.class);

                                final String order_id = view_cart.getOrder_id();
                                final String order_medid = view_cart.getOrder_medicine_id();
                                final String order_quantity = view_cart.getOrder_medicine_quantity();
                                final String order_total = view_cart.getOrder_total();

                                final DatabaseReference db_order4 = FirebaseDatabase.getInstance().getReference("Medicine_List");
                                final Query search2 = db_order4.orderByChild("medicine_id").equalTo(order_medid);

                                search2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for (DataSnapshot snapshot3 : dataSnapshot.getChildren()){
                                                Medicine_Information medinfo = snapshot3.getValue(Medicine_Information.class);
                                                try {
                                                    final String name = medinfo.getMedicine_name();
                                                    final String category = medinfo.getMedicine_category();

                                                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot2) {
                                                            if (dataSnapshot2.exists()){
                                                                for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()){
                                                                    UserObject userObject = snapshot2.getValue(UserObject.class);
                                                                    try {
                                                                        String user_firstname = userObject.getFirst_name();
                                                                        String user_lastname = userObject.getLast_name();
                                                                        String user_address = userObject.getAddress();
                                                                        String user_id = userObject.getUserID();

                                                                        String full_name = user_firstname + " " + user_lastname;

                                                                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                                                                        Order_Info order = new Order_Info(order_id, full_name, user_id, user_address,order_medid, name, order_quantity, order_total,
                                                                                currentDateTimeString, "", "Processing", category);

                                                                        //save data into transaction history

                                                                        db_history.child(order_id).setValue(order);
                                                                        //save data to order table
                                                                        db_order2.child(order_id).setValue(order);
                                                                        snapshot.getRef().removeValue();

                                                                        //save data to notif

                                                                        PushNotification notif = new PushNotification(order_id, user_id, "Incoming order");
                                                                        String notif_id = db_notif.push().getKey();

                                                                        db_notif.child(notif_id).setValue(notif);

                                                                        int incid = uniqueID + 1;

                                                                        notification1.setSmallIcon(R.drawable.logo);
                                                                        notification1.setWhen(System.currentTimeMillis());
                                                                        notification1.setContentTitle("South Star Drug");
                                                                        notification1.setContentText("Your Order has been sent. Please wait for the Admin to process it.");

                                                                        Intent intent2 = new Intent(View_cart.this, User_Order_List.class);
                                                                        PendingIntent pendingIntent = PendingIntent.getActivity(View_cart.this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                                                                        notification1.setContentIntent(pendingIntent);

                                                                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                        nm.notify(incid, notification1.build());

                                                                        finish();

                                                                    }
                                                                    catch(Exception e){

                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                                catch (Exception e){

                                                }
                                            }
                                        }
                                        else{

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(View_cart.this, "none", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_products:

                Intent intent = new Intent(getApplicationContext(), User_View_Product.class);

                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
