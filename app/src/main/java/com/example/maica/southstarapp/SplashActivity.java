package com.example.maica.southstarapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Maica on 1/24/2018.
 */

public class SplashActivity extends AppCompatActivity{
    private static int SPLASH_TIME = 2000;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseAuth = FirebaseAuth.getInstance();
                if (firebaseAuth.getCurrentUser() != null){
                    //profile activity is here

                    String user_id = firebaseAuth.getCurrentUser().getUid();

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
                    Query search =  db.orderByChild("userID").equalTo(user_id);

                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                UserObject userObject = snapshot.getValue(UserObject.class);
                                String user_type = userObject.getUser_type();

                                if (user_type.equals("Admin")){
                                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                                    startActivity(intent);
                                    finish();
                                    //Toast.makeText(SplashActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent intent = new Intent(getApplicationContext(), User_Dashboard.class);

                                    startActivity(intent);
                                    finish();
                                    //Toast.makeText(SplashActivity.this, "User", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME);
    }
}
