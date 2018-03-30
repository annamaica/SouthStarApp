package com.example.maica.southstarapp;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class View_Product extends AppCompatActivity implements View.OnClickListener {

    Button addmedicine, btn_search;
    EditText search_med;
    ListView medicinelist;
    List<Medicine_Information> medicine;
    MedicineList_Adapter medicineList_adapter;
    public static String medkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__product);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");
        medicine = new ArrayList<>();

        addmedicine = findViewById(R.id.addmedicine);
        medicinelist = findViewById(R.id.list_view);
        search_med = findViewById(R.id.search_med);

        addmedicine.setOnClickListener(this);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicine.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Medicine_Information view_medicine = snapshot.getValue(Medicine_Information.class);
                    medicine.add(view_medicine);
                }
                medicineList_adapter = new MedicineList_Adapter(View_Product.this, R.layout.medicinelist_layout, medicine);
                medicinelist.setAdapter(medicineList_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        search_med.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");

                String med_name = search_med.getText().toString();
                medicine = new ArrayList<>();

                Query search = db.orderByChild("medicine_name").startAt(med_name).endAt(med_name);
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        medicine.clear();

                        if(dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                                medicine.add(medinfo);
                            }
                            medicineList_adapter = new MedicineList_Adapter(View_Product.this, R.layout.medicinelist_layout, medicine);
                            medicinelist.setAdapter(medicineList_adapter);

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

        medicinelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final TextView id = view.findViewById(R.id.textView4);

                medkey = id.getText().toString();

                Intent intent = new Intent(getApplicationContext(), View_medicine.class);

                startActivity(intent);
                finish();

            }
        });

    }

    public void onClick (View view){
        if (view == addmedicine){
            startActivity(new Intent(this, Add_Product.class));
        }
    }
}
