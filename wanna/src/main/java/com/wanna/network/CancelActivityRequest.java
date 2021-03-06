package com.wanna.network;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wanna.network.handler.BaseHandler;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by fede on 05/05/15.
 */
public class CancelActivityRequest {
    private static final String URL = ApplicationConstantsRequest.CANCELACTIVITY;

    public static void excecute(Context context, final BaseHandler handler, String facebookId, String idActivity) {

        // Mapeo de los pares clave-valor
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("id_user", facebookId);
        parametros.put("accesskey", "1023045adlkjwo4uosflkjf4w090");
        parametros.put("id_activity", idActivity);
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
}