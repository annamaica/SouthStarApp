package com.example.maica.southstarapp;

import android.app.AlarmManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import static com.example.maica.southstarapp.Reminder.alarmManager;

public class User_View_Product extends AppCompatActivity {

    EditText user_search_med;
    ListView user_medicinelist;
    List<Medicine_Information> user_medicine;
    MedicineList_Adapter user_medicineList_adapter;
    public static String medkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__view__product);
        setTitle("Medicine List");

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");
        user_medicine = new ArrayList<>();

        user_medicinelist = findViewById(R.id.user_list_view);
        user_search_med = findViewById(R.id.user_search_med);

        Query search = db.orderByChild("medicine_category").equalTo("Non-Prescription");
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_medicine.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                    String status = medinfo.getMedicine_status();

                    if (!status.equals("Out of Stock")) {
                        Medicine_Information view_medicine = snapshot.getValue(Medicine_Information.class);
                        user_medicine.add(view_medicine);
                    }
                    user_medicineList_adapter = new MedicineList_Adapter(User_View_Product.this, R.layout.medicinelist_layout, user_medicine);
                    user_medicinelist.setAdapter(user_medicineList_adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//                user_medicine.clear();
//
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    Medicine_Information view_medicine = snapshot.getValue(Medicine_Information.class);
//                    user_medicine.add(view_medicine);
//                }
//                user_medicineList_adapter = new MedicineList_Adapter(User_View_Product.this, R.layout.medicinelist_layout, user_medicine);
//                user_medicinelist.setAdapter(user_medicineList_adapter);

        user_search_med.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");

                String med_name = user_search_med.getText().toString();
                user_medicine = new ArrayList<>();

                Query search = db.orderByChild("medicine_name").startAt(med_name).endAt(med_name);
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user_medicine.clear();

                        if(dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                                user_medicine.add(medinfo);
                            }
                            user_medicineList_adapter = new MedicineList_Adapter(User_View_Product.this, R.layout.medicinelist_layout, user_medicine);
                            user_medicinelist.setAdapter(user_medicineList_adapter);

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

        user_medicinelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final TextView id = view.findViewById(R.id.textView4);

                medkey = id.getText().toString();

                Intent intent = new Intent(getApplicationContext(), User_View_medicine.class);

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:

                Intent intent = new Intent(getApplicationContext(), View_cart.class);

                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
