package com.wanna.client.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.wanna.client.R;
import com.wanna.gcm.ApplicationConstants;
import com.wanna.network.LoginRequest;
import com.wanna.network.handler.LoginHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment implements LoginHandler {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private UiLifecycleHelper uiHelper;

    private final List<String> permissions;

    private boolean isOnStateChange = false;

    public LoginFragment() {
        permissions = Arrays.asList("email", "user_friends", "user_likes");
    }

    private GoogleCloudMessaging gcmObj;
    private String regId = "";
    Context applicationContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        applicationContext = getActivity().getApplicationContext();
//gcm
        registerInBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main, container, false);

        LoginButton authButton = (LoginButton) view
                .findViewById(R.id.authButton);
        authButton.setReadPermissions(permissions);
        authButton.setFragment(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(final Session session, SessionState state,
                                      Exception exception) {

        if (isOnStateChange)
            return;

        if (state.isOpened()) {
            isOnStateChange = true;
            MainApplication.session = session;
            Log.i(TAG, "Logged in...");

            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                                Response response) {
                            // If the response is successful
                            if (MainApplication.session == Session.getActiveSession()) {
                                if (user != null) {
                                    MainApplication.facebookuser = user;
                                }
                            }
                            LoginFragment.this.requestFacebookFriends(session);
                        }
                    });
            Request.executeBatchAsync(request);


        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onSuccess(JSONObject response) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userID", MainApplication.facebookuser.getId());
        editor.putString("IDName", MainApplication.facebookuser.getName());
        editor.commit();

        startActivity(new Intent(this.getActivity(), HomeActivity.class));
        this.getActivity().finish();
        isOnStateChange = false;
    }


    @Override
    public void onFail(VolleyError error) {
        //Definir error.
        Log.i(TAG, error.toString());
    }


    public void requestFacebookFriends(Session session) {
        Request.executeMyFriendsRequestAsync(session,
                new Request.GraphUserListCallback() {
                    @Override
                    public void onCompleted(List<GraphUser> users,
                                            Response response) {

                        ArrayList facebookFriends = new ArrayList<String>();
                        if (users != null) {
                            for (int i = 0; i < users.size(); i++) {
                                facebookFriends.add(users.get(i).getId());
                                MainApplication.facebookFriends.put(users.get(i).getId(), users.get(i));
                            }
                        }
                        LoginRequest.excecute(LoginFragment.this.getActivity().getApplicationContext(), LoginFragment.this, MainApplication.facebookuser.getId(), MainApplication.session.getAccessToken(), "", MainApplication.facebookuser.getName(), facebookFriends, regId);
                    }
                });
    }

    // AsyncTask to register Device in GCM Server
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    //Toast.makeText(applicationContext, "Registered with GCM Server successfully.\n\n" + msg, Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(applicationContext, "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time." + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
}
