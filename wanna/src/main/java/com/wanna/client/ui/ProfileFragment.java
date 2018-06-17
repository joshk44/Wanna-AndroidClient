package com.wanna.client.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.wanna.client.R;
import com.wanna.client.ui.custom.ProfilePictureView;
import com.wanna.data.ArrayActivityAll;
import com.wanna.network.ActivitiesAllRequest;
import com.wanna.network.handler.ActivityAllHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements ActivityAllHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayActivityAll arrac;
    private ProfileActivityItemAdapter activitiesAdapter;
    private List<ProfileActicityItemInfo> activities = new ArrayList<ProfileActicityItemInfo>();
    RecyclerView activitiesRecList;

    private FriendsListAdapter friendsAdapter;
    private List<FriendItemInfo> friends = new ArrayList<FriendItemInfo>();
    RecyclerView friendsRecList;

    //OFFLINE
    private static final String OFFLINEACTIVITIESPROFILE="offlineActivitiesProfile";
    private static final String OFFLINEACTIVITIESFRIENDS="offlineActivitiesFriends";
    private SharedPreferences prefs;
    private Gson gson;
    private String last;
    private boolean onfaill=true;
    private String idUser;
    private String idname;
    private static DisplayMetrics metrics = new DisplayMetrics();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        onfaill=true;
        last = "";
        gson = new Gson();
        prefs = getActivity().getSharedPreferences("UserLoginWANNA",
                Context.MODE_PRIVATE);
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        idUser = preferences.getString("userID", null);
        idname = preferences.getString("IDName",null);

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ProfilePictureView profilePictureView = (ProfilePictureView) rootView.findViewById(R.id.my_profile_picture);
        profilePictureView.setProfileId(idUser);
        TextView nameProfile = (TextView) rootView.findViewById(R.id.username);
        nameProfile.setText(idname);
        ActivitiesAllRequest.excecute(this.getActivity().getApplicationContext(), ProfileFragment.this, "1023045adlkjwo4uosflkjf4w090", idUser);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (onfaill) {
                    arrac = gson.fromJson(prefs.getString(OFFLINEACTIVITIESPROFILE, ""), ArrayActivityAll.class);
                    MainApplication.lastActivitiesHome = arrac;
                    refreshActivities();

                }
            }

        }, 7000);

        activitiesRecList = (RecyclerView) rootView.findViewById(R.id.list_activities);
        activitiesRecList.setHasFixedSize(true);
        activitiesRecList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        activitiesRecList.setLayoutManager(llm);
        activitiesAdapter = new ProfileActivityItemAdapter(activities, this.getActivity());
        activitiesRecList.setAdapter(activitiesAdapter);

        friendsRecList = (RecyclerView) rootView.findViewById(R.id.list_friends);
        friendsRecList.setHasFixedSize(true);
        friendsRecList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        friendsRecList.setLayoutManager(layoutManager);
        friendsAdapter = new FriendsListAdapter(friends);
        friendsRecList.setAdapter(friendsAdapter);

        if(MainApplication.facebookFriends != null) {
            refreshFriends();
        }else{
            arrac=null;
        }


        final Button activities = (Button) rootView.findViewById(R.id.button_activities);
        final Button friends = (Button) rootView.findViewById(R.id.button_friends);

        activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activities.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_baseline_orange));
                activities.setTextColor(getResources().getColor(R.color.primary));

                friends.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_baseline));
                friends.setTextColor(getResources().getColor(R.color.font_gray));

                friendsRecList.setVisibility(View.GONE);
                activitiesRecList.setVisibility(View.VISIBLE);

            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activities.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_baseline));
                activities.setTextColor(getResources().getColor(R.color.font_gray));

                friends.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_baseline_orange));
                friends.setTextColor(getResources().getColor(R.color.primary));

                friendsRecList.setVisibility(View.VISIBLE);
                activitiesRecList.setVisibility(View.GONE);
            }
        });


        //VER PARAPANTALLA LDPI
        switch(metrics.densityDpi)
        {
            case DisplayMetrics.DENSITY_HIGH: //HDPI
                break;
            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                break;
            case DisplayMetrics.DENSITY_LOW:  //LDPI
                //profilePictureView.setScaleX(-150);
                break;
        }

        return rootView;

    }

    @Override
    public void onActivitiesResponse(ArrayActivityAll arrayActivityAll) {
        last = gson.toJson(arrayActivityAll);
        onfaill=false;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(OFFLINEACTIVITIESPROFILE,last);
        editor.commit();
        arrac = arrayActivityAll;
        MainApplication.lastActivitiesHome = arrayActivityAll;
        refreshActivities();

    }

    @Override
    public void onSuccess(JSONObject response) {

    }

    @Override
    public void onFail(VolleyError error) {
        //Definir error.
        Log.i("Error", error.toString());

    }


    public void refreshActivities() {
        activities.clear();
        if (arrac != null) {
            for (int x = 0; x < arrac.getItemsE().size(); x++) {
                activities.add(new ProfileActicityItemInfo(arrac.getItemsE().get(x).getCreator(),arrac.getItemsE().get(x).getCreator_name() , arrac.getItemsE().get(x).getTitle(), arrac.getItemsE().get(x).getDatetime(), arrac.getItemsE().get(x).getId(), Integer.parseInt(arrac.getItemsE().get(x).getClaps()), arrac.getItemsE().get(x).getComment(),arrac.getItemsE().get(x).getCreator()));
            }
            activitiesAdapter.notifyDataSetChanged();
        }
    }

    public void refreshFriends() {
        friends.clear();

        List<GraphUser> facebookFriends =
                new ArrayList<GraphUser>(MainApplication.facebookFriends.values());
            for (int x = 0; x < facebookFriends.size(); x++) {
                friends.add(new FriendItemInfo(facebookFriends.get(x).getName(), facebookFriends.get(x).getId()));

        }
        friendsAdapter.notifyDataSetChanged();

    }
}
