package com.example.maica.southstarapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Maica on 1/17/2018.
 */

public class MedicineList_Adapter extends ArrayAdapter<Medicine_Information>{
    private Context context;
    private int resource;
    private List<Medicine_Information> medicinelist;

    public MedicineList_Adapter(Context context, int resource, List<Medicine_Information> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        medicinelist = objects;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.medicinelist_layout, parent, false);
        TextView medname =  row.findViewById(R.id.textView1);
        TextView medid =  row.findViewById(R.id.textView2);
        TextView medprice = row.findViewById(R.id.textView3);
        TextView medkey = row.findViewById(R.id.textView4);
        TextView medcategory = row.findViewById(R.id.textView5);
        ImageView image = row.findViewById(R.id.medicineImage);

        String name = "Name: "+ medicinelist.get(position).getMedicine_name();
        medname.setText(name);

        String id = "Code: "+ medicinelist.get(position).getMedicine_code();
        medid.setText(id);

        String price = "Price: ₱" + medicinelist.get(position).getMedicine_price();
        medprice.setText(price);

        String category = "Category: " + medicinelist.get(position).getMedicine_category();
        medcategory.setText(category);

        String key = medicinelist.get(position).getMedicine_id();
        medkey.setText(key);

        Glide.with(getContext()).load(medicinelist.get(position).getUrl()).into(image);
        return row;
    }
}

