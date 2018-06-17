package com.wanna.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wanna.data.ArrayCommnets;
import com.wanna.data.ArrayUser;
import com.wanna.network.handler.CommnetsHandler;
import com.wanna.network.handler.UserHandler;

public class CommentsActivityRequest {
    static String URL = "";
    static String Parametros = "";


    public static void excecute(Context context, final CommnetsHandler handler, String accesskey, String user_id) {

        URL = ApplicationConstantsRequest.COMMENTSACTIVITY;
        Parametros = "accesskey=" + accesskey + "&eventid=" + user_id;


        WannaVolleySingleton.getInstance(context).addToRequestQueue(
                new GsonRequest<ArrayCommnets>(URL + Parametros, ArrayCommnets.class,
                        new Response.Listener<ArrayCommnets>() {
                            @Override
                            public void onResponse(ArrayCommnets response) {
                                handler.onUsersResponse(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", error.toString());

                            }
                        }
                )

        );

    }
}
