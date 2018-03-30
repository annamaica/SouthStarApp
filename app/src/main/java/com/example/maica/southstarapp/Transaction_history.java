package com.example.maica.southstarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Transaction_history extends AppCompatActivity {

    EditText search_transac;
    ListView transaclist;
    List<OrderList> transac;
    Transaction_Adapter transaction_adapter;
    DatabaseReference db_transac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        db_transac = FirebaseDatabase.getInstance().getReference().child("Transaction_history");
        transac = new ArrayList<>();

        transaclist = findViewById(R.id.transac_list_view);
        search_transac = findViewById(R.id.search_transac);

        db_transac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transac.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    OrderList view_transac = snapshot.getValue(OrderList.class);
                    transac.add(view_transac);
                }
                transaction_adapter = new Transaction_Adapter(Transaction_history.this, R.layout.transaclist_layout, transac);
                transaclist.setAdapter(transaction_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
