package com.wanna.client.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanna.client.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<String> arrac;
    private ProfileActivityItemAdapter activitiesAdapter;
    private List<ProfileActicityItemInfo> activities = new ArrayList<ProfileActicityItemInfo>();
    RecyclerView activitiesRecList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationsFragment() {
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        arrac = new ArrayList<String>();
        Set<String> stored = preferences.getStringSet("notifications", null);
        if (stored != null) {
            arrac.addAll(stored);
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        activitiesRecList = (RecyclerView) rootView.findViewById(R.id.list_activities);
        activitiesRecList.setHasFixedSize(true);
        activitiesRecList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        activitiesRecList.setLayoutManager(llm);
        activitiesAdapter = new ProfileActivityItemAdapter(activities, this.getActivity());
        activitiesRecList.setAdapter(activitiesAdapter);
        refreshActivities();
        return rootView;

    }


    public void refreshActivities() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        arrac = new ArrayList<String>();
        Set<String> stored = preferences.getStringSet("notifications", null);
        if (stored != null) {
            arrac.addAll(stored);
        }

        activities.clear();
        if (arrac != null) {
            for (int x = 0; x < arrac.size(); x++) {
                String activityID = null;
                String name = null;
                String activityName = null;
                try {
                    String notification = arrac.get(x);
                    name = DevuelParse(notification, "user_name");
                    activityID = DevuelParse(notification, "id_activity");
                    activityName = DevuelParse(notification, "activity_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                activities.add(new ProfileActicityItemInfo("", name, activityName, "", activityID,0,0,""));
            }
            activitiesAdapter.notifyDataSetChanged();
        }
    }

    //Lo agrego para parsear datos el metodo DevuelParse, le paso el json lo convierto en jsonObjen y obtengo los datos
    public String DevuelParse(String TojsonObj, String buscar) throws JSONException {
        String text = "";

        JSONObject jsonObj = new JSONObject(TojsonObj);
        text = jsonObj.getString(buscar);

        return text;
    }

}
