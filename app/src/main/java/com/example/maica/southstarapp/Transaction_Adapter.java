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
 * Created by Maica on 2/3/2018.
 */

public class Transaction_Adapter extends ArrayAdapter<OrderList> {
    private Context context;
    private int resource;
    private List<OrderList> transaclist;

    public Transaction_Adapter(Context context, int resource, List<OrderList> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        transaclist = objects;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.transaclist_layout, parent, false);
        TextView transac_id = row.findViewById(R.id.transac);
        TextView transac_user = row.findViewById(R.id.transac1);
        TextView transac_address = row.findViewById(R.id.transac2);
        TextView transac_medicine = row.findViewById(R.id.transac3);
        TextView transac_quantity = row.findViewById(R.id.transac4);
        TextView transac_total = row.findViewById(R.id.transac7);
        TextView transac_date = row.findViewById(R.id.transac5);
        TextView transac_status = row.findViewById(R.id.transac6);


        String id = transaclist.get(position).getOrder_id();
        transac_id.setText(id);

        String user = transaclist.get(position).getOrder_user();
        transac_user.setText(user);

        String address = transaclist.get(position).getOrder_address();
        transac_address.setText(address);

        String medicine = transaclist.get(position).getOrder_medicine_code();
        transac_medicine.setText(medicine);

        String quantity = transaclist.get(position).getOrder_quantity();
        transac_quantity.setText(quantity);

        String price = "₱" + transaclist.get(position).getOrder_total();
        transac_total.setText(price);

        String date = transaclist.get(position).getOrder_date();
        transac_date.setText(date);

        String status = transaclist.get(position).getOrder_status();
        transac_status.setText(status);

        return row;
    }
}
