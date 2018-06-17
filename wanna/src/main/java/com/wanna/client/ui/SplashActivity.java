package com.wanna.client.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.wanna.client.R;
import com.wanna.client.ui.custom.MyAnimationView;
import com.wanna.gcm.ApplicationConstants;
import com.wanna.network.LoginRequest;
import com.wanna.network.handler.LoginHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

//import android.view.View;
//import android.widget.Button;

public class SplashActivity extends Activity implements LoginHandler {

    private static String TAG = SplashActivity.class.getName();
    private static long SLEEP_TIME = 3;    // Tiempo de inactividad
    private long startTime;
    //Gcm
    private GoogleCloudMessaging gcmObj;
    private String regId = "";
    Context applicationContext;
    private boolean onfaill=true;
    private String idUser;


    /**
     * Lo llamamos cuando creamos la primer actividad
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splashwanna);

        applicationContext = this;

        MyAnimationView anim_view = (MyAnimationView) this.findViewById(R.id.anim_view);
        anim_view.loadAnimation("wanna_annimation", 29, false, 58);

        // Iniciar temporizador y poner en marcha la actividad principal

        Session session = Session.getActiveSession();
        if (session == null) {
            session = Session.openActiveSessionFromCache(SplashActivity.this);
        }


        if (session != null && session.isOpened()) {
            MainApplication.session = session;
//gcm
            registerInBackground();
            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                                Response response) {
                            // If the response is successful
                            if (MainApplication.session == Session.getActiveSession()) {
                                if (user != null) {
                                    MainApplication.facebookuser = user;
                                    SplashActivity.this.requestFacebookFriends(MainApplication.session);
                                } else {
                                    redirectToHomeScreen();
                                }
                            }
                        }
                    });
            Request.executeBatchAsync(request);

        } else {
            Long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime <= 2000) {
                try {
                    Thread.sleep(2000 - elapsedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                // lanzar main activity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();

        }

        AsyncTask test1 = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                SplashActivity.this.findViewById(R.id.spinner_login).setVisibility(View.VISIBLE);
            }
        };
        test1.execute();
        startTime = anim_view.startPlaying();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (onfaill) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    SplashActivity.this.finish();
                }
            }

        }, 9000);

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
                    // Toast.makeText(applicationContext, "Registered with GCM Server successfully.\n\n" + msg, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(applicationContext, "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."+ msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }


    private void redirectToHomeScreen() {
        final long elapsedTime = System.currentTimeMillis() - startTime;
        AsyncTask waitLogo = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                if (elapsedTime <= 2000) {
                    try {
                        Thread.sleep(2000 - elapsedTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if(!onfaill) {

                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    SplashActivity.this.finish();
                }
            }
        };
        waitLogo.execute();
    }





    @Override
    public void onSuccess(JSONObject response) {
        onfaill=false;
        redirectToHomeScreen();

    }

    @Override
    public void onFail(VolleyError error) {
        redirectToHomeScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    /*
        Button play_button = (Button) this.findViewById(R.id.play_button);
        
        final MyAnimationView f_anim_view = anim_view; //para evitar la advertencia, puede estar equivocado
        play_button.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) 
			{
				f_anim_view.playAnimation();
			}
		});*/


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

                        LoginRequest.excecute(SplashActivity.this.getApplicationContext(), SplashActivity.this, MainApplication.facebookuser.getId(), MainApplication.session.getAccessToken(), "", MainApplication.facebookuser.getName(), facebookFriends, regId);


                    }


                });
    }


}
