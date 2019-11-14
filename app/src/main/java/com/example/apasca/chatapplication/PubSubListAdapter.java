package com.example.apasca.chatapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.apasca.chatapplication.dto.PubSubPojo;

import java.util.ArrayList;
import java.util.List;

public class PubSubListAdapter extends ArrayAdapter<PubSubPojo> {
    private Context context;
    private List<PubSubPojo> values = new ArrayList<>();
    private String username;

    @Override
    public void add(PubSubPojo message) {
        this.values.add(message);
        ((Activity) this.context).runOnUiThread( new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();

            }
        });
    }

    public PubSubListAdapter(@NonNull Context context, ArrayList<PubSubPojo> list, String username) {
        super(context, 0 , list);
        this.context = context;
        this.username = username;
        values = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,@NonNull ViewGroup parent) {
        View listItem = convertView;
        PubSubPojo message = values.get(position);
        if (listItem == null && message.getSender().equals(this.username)){
            listItem = LayoutInflater.from(context).inflate(R.layout.item_message_send, parent, false);
        }
        if (listItem == null && !message.getSender().equals(this.username)){
            listItem = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
        }
        if (listItem != null && message.getSender().equals(this.username)){
            listItem = LayoutInflater.from(context).inflate(R.layout.item_message_send, parent, false);
        }
        if (listItem != null && !message.getSender().equals(this.username)){
            listItem = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
        }
        TextView sender = listItem.findViewById(R.id.text_message_name);
        sender.setText( message.getSender() );
        TextView body = listItem.findViewById(R.id.text_message_body);
        body.setText( message.getMessage() );
        return listItem;
    }
}