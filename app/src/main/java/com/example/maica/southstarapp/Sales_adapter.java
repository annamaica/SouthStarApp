package com.example.maica.southstarapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.maica.southstarapp.OrderList;
import com.example.maica.southstarapp.R;
import com.example.maica.southstarapp.Sales;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Maica on 2/15/2018.
 */

public class Sales_adapter extends ArrayAdapter<Sales> {

    private Context context;
    private int resource;
    private List<Sales> saleslist;

    NotificationCompat.Builder notification1;
    private static final int uniqueID = 45612;

    String user_id;
    FirebaseAuth auth;

    DatabaseReference db_notif = FirebaseDatabase.getInstance().getReference("Push_Notification");

    public Sales_adapter(Context context, int resource, List<Sales> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        saleslist = objects;

        notification1 = new NotificationCompat.Builder(getContext());
        notification1.setAutoCancel(true);

        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.sales_layout, parent, false);
        TextView sales_id = row.findViewById(R.id.salesid);
        TextView sales_code = row.findViewById(R.id.sales_code);
        TextView sales_name = row.findViewById(R.id.sales_name);
        TextView sales_quantity = row.findViewById(R.id.sales_stock);
        TextView sales_status = row.findViewById(R.id.sales_status);
        TextView sales_number = row.findViewById(R.id.sales_number);


        String id = saleslist.get(position).getMed_id();
        sales_id.setText(id);

        String code = saleslist.get(position).getMed_code();
        sales_code.setText(code);

        String name = saleslist.get(position).getMed_name();
        sales_name.setText(name);

        String quantity = saleslist.get(position).getMed_quantity();
        sales_quantity.setText(quantity);

        String status = saleslist.get(position).getMed_status();
        sales_status.setText(status);

        String number = saleslist.get(position).getMed_sales();
        sales_number.setText(number);

//        if (saleslist.get(position).getMed_status().equals("Critical")){
//
////            //save data to notif
////
////            user_id = auth.getCurrentUser().getUid();
////
////            PushNotification notif = new PushNotification(code, user_id, "There are " + quantity + " quantity left \n of medicine that is in CRITICAL CONDITION.");
////            String notif_id = db_notif.push().getKey();
////
////            db_notif.child(notif_id).setValue(notif);
//
//            int incid = uniqueID + 1;
//
//            notification1.setSmallIcon(R.drawable.logo);
//            notification1.setWhen(System.currentTimeMillis());
//            notification1.setContentTitle("South Star Drug");
//            notification1.setContentText("There are " + quantity + " quantity left " + "\n" +"of medicine that is in CRITICAL CONDITION.");
//
//            Intent intent2 = new Intent(getContext(), SalesModule.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//            notification1.setContentIntent(pendingIntent);
//
//            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//            nm.notify(incid, notification1.build());
//        }
//
//        else if (saleslist.get(position).getMed_status().equals("Out of Stock")){
//
////            //save data to notif
////
////            user_id = auth.getCurrentUser().getUid();
////
////            PushNotification notif = new PushNotification(code, user_id, "There are "+ quantity + " quantity left \n of medicine that is Out of Stock.");
////            String notif_id = db_notif.push().getKey();
////
////            db_notif.child(notif_id).setValue(notif);
//
//            int incid = uniqueID + 1;
//
//            notification1.setSmallIcon(R.drawable.logo);
//            notification1.setWhen(System.currentTimeMillis());
//            notification1.setContentTitle("South Star Drug");
//            notification1.setContentText("There are " + quantity + " quantity left of medicine that is Out of Stock.");
//
//            Intent intent2 = new Intent(getContext(), SalesModule.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//            notification1.setContentIntent(pendingIntent);
//
//            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//            nm.notify(incid, notification1.build());
//        }

        return row;
    }

}
