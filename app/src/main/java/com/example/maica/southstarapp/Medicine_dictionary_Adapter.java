package com.example.maica.southstarapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Maica on 2/20/2018.
 */

public class Medicine_dictionary_Adapter extends ArrayAdapter<Medicine_Information> {

    private Context context;
    private int resource;
    private List<Medicine_Information> orderlist;

    public Medicine_dictionary_Adapter(Context context, int resource, List<Medicine_Information> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        orderlist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.medicine_dictionary_layout, parent, false);
        TextView med_name = row.findViewById(R.id.med_name);
        TextView med_type = row.findViewById(R.id.med_type);
        TextView med_id = row.findViewById(R.id.med_id);

        String medicine_name = orderlist.get(position).getMedicine_name();
        med_name.setText(medicine_name);

        String medicine_type = " (" +orderlist.get(position).getMedicine_type() + ")";
        med_type.setText(medicine_type);

        String id = orderlist.get(position).getMedicine_id();
        med_id.setText(id);

        return row;
    }
}
