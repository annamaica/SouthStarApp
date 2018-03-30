package com.example.maica.southstarapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Prescription extends AppCompatActivity implements View.OnClickListener{

    Button btn_send, btn_browse_image;
    DatabaseReference db_pres, db_user;
    StorageReference mStorageRef;
    ImageView image;
    Uri imgUri;
    FirebaseAuth firebaseAuth;

    public static final String STORAGE_PATH = "prescription/";
    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db_pres = FirebaseDatabase.getInstance().getReference("Prescription").child(firebaseAuth.getCurrentUser().getUid());
        db_user = FirebaseDatabase.getInstance().getReference().child("Users");

        btn_send = findViewById(R.id.sendimage);
        btn_browse_image = findViewById(R.id.browseimage);
        image = findViewById(R.id.image);

        btn_browse_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
            }
        });

        btn_send.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                image.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void onClick(View view) {
        if (view == btn_send){
            if (imgUri != null) {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Uploading image");
                dialog.show();

                //Get the storage reference
                StorageReference ref = mStorageRef.child(STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

                //Add file to reference

                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        //dismiss dialog when success
                        dialog.dismiss();

                        //Save image info in to database
                        Query search = db_user.orderByChild("userID").equalTo(firebaseAuth.getCurrentUser().getUid());
                        search.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        UserObject userObject = snapshot.getValue(UserObject.class);
                                        try {
                                            String user_firstname = userObject.getFirst_name();
                                            String user_lastname = userObject.getLast_name();
                                            String user_id = userObject.getUserID();

                                            String full_name = user_firstname + " " + user_lastname;

                                            String pres_id = db_pres.push().getKey();
                                            PrescriptionAdapter info = new PrescriptionAdapter(pres_id, user_id, full_name, full_name, "Processing",taskSnapshot.getDownloadUrl().toString());
                                            db_pres.child(pres_id).setValue(info);

                                            Toast.makeText(getApplicationContext(), "Prescription Sent!", Toast.LENGTH_SHORT).show();


                                            Intent intent = new Intent(getApplicationContext(), User_Dashboard.class);

                                            startActivity(intent);
                                            finish();
                                        }
                                        catch (Exception e){

                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(Prescription.this, "none", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                //dismiss dialog when fail
                                dialog.dismiss();
                                //display success toast
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                //show upload progress

                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                dialog.setMessage("Uploading");
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please select image to upload", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
