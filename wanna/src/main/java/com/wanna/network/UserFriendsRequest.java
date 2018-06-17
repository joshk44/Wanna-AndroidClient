package com.wanna.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wanna.data.ArrayUser;
import com.wanna.network.handler.UserHandler;

public class UserFriendsRequest {
    static String URL = "";
    static String Parametros = "";


    public static void excecute(Context context, final UserHandler handler, String accesskey, String user_id) {

        URL = ApplicationConstantsRequest.USERFRIENDS;
        Parametros = "accesskey=" + accesskey + "&user_id=" + user_id;


        WannaVolleySingleton.getInstance(context).addToRequestQueue(
                new GsonRequest<ArrayUser>(URL + Parametros, ArrayUser.class,
                        new Response.Listener<ArrayUser>() {
                            @Override
                            public void onResponse(ArrayUser response) {
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
