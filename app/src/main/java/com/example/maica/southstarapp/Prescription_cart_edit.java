package com.example.maica.southstarapp;

import android.content.Intent;
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

import static com.example.maica.southstarapp.Prescription_cart.prescrip_key;
import static com.example.maica.southstarapp.Order_Prescription.userid;


public class Prescription_cart_edit extends AppCompatActivity {

    TextView cart_prescrip_id, cart_prescrip_code, cart_prescrip_name, cart_prescrip_price, cart_prescrip_desc, cart_prescrip_quantity, cart_prescrip_dosage;
    Button cart_prescrip_add_quantity, cart_prescrip_minus_quantity, addcart_prescrip;
    ImageView cart_prescrip_image;
    String user;

    public static int quan= 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_cart_edit);

        String key = prescrip_key;

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");

        cart_prescrip_id = findViewById(R.id.cart_prescrip_id);
        cart_prescrip_code = findViewById(R.id.cart_prescrip_code);
        cart_prescrip_name = findViewById(R.id.cart_prescrip_name);
        cart_prescrip_price = findViewById(R.id.cart_prescrip_price);
        cart_prescrip_desc = findViewById(R.id.cart_prescrip_desc);
        cart_prescrip_quantity = findViewById(R.id.cart_prescrip_quantity);
        addcart_prescrip = findViewById(R.id.addcart_prescrip);
        cart_prescrip_dosage = findViewById(R.id.cart_prescrip_dosage);

        cart_prescrip_image = findViewById(R.id.cart_prescrip_Image);


        cart_prescrip_add_quantity = findViewById(R.id.cart_prescrip_add_quantity);
        cart_prescrip_minus_quantity = findViewById(R.id.cart_prescrip_minus_quantity);

        cart_prescrip_add_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quan++;

                String val = Integer.toString(quan);
                cart_prescrip_quantity.setText(val);
            }
        });

        cart_prescrip_minus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String val = Integer.toString(quan);
                cart_prescrip_quantity.setText(val);

                if (quan > 0) {
                    quan--;

                } else {
                    //
                }
            }
        });

        Query search = db.orderByChild("medicine_id").equalTo(key);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                        try {
                            cart_prescrip_id.setText(medinfo.getMedicine_id());
                            cart_prescrip_code.setText(medinfo.getMedicine_code());
                            cart_prescrip_name.setText(medinfo.getMedicine_name());
                            cart_prescrip_price.setText(medinfo.getMedicine_price());
                            cart_prescrip_desc.setText(medinfo.getMedicine_desc());
                            cart_prescrip_dosage.setText(medinfo.getMedicine_dosage());
                            Glide.with(Prescription_cart_edit.this).load(medinfo.getUrl()).into(cart_prescrip_image);

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

        addcart_prescrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = userid;
                String med_id = cart_prescrip_id.getText().toString();
                String med_qua = cart_prescrip_quantity.getText().toString();
                String price = cart_prescrip_price.getText().toString();

                int a, b, multiply;

                a = Integer.parseInt(price);
                b = Integer.parseInt(med_qua);
                multiply = a*b;

                String total = Integer.toString(multiply);



                DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Pending_List").child(user);
                String order_id = db2.push().getKey();
                Cart_info order = new Cart_info(order_id,med_id, med_qua, total);

                db2.child(order_id).setValue(order);

                Toast.makeText(Prescription_cart_edit.this, "Medicine added to cart!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
