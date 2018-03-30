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
 * Created by Maica on 3/2/2018.
 */

public class User_Adapter extends ArrayAdapter<UserObject> {

    private Context context;
    private int resource;
    private List<UserObject> userlist;

    public User_Adapter(Context context, int resource, List<UserObject> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        userlist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.user_list, parent, false);
        TextView user_id = row.findViewById(R.id.user_id);
        TextView user_name = row.findViewById(R.id.user_name);

        String id = userlist.get(position).getUserID();
        String usertype = userlist.get(position).getUser_type();
        user_id.setText(id);

        String fname = userlist.get(position).getFirst_name();
        String lname = userlist.get(position).getLast_name();

        user_name.setText(fname + " " + lname + " (" + usertype+ ")");

        return row;
    }
}
