package com.wanna.network.handler;

import com.wanna.data.ArrayUser;


public interface UserHandler extends BaseHandler {

    public void onUsersResponse(ArrayUser users);
}
