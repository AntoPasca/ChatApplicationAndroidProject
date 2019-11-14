package com.example.apasca.chatapplication.utils;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apasca.chatapplication.constants.ApiUrls;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class VolleyRequest extends Application {

    private Context context;

    public VolleyRequest(Context context){
        this.context = context;
    }

    public void getResponse(int method, String url, JSONObject jsonValue, final VolleyCallback callback) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest( method, url, jsonValue, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccessResponse(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toast.makeText(context, e + "error", Toast.LENGTH_LONG).show();
            }
        })
        {
//            // set headers
//            @Override
//            public Map< String, String > getHeaders() throws com.android.volley.AuthFailureError {
//                Map < String, String > params = new HashMap < String, String > ();
//                params.put("Authorization: Basic", TOKEN);
//                return params;
//            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

}

