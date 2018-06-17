package com.wanna.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wanna.network.handler.NewActivityHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class NewActivityRequest {

    private static final String URL = ApplicationConstantsRequest.NEWACTIVITY;

    public static void excecute(Context context, final NewActivityHandler handler, String title, String description, String creator, String location, Long datetime, Long datetimeCreation, String quota, String icon, String userName, ArrayList<String> friends) {

        // Mapeo de los pares clave-valor
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("title", title);
        parametros.put("accesskey", "1023045adlkjwo4uosflkjf4w090");
        parametros.put("creator", creator);
        parametros.put("description", description);
        parametros.put("location", location);
        parametros.put("datetime", "" + datetime);
        parametros.put("datetime_creation", "" + datetimeCreation);
        parametros.put("quota", quota);
        parametros.put("icon", icon);
        parametros.put("name", userName);
        parametros.put("id_friends", facebookFriends(friends));

        JSONObject json = new JSONObject(parametros);

        JsonObjectRequest request = new JsonObjectRequest(URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handler.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                handler.onFail(error);
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);

    }


    private static String facebookFriends(ArrayList<String> friends) {

        String result = "";
        if (friends != null) {
            for (int i = 0; i < friends.size(); i++) {
                result += friends.get(i) + ",";
            }

            if (result.length() > 0) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

}


