package com.example.maica.southstarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser fuser;
    private DatabaseReference fdb;

    EditText username,password;
    Button login;
    TextView createaccount;
    ProgressBar progBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            login = findViewById(R.id.button);
            createaccount = findViewById(R.id.createaccount);

            progBar = findViewById(R.id.progbar);

            firebaseAuth = FirebaseAuth.getInstance();
            fdb = FirebaseDatabase.getInstance().getReference(); // Access to child

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String uname = username.getText().toString().trim();
                    String pword = password.getText().toString().trim();
                    if (TextUtils.isEmpty(uname)){
                        //email is empty
                        Toast.makeText(MainActivity.this,"Please enter email", Toast.LENGTH_SHORT).show();
                        //stopping the function executing further
                        return;
                    }
                    if (TextUtils.isEmpty(pword)){
                        //email is empty
                        Toast.makeText(MainActivity.this,"Please enter password", Toast.LENGTH_SHORT).show();
                        //stopping the function executing further
                        return;
                    }
                    else{
                        userLogin(uname, pword);
                        progBar.setVisibility(View.VISIBLE);
                    }
                }
            });

            createaccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                    startActivity(intent);
                    finish();

                }
            });
    }

    private void userLogin(final String username, String password){
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String user_id = firebaseAuth.getCurrentUser().getUid();

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
                    Query search =  db.orderByChild("userID").equalTo(user_id);

                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    UserObject userObject = snapshot.getValue(UserObject.class);
                                    try {
                                        String user_type = userObject.getUser_type().toString();

                                        if (user_type.equals("Admin")){
                                            Toast.makeText(MainActivity.this, "Success" , Toast.LENGTH_SHORT ).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        }
                                        else {

                                            FirebaseUser userloggedin = FirebaseAuth.getInstance().getCurrentUser();
                                            boolean emailVerified = userloggedin.isEmailVerified();
                                            if (emailVerified) {
                                                Toast.makeText(MainActivity.this, "Welcome " + username + " !", Toast.LENGTH_SHORT).show();
                                                finish();
                                                startActivity(new Intent(getApplicationContext(), User_Dashboard.class));
                                            } else {
                                                Toast.makeText(getApplicationContext(), "You have to validate your account through email", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                firebaseAuth.signOut();

                                            }
                                        }
                                    }
                                    catch(Exception e){

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    Toast.makeText(MainActivity.this, "Failed" , Toast.LENGTH_SHORT ).show();
                    progBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
