package com.example.maica.southstarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    List<UserObject> userlist;
    User_Adapter userAdapter;
    ListView listofuser;

    public static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
        userlist = new ArrayList<>();

        listofuser = findViewById(R.id.list_of_users);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userlist.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserObject users = snapshot.getValue(UserObject.class);
                    String userhold = users.getUser_type();
                    if(userhold.equals("Admin")){
                        userlist.add(users);
                    }
                    else{
                        //
                    }
                }
                userAdapter = new User_Adapter(Chat.this, R.layout.user_list, userlist);
                listofuser.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listofuser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView id = view.findViewById(R.id.user_id);

                user_id = id.getText().toString();

                Intent intent = new Intent(getApplicationContext(), Chat_Message.class);

                startActivity(intent);
            }
        });
    }
}
