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

import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Order_Prescription extends AppCompatActivity {

    ListView prescriptionlist;
    List<PrescriptionAdapter> prescription;
    Prescription_List prescription_adapter;
    DatabaseReference db_prescription, db_user, db_order, db, db_transac;
    Dialog dialog;
    public static String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__prescription);


        db_user = FirebaseDatabase.getInstance().getReference("Users");
        db_transac = FirebaseDatabase.getInstance().getReference("Prescription_Transaction_History");
        prescription = new ArrayList<>();

        prescriptionlist = findViewById(R.id.prescription_list_view);

        db_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UserObject userInfo = snapshot.getValue(UserObject.class);
                    String user_id = userInfo.getUserID();
                    db_prescription = FirebaseDatabase.getInstance().getReference("Prescription").child(user_id);
                    db_prescription.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                PrescriptionAdapter view_pres = snapshot.getValue(PrescriptionAdapter.class);
                                prescription.add(view_pres);
//                                Toast.makeText(Order_Prescription.this, "hello", Toast.LENGTH_SHORT).show();
                            }
                                prescription_adapter = new Prescription_List(Order_Prescription.this, R.layout.prescription_layout, prescription);
                                prescriptionlist.setAdapter(prescription_adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        prescriptionlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog = new Dialog(Order_Prescription.this);
                dialog.setTitle("Select Action");
                dialog.setContentView(R.layout.prescription_dialog);

                Button med_list = dialog.findViewById(R.id.med_list);
                Button cancelled = dialog.findViewById(R.id.pres_cancelled);
                Button complete = dialog.findViewById(R.id.pres_complete);

                TextView presc_id = findViewById(R.id.prescrip_id);
                final TextView presc_user_id = findViewById(R.id.prescrip_userid);

                final String prescription = presc_id.getText().toString();

                med_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        userid = presc_user_id.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), Prescription_cart.class);
                        startActivity(intent);
                        finish();

                    }
                });

                cancelled.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userid = presc_user_id.getText().toString();

                        db_order = FirebaseDatabase.getInstance().getReference("Prescription").child(userid);
                        Query search = db_order.orderByChild("prescription_id").equalTo(prescription);
                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        PrescriptionAdapter adapter = snapshot.getValue(PrescriptionAdapter.class);
                                        try{
                                            String pres_id = adapter.getPrescription_id();
                                            String pres_userid = adapter.getPrescription_userid();
                                            String url = adapter.getUrl();
                                            String pres_name = adapter.getPrescription_user();
                                            String pres_med = adapter.getPrescription_image();

                                            PrescriptionAdapter info = new PrescriptionAdapter(pres_id, pres_userid, pres_name, pres_med, "Cancelled", url);
                                            db_transac.child(pres_id).setValue(info);

                                            snapshot.getRef().removeValue();
                                            Toast.makeText(Order_Prescription.this, "Prescription Cancelled", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                                            startActivity(intent);
                                            finish();
                                        }
                                        catch (Exception e){

                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(Order_Prescription.this, "none", Toast.LENGTH_SHORT).show();
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
                        userid = presc_user_id.getText().toString();
                        db_order = FirebaseDatabase.getInstance().getReference("Prescription").child(userid);
                        Query search = db_order.orderByChild("prescription_id").equalTo(prescription);
                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        PrescriptionAdapter adapter = snapshot.getValue(PrescriptionAdapter.class);
                                        try{
                                            String pres_id = adapter.getPrescription_id();
                                            String pres_userid = adapter.getPrescription_userid();
                                            String url = adapter.getUrl();
                                            String pres_name = adapter.getPrescription_user();
                                            String pres_med = adapter.getPrescription_image();

                                            PrescriptionAdapter info = new PrescriptionAdapter(pres_id, pres_userid, pres_name, pres_med, "Complete", url);
                                            db_transac.child(pres_id).setValue(info);
                                            snapshot.getRef().removeValue();

                                            Toast.makeText(Order_Prescription.this, "Prescription Complete", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                                            startActivity(intent);
                                            finish();
                                        }
                                        catch (Exception e){

                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(Order_Prescription.this, "none", Toast.LENGTH_SHORT).show();
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
