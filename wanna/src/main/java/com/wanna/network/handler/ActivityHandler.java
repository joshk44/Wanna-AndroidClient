package com.wanna.network.handler;

import com.wanna.data.ArrayActivityAll;


public interface ActivityHandler extends BaseHandler {

    public void onActivitiesResponse(ArrayActivityAll activities);
}
