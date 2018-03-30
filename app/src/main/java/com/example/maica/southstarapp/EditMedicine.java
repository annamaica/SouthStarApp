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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static com.example.maica.southstarapp.View_Product.medkey;

public class EditMedicine extends AppCompatActivity implements View.OnClickListener {

    EditText edit_id, edit_name, edit_price, edit_desc, edit_stock, edit_dosage;
    Button btn_save, edit_btnbrowse;
    ImageView view_image;
    StorageReference mStorageRef;
    Uri imgUri;
    DatabaseReference db, db_sales;
    public static final String STORAGE_PATH = "userimage/";
    public static final int REQUEST_CODE = 1234;
    String category, type, stock_status, sales_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");
        db_sales =  FirebaseDatabase.getInstance().getReference("Sales");


        edit_id = findViewById(R.id.edit_id);
        edit_name = findViewById(R.id.edit_name);
        edit_price = findViewById(R.id.edit_price);
        edit_desc = findViewById(R.id.edit_desc);
        edit_stock = findViewById(R.id.edit_stock);
        view_image = findViewById(R.id.edit_medicineImage);
        edit_dosage = findViewById(R.id.edit_dosage);
        edit_btnbrowse = findViewById(R.id.edit_btnBrowseImage);

        edit_btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
            }
        });

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");

        String id = medkey;
        Query search = db.orderByChild("medicine_id").equalTo(id);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                        try {
                            type = medinfo.getMedicine_type();
                            category = medinfo.getMedicine_category();
                            edit_id.setText(medinfo.getMedicine_code());
                            edit_name.setText(medinfo.getMedicine_name());
                            edit_price.setText(medinfo.getMedicine_price());
                            edit_desc.setText(medinfo.getMedicine_desc());
                            edit_stock.setText(medinfo.getMedicine_stock());
                            edit_dosage.setText(medinfo.getMedicine_dosage());
                            Glide.with(EditMedicine.this).load(medinfo.getUrl()).into(view_image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query search2 = db_sales.orderByChild("med_id").equalTo(id);
        search2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Sales sales = snapshot.getValue(Sales.class);
                        try {
                            sales_number = sales.getMed_sales();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                view_image.setImageBitmap(bm);
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
        if (view == btn_save) {
            if (imgUri != null) {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Uploading image");
                dialog.show();

                //Get the storage reference
                StorageReference ref = mStorageRef.child(STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

                //Add file to reference

                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //dismiss dialog when success
                        dialog.dismiss();

                        //Save image info in to database

                        String id = edit_id.getText().toString().trim();
                        String name = edit_name.getText().toString().trim();
                        String price = edit_price.getText().toString().trim();
                        String desc = edit_desc.getText().toString().trim();
                        String stock = edit_stock.getText().toString().trim();
                        String dosage = edit_dosage.getText().toString().trim();

                        int stock_num = Integer.parseInt(stock);

                        if (stock_num == 0){
                            stock_status = "Out of Stock";
                        }
                        else if (stock_num > 100){
                            stock_status = "High";
                        }

                        else if (stock_num >= 50 && stock_num <= 100){
                            stock_status = "Normal";
                        }

                        else if (stock_num < 50){
                            stock_status = "Critical";
                        }

                        if (TextUtils.isEmpty(id)) {
                            Toast.makeText(getApplicationContext(), "Please fill up the details", Toast.LENGTH_LONG).show();
                        }
                        if (TextUtils.isEmpty(name)) {
                            Toast.makeText(getApplicationContext(), "Please fill up the details", Toast.LENGTH_LONG).show();
                        }
                        if (TextUtils.isEmpty(price)) {
                            Toast.makeText(getApplicationContext(), "Please fill up the details", Toast.LENGTH_LONG).show();
                        }
                        if (TextUtils.isEmpty(desc)) {
                            Toast.makeText(getApplicationContext(), "Please fill up the details", Toast.LENGTH_LONG).show();
                        } else {


                            //save to medicine_list db
                            String med_id = medkey;
                            Medicine_Information info = new Medicine_Information(med_id, id, name, price, desc, stock, category, dosage, type, name, taskSnapshot.getDownloadUrl().toString(), stock_status);
                            db.child(med_id).setValue(info);

                            //save to sales module
                            Sales item = new Sales(med_id, id, name, stock, stock_status, sales_number);
                            db_sales.child(med_id).setValue(item);

                            Toast.makeText(getApplicationContext(), "Medicine Updated!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
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
                                dialog.setMessage("Uploading " + (int) progress + "");
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please select image to upload", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
