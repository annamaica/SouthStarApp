package com.example.maica.southstarapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by Maica on 3/1/2018.
 */

public class MessageAdapter extends ArrayAdapter<ChatMessage> {

    private Context context;
    private int resource;
    private List<ChatMessage> msgList;

    public MessageAdapter(Context context, int resource, List<ChatMessage> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        msgList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.list_item,parent,false);


        TextView messageText = row.findViewById(R.id.message_text);
        TextView messageTime = row.findViewById(R.id.message_time);
        TextView messageUser = row.findViewById(R.id.message_user);

        messageText.setText(msgList.get(position).getMessageText());
        messageUser.setText(msgList.get(position).getMessageUser());
        messageTime.setText(DateFormat.format("dd-MM-yyyy (h:mm a)", msgList.get(position).getMessageTime()));

        return row;
    }
}
