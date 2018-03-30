package com.example.maica.southstarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.example.maica.southstarapp.View_cart.cart_id;

public class Cart_Edit extends AppCompatActivity {

    FirebaseAuth auth;
    EditText pending_quantity;
    TextView pending_id;
    DatabaseReference db;
    Button edit, delete;
    String cart_med_id, cart_total, cart_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__edit);

        String edit_id = cart_id;

        auth = FirebaseAuth.getInstance();

        pending_id = findViewById(R.id.pending_id);
        pending_quantity = findViewById(R.id.pending_quantity);

        db = FirebaseDatabase.getInstance().getReference("Pending_List").child(auth.getCurrentUser().getUid());

        Query search = db.orderByChild("order_id").equalTo(edit_id);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Cart_info cart = snapshot.getValue(Cart_info.class);
                        try {

                            String cart_pending_id = cart.getOrder_id();
                            String cart_pending_quantity = cart.getOrder_medicine_quantity();
                            cart_total = cart.getOrder_total();
                            cart_med_id = cart.getOrder_medicine_id();

                            pending_id.setText(cart_pending_id);
                            pending_quantity.setText(cart_pending_quantity);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{

                    Toast.makeText(Cart_Edit.this, "None", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit = findViewById(R.id.btn_cart_edit);
        delete = findViewById(R.id.btn_cart_cancel);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String edit_quantity = pending_quantity.getText().toString();
                final String edit_id = pending_id.getText().toString();

                //get medicine price

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Medicine_List");

                Query search = databaseReference.orderByChild("medicine_id").equalTo(cart_med_id);
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                                try {
                                    cart_price = medinfo.getMedicine_price();
                                    int a, b, multiply;

                                    a = Integer.parseInt(cart_price);
                                    b = Integer.parseInt(edit_quantity);
                                    multiply = a*b;

                                    String total = Integer.toString(multiply);

                                    Cart_info order = new Cart_info(edit_id,cart_med_id, edit_quantity, total);

                                    db.child(edit_id).setValue(order);

                                    Toast.makeText(Cart_Edit.this, "Cart edited!", Toast.LENGTH_SHORT).show();
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else{
                            Toast.makeText(Cart_Edit.this, "none", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
