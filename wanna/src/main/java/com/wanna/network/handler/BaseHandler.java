package com.wanna.network.handler;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by josh on 13/4/15.
 */
public interface BaseHandler {

    public void onSuccess(JSONObject response);

    public void onFail(VolleyError error);
}
