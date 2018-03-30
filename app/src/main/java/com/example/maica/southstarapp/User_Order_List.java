package com.example.maica.southstarapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class User_Order_List extends AppCompatActivity {

    ListView user_orderlist;
    List<Order_Info> order;
    Non_PrescriptionAdapter non_prescriptionAdapter;
    DatabaseReference db, db2;
    FirebaseAuth auth;
    Dialog dialog;
    FirebaseAuth firebaseAuth;

    public static String orderid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__order__list);

        firebaseAuth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance().getReference().child("Transaction_history");
        auth = FirebaseAuth.getInstance();

        order = new ArrayList<>();

        user_orderlist = findViewById(R.id.list_view_orderlist);

        order.clear();

        Query search = db.orderByChild("order_userID").equalTo(auth.getCurrentUser().getUid());
        search.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                order.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Order_Info view_order = snapshot.getValue(Order_Info.class);
                    order.add(view_order);
//                                Toast.makeText(Order_Prescription.this, "hello", Toast.LENGTH_SHORT).show();
                }
                non_prescriptionAdapter = new Non_PrescriptionAdapter(User_Order_List.this, R.layout.nonprescription_layout, order);
                user_orderlist.setAdapter(non_prescriptionAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        user_orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog = new Dialog(User_Order_List.this);
                dialog.setTitle("Select Option");
                dialog.setContentView(R.layout.option_layout);

                Button add = dialog.findViewById(R.id.btn_add_cart);
                Button remove = dialog.findViewById(R.id.btn_remove_cart);

                final TextView id = view.findViewById(R.id.order);

                orderid = id.getText().toString();

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Pending_List").child(firebaseAuth.getCurrentUser().getUid());
                        DatabaseReference db3 = FirebaseDatabase.getInstance().getReference("Transaction_history");

                        Query search = db3.orderByChild("order_id").equalTo(orderid);
                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Order_Info order = snapshot.getValue(Order_Info.class);
                                    try {
                                        String med_id = order.getOrder_medicine_code();
                                        String med_qua = order.getOrder_quantity();
                                        String med_total = order.getOrder_total();

                                        String id = db2.push().getKey();

                                        Cart_info info_cart = new Cart_info(id, med_id, med_qua, med_total);

                                        db2.child(id).setValue(info_cart);

                                        Toast.makeText(User_Order_List.this, "Medicine added to cart!", Toast.LENGTH_SHORT).show();

                                        dialog.dismiss();
                                    } catch (Exception e) {

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference db3 = FirebaseDatabase.getInstance().getReference("Transaction_history");

                        Query search = db3.orderByChild("order_id").equalTo(orderid);
                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                    Toast.makeText(User_Order_List.this, "Medicine history removed!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
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
    }
}
