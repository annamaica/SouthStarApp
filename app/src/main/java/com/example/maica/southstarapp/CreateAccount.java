package com.example.maica.southstarapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference fdb;
    ProgressBar progressBar;

    private static final String TAG = "CreateAccount";

    private Button button;
    private EditText useremail,passwords, conpassword;
    TextView loginaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        useremail = findViewById(R.id.email);
        passwords = findViewById(R.id.password);
        button = findViewById(R.id.button);
        conpassword = findViewById(R.id.conpassword);
        progressBar = findViewById(R.id.progressbar);

        loginaccount = findViewById(R.id.loginaccount);

        button.setOnClickListener(CreateAccount.this);
        loginaccount.setOnClickListener(this);
    }

    public void onClick(View view) {

        if (view == button){
            String email = useremail.getText().toString().trim();
            String password = passwords.getText().toString().trim();
            String cpassword = conpassword.getText().toString().trim();

            progressBar.setVisibility(View.VISIBLE);

            if (password.length() > 0){
                if (password.equals(cpassword)){
                    registerUser(email, password);
                }else{
                    conpassword.setError("Password does not match!");
                }
            }else{
                passwords.setError("Please enter a valid password");
            }
        }

        if (view == loginaccount){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void registerUser(final String email, final String password){
        Log.d("BUTTON CLICK", "clicked");
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d("ACCOUNT", "Account created");
                            userLogin(email, password);
                        }else {
                            Toast.makeText(CreateAccount.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void userLogin(String username, String password){
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(CreateAccount.this, CreateAccountInfo.class);
                    startActivity(intent);
                    finish();
                }else{

                }
            }
        });
    }

}
