package com.example.apasca.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apasca.chatapplication.constants.ApiUrls;
import com.example.apasca.chatapplication.utils.VolleyCallback;
import com.example.apasca.chatapplication.utils.VolleyRequest;
import com.example.apasca.chatapplication.utils.VolleySingleton;
import com.fasterxml.jackson.core.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.entry );
        Button entryButton = findViewById( R.id.buttonEntra );
        EditText username = findViewById( R.id.editTextUsername );
        EditText password = findViewById( R.id.editTextPass );
        VolleyRequest vr = new VolleyRequest( this );
        Intent intent = new Intent( getBaseContext(), ChatActivity.class );
        entryButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View main) {
                if (username.getText().toString().equals( "" ) || username.getText().toString().trim().equals( "" ) || password.getText().toString().equals( "" ) || password.getText().toString().trim().equals( "" )) {
                    alertView( "Campo Username o Password Vuoti" );
                } else {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put( "username", username.getText().toString() );
                    params.put( "password", password.getText().toString() );
                    vr.getResponse( Request.Method.POST, ApiUrls.ApiPostLoginUrl, new JSONObject( params ),
                            new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    try {
                                        JSONObject response = new JSONObject( result );
                                        Log.d( "RESPONSE in string", result );
                                        // do your work with response object
                                        intent.putExtra( "USERNAME", username.getText().toString().trim() );
                                        startActivity( intent );

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );
                }
            }
        } );
    }

    private void alertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle( "Errore" )
                .setMessage( message )
                .show();
    }
}
