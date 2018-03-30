package com.example.maica.southstarapp;

import android.app.AlarmManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.maica.southstarapp.Reminder.alarmManager;
import static com.example.maica.southstarapp.User_Dashboard.notif_id;

public class User_View_Order extends AppCompatActivity {

    TextView user_order_id, user_medicine_code, user_medicine_quantity, user_order_date, user_order_status;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__view__order);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        String order_id = notif_id;
        user_order_id = findViewById(R.id.user_order_id);
        user_medicine_code = findViewById(R.id.user_medicine_code);
        user_medicine_quantity = findViewById(R.id.user_order_quantity);
        user_order_date = findViewById(R.id.user_order_date);
        user_order_status = findViewById(R.id.user_order_status);


        user_order_id.setText(order_id);

        db = FirebaseDatabase.getInstance().getReference().child("Order_List");
        Query search = db.orderByChild("order_id").equalTo(order_id);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Order_Info infoo = snapshot.getValue(Order_Info.class);
                        try {
                            String medcode = infoo.getOrder_medicine_code();
                            String medqua = infoo.getOrder_quantity();
                            String meddate = infoo.getOrder_date();
                            String medstatus = infoo.getOrder_status();

                            user_medicine_code.setText(medcode);
                            user_medicine_quantity.setText(medqua);
                            user_order_date.setText(meddate);
                            user_order_status.setText(medstatus);
                        }
                        catch (Exception e){

                        }
                    }
                }
                else {
                    Toast.makeText(User_View_Order.this, "none", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
