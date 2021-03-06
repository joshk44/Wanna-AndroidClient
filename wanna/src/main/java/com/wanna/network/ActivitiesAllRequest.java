package com.wanna.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wanna.data.ArrayActivityAll;
import com.wanna.network.handler.ActivityAllHandler;


/**
 * Created by fede on 05/05/15.
 */
public class ActivitiesAllRequest {
    static String URL = "";
    static String Parametros = "";


    public static void excecute(Context context, final ActivityAllHandler handler, String accesskey, String userid) {

        URL = ApplicationConstantsRequest.ACTIVITIESALL;
        Parametros = "accesskey=" + accesskey + "&user_id=" + userid;


        WannaVolleySingleton.getInstance(context).addToRequestQueue(


                new GsonRequest<ArrayActivityAll>(URL + Parametros, ArrayActivityAll.class,
                        new Response.Listener<ArrayActivityAll>() {
                            @Override
                            public void onResponse(ArrayActivityAll response) {
                                handler.onActivitiesResponse(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", error.toString());
                                handler.onFail(error);

                            }
                        }
                )

        );

    }
}
