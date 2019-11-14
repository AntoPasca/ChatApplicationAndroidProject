package com.example.apasca.chatapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.apasca.chatapplication.dto.PubSubPojo;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.Arrays;

import tech.gusavila92.websocketclient.WebSocketClient;

public class ChatActivity extends AppCompatActivity {

    private WebSocketClient webSocketClient;
    PubNub pubnub;
    private String username;
    private PubSubListAdapter pubSubListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {this.getSupportActionBar().hide();}
        catch (NullPointerException e){}
        this.username = getIntent().getStringExtra("USERNAME");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity );
//        createWebSocketClient();
        initPubNub();

        //GESTIONE NOTIFICA
        NotificationManager notificationManager =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);

        String channelId = "1";
        String channel2 = "2";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Channel 1",NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("This is BNT");
            notificationChannel.setLightColor( Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel2 = new NotificationChannel(channel2,
                    "Channel 2",NotificationManager.IMPORTANCE_MIN);

            notificationChannel.setDescription("This is bTV");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel2);

        }
    }

    public void initPubNub(){
        ArrayList<PubSubPojo> messageList = new ArrayList<>();
        ListView messageListView = findViewById( R.id.message_list );
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-8735bef8-0689-4237-b212-3df217beaefa"); // REPLACE with your pub key
        pnConfiguration.setSubscribeKey("sub-c-fab05640-046e-11ea-a149-ead0b8c5d242"); // REPLACE with your sub key

        pnConfiguration.setUuid( this.username ).setSecure(true);
        pubnub = new PubNub(pnConfiguration);
        // Listen to messages that arrive on the channel


        Button sendButton = (Button) findViewById( R.id.sendButton );
        sendButton.setOnClickListener(new View.OnClickListener() {
            EditText text = findViewById( R.id.editText );
            @Override
            public void onClick(View main) {
                publishMessage( text.getText().toString().replace( "\"","" ) );
                text.setText( "" );
            }
        });
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pub, PNStatus status) {
            }
            @Override
            public void message(PubNub pub, final PNMessageResult message) {
                // Replace double quotes with a blank space
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            // Display the message on the app
                            pubSubListAdapter.add(new PubSubPojo(message.getPublisher().toString(), message.getMessage().toString().replace( "\"","" )) );
                            messageListView.setSelection(pubSubListAdapter.getCount() - 1);
//                            messageList.add(new PubSubPojo(message.getPublisher().toString(), message.getMessage().toString().replace( "\"","" )) );
                        } catch (Exception e){
                            System.out.println("Error");
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void presence(PubNub pub, PNPresenceEventResult presence) {
            }
        });
        // Subscribe to the global channel
        pubnub.subscribe()
                .channels( Arrays.asList("public"))
                .execute();

        pubnub.history()
                .channel("public")
                .count(10)
                .includeTimetoken(true)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        if (!status.isError()) {
                            for (PNHistoryItemResult historyItem : result.getMessages()) {
                                System.out.println("Message content: " + historyItem.getEntry());
                                messageList.add(new PubSubPojo("history", historyItem.getEntry().toString().replace( "\"","" )) );
                                pubSubListAdapter.notifyDataSetChanged();
                            }
                            System.out.println("Start timetoken: " + result.getStartTimetoken());
                            System.out.println("End timetoken: " + result.getEndTimetoken());
                        } else {
                            status.getErrorData().getThrowable().printStackTrace();
                        }
                    }
                });
        pubSubListAdapter = new PubSubListAdapter( this,messageList , username);
        messageListView.setAdapter( pubSubListAdapter );
        pubnub.addPushNotificationsOnChannels();
    }


    public void publishMessage(String message){
        // Publish message to the global chanel
        pubnub.publish()
                .message(message)
                .channel("public")
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        pubSubListAdapter.notifyDataSetChanged();
                        // status.isError() to see if error happened and print status code if error
                        if(status.isError()) {
                            System.out.println("pub status code: " + status.getStatusCode());
                        }
                    }
                });
    }


}
