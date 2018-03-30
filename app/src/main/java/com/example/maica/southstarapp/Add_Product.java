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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Add_Product extends AppCompatActivity implements View.OnClickListener{

    EditText txt_medicine_id, txt_medicine_name, txt_medicine_price, txt_medicine_desc, txt_medicine_stock, dosage;
    Button btn_addmedicine, btn_browse_image;
    DatabaseReference db_medicine, db_sales;
    StorageReference mStorageRef;
    ImageView AddImage;
    Uri imgUri;
    public static final String STORAGE_PATH = "medicineimage/";
    public static final int REQUEST_CODE = 1234;
    String txtcategory, txttype, stock_status;
    RadioGroup radioGroup, radiotype;
    RadioButton radioButton, buttontype;
    int selectedID, typeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__product);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        txt_medicine_id = findViewById(R.id.medicine_id);
        txt_medicine_name = findViewById(R.id.medicine_name);
        txt_medicine_price = findViewById(R.id.medicine_price);
        txt_medicine_desc = findViewById(R.id.medicine_desc);
        txt_medicine_stock = findViewById(R.id.medicine_stock);
        btn_addmedicine = findViewById(R.id.btn_addmedicine);
        dosage = findViewById(R.id.dosage);
        AddImage = findViewById(R.id.AddImage);
        btn_browse_image = findViewById(R.id.btnBrowseImage);

        radioGroup = findViewById(R.id.radiogrp);
        radiotype = findViewById(R.id.radiotype);

        btn_addmedicine.setOnClickListener(this);

        btn_browse_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
            }
        });



        db_medicine = FirebaseDatabase.getInstance().getReference("Medicine_List");
        db_sales = FirebaseDatabase.getInstance().getReference("Sales");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                AddImage.setImageBitmap(bm);
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
        if (view == btn_addmedicine) {

            selectedID= radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(selectedID);

            typeID = radiotype.getCheckedRadioButtonId();
            buttontype = findViewById(typeID);

            txtcategory = radioButton.getText().toString();
            txttype = buttontype.getText().toString();
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

                        String id = txt_medicine_id.getText().toString().trim();
                        String name = txt_medicine_name.getText().toString().trim();
                        String price = txt_medicine_price.getText().toString().trim();
                        String desc = txt_medicine_desc.getText().toString().trim();
                        String stock = txt_medicine_stock.getText().toString().trim();
                        String btn_dosage = dosage.getText().toString().trim();

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

                            String med_id = db_medicine.push().getKey();
                            String sales_number = "0";
                            Medicine_Information info = new Medicine_Information(med_id, id, name, price, desc, stock, txtcategory, btn_dosage, txttype, name, taskSnapshot.getDownloadUrl().toString(), stock_status);
                            Sales sales = new Sales(med_id, id, name, stock, stock_status, sales_number);
                            db_medicine.child(med_id).setValue(info);
                            db_sales.child(med_id).setValue(sales);

                            Toast.makeText(getApplicationContext(), "Medicine Added!", Toast.LENGTH_SHORT).show();
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
                                dialog.setMessage("Uploading");
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please select image to upload", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
