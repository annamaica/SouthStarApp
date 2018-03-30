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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Medicine_dictionary extends AppCompatActivity {

    ListView medicinelist_dictionary;
    List<Medicine_Information> dictionary;
    Medicine_dictionary_Adapter medicine_dictionary_adapter;

    EditText search_med;
    Button btn_sort;
    String txtsort;
    RadioGroup radioGroup1;
    RadioButton radioButton1;
    int sortID;


    public static String dictionary_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_dictionary);
        setTitle("Medicine Dictionary");

        radioGroup1 = findViewById(R.id.radiogrp_sort);
        btn_sort = findViewById(R.id.btn_sort);
        search_med = findViewById(R.id.search_med);

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");
        dictionary = new ArrayList<>();

        medicinelist_dictionary = findViewById(R.id.list_med_dictionary);


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dictionary.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Medicine_Information view_medicine = snapshot.getValue(Medicine_Information.class);
                    dictionary.add(view_medicine);
                }
                medicine_dictionary_adapter = new Medicine_dictionary_Adapter(Medicine_dictionary.this, R.layout.medicine_dictionary_layout, dictionary);
                medicinelist_dictionary.setAdapter(medicine_dictionary_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortID= radioGroup1.getCheckedRadioButtonId();
                radioButton1 = findViewById(sortID);

                txtsort = radioButton1.getText().toString();

                if (txtsort.equals("All")){
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dictionary.clear();

                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Medicine_Information view_medicine = snapshot.getValue(Medicine_Information.class);
                                dictionary.add(view_medicine);
                            }
                            medicine_dictionary_adapter = new Medicine_dictionary_Adapter(Medicine_dictionary.this, R.layout.medicine_dictionary_layout, dictionary);
                            medicinelist_dictionary.setAdapter(medicine_dictionary_adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });                }

                if (txtsort.equals("Branded")){

                    Query search = db.orderByChild("medicine_type").equalTo("Branded");
                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dictionary.clear();

                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Medicine_Information view_medicine = snapshot.getValue(Medicine_Information.class);
                                dictionary.add(view_medicine);
                            }
                            medicine_dictionary_adapter = new Medicine_dictionary_Adapter(Medicine_dictionary.this, R.layout.medicine_dictionary_layout, dictionary);
                            medicinelist_dictionary.setAdapter(medicine_dictionary_adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                if (txtsort.equals("Generic")){
                    Query search = db.orderByChild("medicine_type").equalTo("Generic");
                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dictionary.clear();

                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Medicine_Information view_medicine = snapshot.getValue(Medicine_Information.class);
                                dictionary.add(view_medicine);
                            }
                            medicine_dictionary_adapter = new Medicine_dictionary_Adapter(Medicine_dictionary.this, R.layout.medicine_dictionary_layout, dictionary);
                            medicinelist_dictionary.setAdapter(medicine_dictionary_adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });                }
            }
        });


        medicinelist_dictionary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView id = view.findViewById(R.id.med_id);

                dictionary_key = id.getText().toString();

                Intent intent = new Intent(getApplicationContext(), Medicine_dictionary_view.class);

                startActivity(intent);
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
                dictionary = new ArrayList<>();

                Query search = db.orderByChild("medicine_name").startAt(med_name).endAt(med_name);
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dictionary.clear();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                                dictionary.add(medinfo);
                            }
                            medicine_dictionary_adapter = new Medicine_dictionary_Adapter(Medicine_dictionary.this, R.layout.medicine_dictionary_layout, dictionary);
                            medicinelist_dictionary.setAdapter(medicine_dictionary_adapter);

                        } else {
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
    }
}
