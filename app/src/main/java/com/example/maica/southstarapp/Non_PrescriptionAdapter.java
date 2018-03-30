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
 * Created by Maica on 1/24/2018.
 */

public class Non_PrescriptionAdapter extends ArrayAdapter<Order_Info> {
    private Context context;
    private int resource;
    private List<Order_Info> orderlist;

    public Non_PrescriptionAdapter(Context context, int resource, List<Order_Info> objects){
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
        row = layoutInflater.inflate(R.layout.nonprescription_layout, parent, false);
        TextView order_id = row.findViewById(R.id.order);
        TextView order_user = row.findViewById(R.id.order1);
        TextView order_address = row.findViewById(R.id.order2);
        TextView order_medicine = row.findViewById(R.id.order3);
        TextView order_quantity = row.findViewById(R.id.order4);
        TextView order_date = row.findViewById(R.id.order5);
        TextView order_status = row.findViewById(R.id.order6);
        TextView order_total = row.findViewById(R.id.order7);
        TextView order_category = row.findViewById(R.id.order8);
        TextView order_medname = row.findViewById(R.id.order9);


        String id = orderlist.get(position).getOrder_id();
        order_id.setText(id);

        String user = orderlist.get(position).getOrder_user();
        order_user.setText(user);

        String address = orderlist.get(position).getOrder_address();
        order_address.setText(address);

        String medicine = orderlist.get(position).getOrder_medicine_code();
        order_medicine.setText(medicine);

        String medicine_name =  orderlist.get(position).getOrder_medicine_name();
        order_medname.setText(medicine_name);

        String quantity = orderlist.get(position).getOrder_quantity();
        order_quantity.setText(quantity);

        String price = orderlist.get(position).getOrder_total();
        order_total.setText(price);

        String date = orderlist.get(position).getOrder_date();
        order_date.setText(date);

        String status =  orderlist.get(position).getOrder_status();
        order_status.setText(status);

        String category = orderlist.get(position).getOrder_category();
        order_category.setText(category);

        return row;
    }
}
