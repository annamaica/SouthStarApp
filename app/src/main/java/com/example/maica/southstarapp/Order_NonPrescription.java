package com.example.maica.southstarapp;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Order_NonPrescription extends AppCompatActivity {

    EditText search_order;
    ListView orderlist;
    List<Order_Info> order;
    TextView order_id;
    Non_PrescriptionAdapter non_prescriptionAdapter;
    Dialog dialog;
    FirebaseAuth auth;
    String stock_status;
    DatabaseReference db, db2, db3, db4, db5, db6, db_sales;
    int hoursToAdd = 24;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    public static String notif_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__non_prescription);

        notification = new NotificationCompat.Builder(getApplicationContext());
        notification.setAutoCancel(true);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("Order_List");
        db2 = FirebaseDatabase.getInstance().getReference().child("Order_List");

        db3 = FirebaseDatabase.getInstance().getReference("Pick_up");
        db4 = FirebaseDatabase.getInstance().getReference("Transaction_history");
        db5 = FirebaseDatabase.getInstance().getReference().child("Medicine_List");
        db6 = FirebaseDatabase.getInstance().getReference("Push_Notification");

        order = new ArrayList<>();

        orderlist = findViewById(R.id.order_list_view);
        search_order = findViewById(R.id.search_order);


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Order_Info view_order = snapshot.getValue(Order_Info.class);
                    order.add(view_order);
                }
                non_prescriptionAdapter = new Non_PrescriptionAdapter(Order_NonPrescription.this, R.layout.nonprescription_layout, order);
                orderlist.setAdapter(non_prescriptionAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog = new Dialog(Order_NonPrescription.this);
                dialog.setTitle("Select Action");
                dialog.setContentView(R.layout.action_dialog);

                Button pickup = dialog.findViewById(R.id.pickup);
                Button cancelled = dialog.findViewById(R.id.cancelled);
                Button complete = dialog.findViewById(R.id.complete);

                TextView order_id = view.findViewById(R.id.order);

                final String id = order_id.getText().toString();

                pickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Query search = db2.orderByChild("order_id").equalTo(id);
                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){
                                    for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                        Order_Info pending = snapshot2.getValue(Order_Info.class);
                                        try {
                                            String order_id = pending.getOrder_id();
                                            String order_userid = pending.getOrder_userID();
                                            String order_user = pending.getOrder_user();
                                            String order_medname = pending.getOrder_medicine_name();
                                            String order_medcode = pending.getOrder_medicine_code();
                                            String order_quantity = pending.getOrder_quantity();
                                            String order_date = pending.getOrder_date();
                                            String order_address = pending.getOrder_address();
                                            String order_total = pending.getOrder_total();
                                            String order_category = pending.getOrder_category();

                                            Pending_info info = new Pending_info(order_id, order_user, order_address, order_medcode, order_quantity, order_date, "Pick Up");

                                            Calendar c = Calendar.getInstance();
                                            c.add(Calendar.HOUR, hoursToAdd);
                                            Date cc = c.getTime();
                                            String expiredate = cc.toString();

                                            Order_Info order = new Order_Info(order_id, order_user, order_userid, order_address,order_medcode, order_medname, order_quantity, order_total,
                                                    order_date, expiredate, "Pick-up", order_category);

                                            PushNotification notif = new PushNotification(order_id, order_userid, "Your medicine is ready to pick-up!");


                                            String pending_id = order_id;
                                            String notif_id = db6.push().getKey();

                                            db3.child(pending_id).setValue(info);
                                            db2.child(pending_id).setValue(order);
                                            db6.child(notif_id).setValue(notif);

                                            //save to db transac
                                            db4.child(pending_id).setValue(order);

                                            dialog.dismiss();

                                            Toast.makeText(Order_NonPrescription.this, "Ready for pick-up", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(Order_NonPrescription.this, "none", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                cancelled.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Query search = db2.orderByChild("order_id").equalTo(id);
                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                        Order_Info pending = snapshot2.getValue(Order_Info.class);
                                        try {
                                            String order_id = pending.getOrder_id();
                                            String order_user = pending.getOrder_user();
                                            String order_userid = pending.getOrder_userID();
                                            String order_address = pending.getOrder_address();
                                            String order_medname = pending.getOrder_medicine_name();
                                            String order_medcode = pending.getOrder_medicine_code();
                                            String order_quantity = pending.getOrder_quantity();
                                            String order_date = pending.getOrder_date();
                                            String order_total = pending.getOrder_total();
                                            String order_category = pending.getOrder_category();

                                            Pending_info info = new Pending_info(order_id, order_user, order_address, order_medcode, order_quantity, order_date, "Cancelled");

                                            String cancelled_id = order_id;

                                            Order_Info order = new Order_Info(order_id, order_user, order_userid, order_address,order_medcode, order_medname, order_quantity,order_total,
                                                    order_date, "", "Cancelled", order_category);

                                            db4.child(cancelled_id).setValue(order);
                                            snapshot2.getRef().removeValue();

                                            //save to db transac
                                            db4.child(cancelled_id).setValue(order);

                                            //save to notif
                                            String notif_id = db6.push().getKey();

                                            PushNotification notif = new PushNotification(order_id, order_userid, "Your medicine is cancelled." + "\n" + "No more stocks available.");
                                            db6.child(notif_id).setValue(notif);


                                            dialog.dismiss();
                                            Toast.makeText(Order_NonPrescription.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(Order_NonPrescription.this, "none", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Query search = db2.orderByChild("order_id").equalTo(id);
                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (final DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                        Order_Info pending = snapshot2.getValue(Order_Info.class);
                                        try {
                                            final String order_id = pending.getOrder_id();
                                            final String order_user = pending.getOrder_user();
                                            final String order_medcode = pending.getOrder_medicine_code();
                                            final String order_medname = pending.getOrder_medicine_name();
                                            final String order_quantity = pending.getOrder_quantity();
                                            final String order_date = pending.getOrder_date();
                                            final String order_address = pending.getOrder_address();
                                            final String order_expire = pending.getOrder_expire();
                                            final String order_userid = pending.getOrder_userID();
                                            final String order_total = pending.getOrder_total();
                                            final String order_category = pending.getOrder_category();

                                            //quantity

                                            db_sales =  FirebaseDatabase.getInstance().getReference("Sales");

                                            Query search3 = db_sales.orderByChild("med_id").equalTo(order_medcode);
                                            search3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        for (DataSnapshot snapshot4 : dataSnapshot.getChildren()) {
                                                            Sales sales = snapshot4.getValue(Sales.class);
                                                            try{
                                                                float number_sales, price_med, total_price;
                                                                String med_sales = sales.getMed_sales();
                                                                number_sales = Float.parseFloat(med_sales);
                                                                price_med = Float.parseFloat(order_total);

                                                                total_price = number_sales+price_med;

                                                                final String total_sales = Float.toString(total_price);

                                                                //Sales item = new Sales(order_medcode, med_id, name, total, stock_status, total_sales);

                                                                //quantity

                                                                Query search2 = db5.orderByChild("medicine_id").equalTo(order_medcode);
                                                                search2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if(dataSnapshot.exists()){
                                                                            for (DataSnapshot snapshot3 : dataSnapshot.getChildren()){
                                                                                Medicine_Information medinfo = snapshot3.getValue(Medicine_Information.class);
                                                                                try {
                                                                                    float a, b, sum;

                                                                                    String med_id = medinfo.getMedicine_code();
                                                                                    String name = medinfo.getMedicine_name();
                                                                                    String price = medinfo.getMedicine_price();
                                                                                    String desc = medinfo.getMedicine_desc();
                                                                                    String category = medinfo.getMedicine_category();
                                                                                    String dosage = medinfo.getMedicine_dosage();
                                                                                    String type = medinfo.getMedicine_type();
                                                                                    String url = medinfo.getUrl();


                                                                                    String stock = medinfo.getMedicine_stock();
                                                                                    a = Float.parseFloat(stock);
                                                                                    b = Float.parseFloat(order_quantity);

                                                                                    if (b > a){
                                                                                        Toast.makeText(Order_NonPrescription.this, "Maximum allowed for the item to be ordered is " + stock, Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                    else{
                                                                                        sum = a-b;

                                                                                        String total = Float.toString(sum);

                                                                                        Order_Info order = new Order_Info(order_id, order_user, order_userid, order_address,order_medcode, order_medname, order_quantity,order_total,
                                                                                                order_date, order_expire, "Complete", order_category);

                                                                                        //sales

                                                                                        float stock_num = Float.parseFloat(total);

                                                                                        if (stock_num == 0){
                                                                                            stock_status = "Out of Stock";

                                                                                            //save to notif db

                                                                                            String notif_id = db6.push().getKey();

                                                                                            PushNotification notif = new PushNotification(notif_id, auth.getCurrentUser().getUid(), "The Number of "+ name + " is in " + stock_status);
                                                                                            db6.child(notif_id).setValue(notif);

                                                                                        }
                                                                                        else if (stock_num > 100){
                                                                                            stock_status = "High";
                                                                                        }

                                                                                        else if (stock_num >= 50 && stock_num <= 100){
                                                                                            stock_status = "Normal";
                                                                                        }

                                                                                        else if (stock_num < 50){
                                                                                            stock_status = "Critical";

                                                                                            //save to notif db

                                                                                            String notif_id = db6.push().getKey();

                                                                                            PushNotification notif = new PushNotification(notif_id, auth.getCurrentUser().getUid(), "The Number of "+ name + " is in " + stock_status);
                                                                                            db6.child(notif_id).setValue(notif);
                                                                                        }

                                                                                        Medicine_Information info = new Medicine_Information(order_medcode, med_id, name, price, desc, total, category, dosage, type,  name, url, stock_status);

                                                                                        Sales item = new Sales(order_medcode, med_id, name, total, stock_status, total_sales);

                                                                                        String complete_id = order_id;

                                                                                        //save to db transac
                                                                                        db4.child(complete_id).setValue(order);
                                                                                        snapshot2.getRef().removeValue();

                                                                                        //save to med list
                                                                                        db5.child(order_medcode).setValue(info);

                                                                                        //save to sales
                                                                                        db_sales.child(order_medcode).setValue(item);

                                                                                        //save to notif
                                                                                        String notif_id = db6.push().getKey();

                                                                                        PushNotification notif = new PushNotification(order_id, order_userid, "Your medicine transaction is complete." + "\n" + "Thank you for choosing South Star Drug.");
                                                                                        db6.child(notif_id).setValue(notif);

                                                                                        dialog.dismiss();

                                                                                        Toast.makeText(Order_NonPrescription.this, "Transaction Complete", Toast.LENGTH_SHORT).show();
                                                                                    }
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
                                                            catch (Exception e){

                                                            }
                                                        }
                                                    }
                                                    else {
                                                        Toast.makeText(Order_NonPrescription.this, "none", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(Order_NonPrescription.this, "none", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });
        search_order.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Order_List");

                String med_name = search_order.getText().toString();
                order = new ArrayList<>();

                Query search = db.orderByChild("order_medicine").startAt(med_name).endAt(med_name);
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        order.clear();

                        if(dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Order_Info orderinfo = snapshot.getValue(Order_Info.class);
                                order.add(orderinfo);
                            }
                            non_prescriptionAdapter = new Non_PrescriptionAdapter(Order_NonPrescription.this, R.layout.nonprescription_layout, order);
                            orderlist.setAdapter(non_prescriptionAdapter);

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "NONE", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Push_Notification");
        Query search5 = databaseReference2.orderByChild("notifUser").equalTo(auth.getCurrentUser().getUid());
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

                        Intent intent = new Intent(Order_NonPrescription.this, User_Order_List.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(Order_NonPrescription.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    }

    public void sendnotif(View view){
//        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, Order_NonPrescription.class), 0);
//        mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.logo)
//                .setContentIntent(pi)
//                .setAutoCancel(true)
//                .setContentTitle("My notification")
//                .setContentText("Hello World!");
//
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        mNotificationManager.notify(001, mBuilder.build());

//
//        String orderid = order_id.getText().toString();
//        db2 = FirebaseDatabase.getInstance().getReference().child("Order_List");
//        Query search =  db2.orderByChild("order_id").equalTo(orderid);
//
//
//        search.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()){
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Order_Info order_info = snapshot.getValue(Order_Info.class);
//                        try
//                        {
//                            String order_id = order_info.getOrder_id();
//                            String order_user = order_info.getOrder_user();
//                            String order_medcode = order_info.getOrder_medicine_code();
//                            String order_quantity = order_info.getOrder_quantity();
//                            String order_date = order_info.getOrder_date();
//
//                            Pending_info info = new Pending_info(order_id, order_user, order_medcode, order_quantity, order_date);
//
////                            String pending_id = db2.push().getKey();
////
////                            db2.child(pending_id).setValue(info);
//                            Toast.makeText(Order_NonPrescription.this, order_id, Toast.LENGTH_SHORT).show();
//
//                        }
//                        catch (Exception e){
//
//                        }
//                    }
//                }
//                else{
//                    Toast.makeText(Order_NonPrescription.this, "none", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
