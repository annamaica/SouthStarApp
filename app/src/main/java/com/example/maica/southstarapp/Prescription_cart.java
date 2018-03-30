package com.example.maica.southstarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.maica.southstarapp.Order_Prescription.userid;


public class Prescription_cart extends AppCompatActivity {

    ListView prescription_list;
    List<Medicine_Information> prescrip_med;
    MedicineList_Adapter user_medicineList_adapter;
    public static String prescrip_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_cart);

        String user = userid;

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");
        prescrip_med = new ArrayList<>();

        prescription_list = findViewById(R.id.list_view_prescription);

        Query search = db.orderByChild("medicine_category").equalTo("Prescription");
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prescrip_med.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Medicine_Information view_medicine = snapshot.getValue(Medicine_Information.class);
                    prescrip_med.add(view_medicine);
                }
                user_medicineList_adapter = new MedicineList_Adapter(Prescription_cart.this, R.layout.medicinelist_layout, prescrip_med);
                prescription_list.setAdapter(user_medicineList_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prescription_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView id = view.findViewById(R.id.textView4);

                prescrip_key = id.getText().toString();

                Intent intent = new Intent(getApplicationContext(), Prescription_cart_edit.class);

                startActivity(intent);
            }
        });
    }
}
