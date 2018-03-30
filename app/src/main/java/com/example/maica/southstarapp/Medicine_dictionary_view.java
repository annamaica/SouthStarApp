package com.example.maica.southstarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.maica.southstarapp.Medicine_dictionary.dictionary_key;

public class Medicine_dictionary_view extends AppCompatActivity {

    TextView dictionary_id, dictionary_name, dictionary_type, dictionary_dosage, dictionary_desc;
    ImageView dictionary_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_dictionary_view);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");

        String key = dictionary_key;

        dictionary_id = findViewById(R.id.dictionary_id);
        dictionary_name = findViewById(R.id.dictionary_name);
        dictionary_type = findViewById(R.id.dictionary_type);
        dictionary_dosage = findViewById(R.id.dictionary_dosage);
        dictionary_desc = findViewById(R.id.dictionary_desc);
        dictionary_image = findViewById(R.id.dictionary_image);

        Query search = db.orderByChild("medicine_id").equalTo(key);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                        try {
                            dictionary_id.setText(medinfo.getMedicine_code());
                            dictionary_name.setText(medinfo.getMedicine_name());
                            dictionary_type.setText(medinfo.getMedicine_type());
                            dictionary_dosage.setText(medinfo.getMedicine_dosage());
                            dictionary_desc.setText(medinfo.getMedicine_desc());
                            Glide.with(Medicine_dictionary_view.this).load(medinfo.getUrl()).into(dictionary_image);
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
