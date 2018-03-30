package com.example.maica.southstarapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.maica.southstarapp.View_Product.medkey;

public class View_medicine extends AppCompatActivity {

    TextView view_id, view_name, view_price, view_desc, view_stock, view_dosage;
    Button edit_button, delete_button;
    ImageView medicine_image;
    public static int quan= 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine);


        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");

        view_id = findViewById(R.id.view_id);
        view_name = findViewById(R.id.view_name);
        view_price = findViewById(R.id.view_price);
        view_desc = findViewById(R.id.view_desc);
        view_stock = findViewById(R.id.view_stock);
        view_dosage = findViewById(R.id.view_dosage);
        medicine_image = findViewById(R.id.medicineImage);

        edit_button = findViewById(R.id.editbutton);
        delete_button = findViewById(R.id.deletebutton);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditMedicine.class);

                startActivity(intent);
                finish();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(View_medicine.this);

                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this medicine?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String key = medkey;

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");
                        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("Sales");
                        Query search = db.orderByChild("medicine_id").equalTo(key);

                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();

                                    finish();

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        Query search2 = db2.orderByChild("med_id").equalTo(key);

                        search2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                    snapshot2.getRef().removeValue();

                                    finish();

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        String id = medkey;

        Query search = db.orderByChild("medicine_id").equalTo(id);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                        try {
                            view_id.setText(medinfo.getMedicine_code());
                            view_name.setText( medinfo.getMedicine_name());
                            view_price.setText(medinfo.getMedicine_price());
                            view_desc.setText(medinfo.getMedicine_desc());
                            view_stock.setText(medinfo.getMedicine_stock());
                            view_dosage.setText(medinfo.getMedicine_dosage());
                            Glide.with(View_medicine.this).load(medinfo.getUrl()).into(medicine_image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
