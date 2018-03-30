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

public class SalesModule extends AppCompatActivity {

    ListView saleslist;
    List<Sales> sales;
    Sales_adapter sales_adapter;
    DatabaseReference db_sales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_module);

        sales = new ArrayList<>();
        saleslist = findViewById(R.id.list_view_sales);

        db_sales =  FirebaseDatabase.getInstance().getReference("Sales");

        db_sales.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sales.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Sales salesitem = snapshot.getValue(Sales.class);
                    sales.add(salesitem);
                }
                sales_adapter = new Sales_adapter(SalesModule.this, R.layout.sales_layout, sales);
                saleslist.setAdapter(sales_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
