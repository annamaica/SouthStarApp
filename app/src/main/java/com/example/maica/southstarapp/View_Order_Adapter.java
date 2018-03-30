package com.example.maica.southstarapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Maica on 1/31/2018.
 */

public class View_Order_Adapter extends ArrayAdapter<Cart_info> {
    private Context context;
    private int resource;
    private List<Cart_info> pendinglist;


    public View_Order_Adapter(Context context, int resource, List<Cart_info> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.pendinglist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.user_cart, parent, false);
        TextView pendingorderid = row.findViewById(R.id.order_id);
        final TextView pendingname = row.findViewById(R.id.order_name);
        final TextView pendingmedcode = row.findViewById(R.id.order_medcode);
        TextView pendingquan = row.findViewById(R.id.order_quan);
        final TextView pendingtotal = row.findViewById(R.id.order_total);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Medicine_List");

        String medid = pendinglist.get(position).getOrder_medicine_id();

        Query search = db.orderByChild("medicine_id").equalTo(medid);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine_Information medinfo = snapshot.getValue(Medicine_Information.class);
                        try {
                            pendingname.setText("Medicine Name: "+ medinfo.getMedicine_name());
                            pendingmedcode.setText("Medicine Code: " + medinfo.getMedicine_code());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String orderid = pendinglist.get(position).getOrder_id();
        pendingorderid.setText(orderid);

        String quantity = pendinglist.get(position).getOrder_medicine_quantity();
        pendingquan.setText("Medicine Quantity: "+quantity);

        String total = pendinglist.get(position).getOrder_total();
        pendingtotal.setText("Price: ₱"+total);

        return row;
    }
}
