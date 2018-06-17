package com.wanna.network;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wanna.network.handler.LoginHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginRequest {

    private static final String URL = ApplicationConstantsRequest.LOGIN;

    public static void excecute(Context context, final LoginHandler handler, String facebookId, String token, String email, String name, ArrayList<String> friendsIds, String regId) {

        // Mapeo de los pares clave-valor
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("id_user", facebookId);
        parametros.put("accesskey", "1023045adlkjwo4uosflkjf4w090");
        parametros.put("tok", token);
        parametros.put("email", email);
        parametros.put("name", name);
        parametros.put("regId", regId);
        parametros.put("id_friends", facebookFriends(friendsIds));
        JSONObject json = new JSONObject(parametros);

        JsonObjectRequest request = new JsonObjectRequest(URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handler.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.onFail(error);
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);

    }


    private static String facebookFriends(ArrayList<String> friends) {

        String result = "";
        for (int i = 0; i < friends.size(); i++) {
            result += friends.get(i) + ",";
        }

        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

}


