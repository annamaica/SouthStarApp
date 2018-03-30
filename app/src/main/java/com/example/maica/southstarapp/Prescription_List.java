package com.example.maica.southstarapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Maica on 2/3/2018.
 */

public class Prescription_List extends ArrayAdapter<PrescriptionAdapter> {
    private Context context;
    private int resource;
    private List<PrescriptionAdapter> prescriptionlist;

    public Prescription_List(Context context, int resource, List<PrescriptionAdapter> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        prescriptionlist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.prescription_layout, parent, false);
        ImageView pic = row.findViewById(R.id.prescrip_image);
        TextView pic_id = row.findViewById(R.id.prescrip_id);
        TextView pic_user = row.findViewById(R.id.prescrip_user);
        TextView pic_status = row.findViewById(R.id.prescrip_status);
        TextView pic_userid = row.findViewById(R.id.prescrip_userid);


        String id = prescriptionlist.get(position).getPrescription_id();
        pic_id.setText(id);

        String userid = prescriptionlist.get(position).getPrescription_userid();
        pic_userid.setText(userid);

        String user = "Name: " + prescriptionlist.get(position).getPrescription_user();
        pic_user.setText(user);

        String status = "Status: " + prescriptionlist.get(position).getPrescription_status();
        pic_status.setText(status);

        Glide.with(getContext()).load(prescriptionlist.get(position).getUrl()).into(pic);
        return row;
    }
}
