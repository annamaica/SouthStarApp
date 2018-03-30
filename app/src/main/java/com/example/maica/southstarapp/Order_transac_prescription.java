package com.example.maica.southstarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Order_transac_prescription extends AppCompatActivity {

    ListView transaclist_pres;
    List<PrescriptionAdapter> transac_pres;
    Prescription_List transaction_adapter;
    DatabaseReference db_transac_pres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_transac_prescription);

        db_transac_pres = FirebaseDatabase.getInstance().getReference("Prescription_Transaction_History");
        transac_pres = new ArrayList<>();

        transaclist_pres = findViewById(R.id.listview_transac);

        db_transac_pres.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transac_pres.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PrescriptionAdapter view_transac_pres = snapshot.getValue(PrescriptionAdapter.class);
                    transac_pres.add(view_transac_pres);
                }
                transaction_adapter = new Prescription_List(Order_transac_prescription.this, R.layout.prescription_layout, transac_pres);
                transaclist_pres.setAdapter(transaction_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
