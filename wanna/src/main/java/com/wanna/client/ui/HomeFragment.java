package com.wanna.client.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.wanna.client.R;
import com.wanna.data.ArrayActivityAll;
import com.wanna.gcm.ApplicationConstants;
import com.wanna.network.ActivitiesAllRequest;
import com.wanna.network.VersionAppRequest;
import com.wanna.network.handler.ActivityAllHandler;
import com.wanna.network.handler.BaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ActivityAllHandler, BaseHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayActivityAll arrac;
    private List<ActicityItemInfo> activities = new ArrayList<>();
    private ActivityItemAdapter adapter;
    LinearLayout tooQuietAdvice;
    RecyclerView recList;
    View loading;


    //OFFLINE
    private static final String OFFLINEACTIVITIES="offlineActivities";
    private SharedPreferences prefs;
    private Gson gson;
    private String last,temp;
    private boolean onfaill=true;
    SharedPreferences preferences;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param param1
     *     Parameter 1.
     * @param param2
     *     Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarActivity activity = (ActionBarActivity) this.getActivity();
        adapter = new ActivityItemAdapter(activities, HomeFragment.this);
        if (MainApplication.facebookuser != null) {
            ActivitiesAllRequest
                .excecute(this.getActivity().getApplicationContext(), HomeFragment.this,
                        "1023045adlkjwo4uosflkjf4w090", MainApplication.facebookuser.getId());
            //VERSION
            VersionAppRequest.excecute(this.getActivity().getApplicationContext(), HomeFragment.this, ApplicationConstants.IDAPPVS);


            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (onfaill) {
                        arrac = gson.fromJson(prefs.getString(OFFLINEACTIVITIES, ""), ArrayActivityAll.class);
                        MainApplication.lastActivitiesHome = arrac;
                        refreshActivities();
                    }
                }

            }, 7000);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (onfaill) {
                    arrac = gson.fromJson(prefs.getString(OFFLINEACTIVITIES, ""), ArrayActivityAll.class);
                    MainApplication.lastActivitiesHome =arrac;
                    refreshActivities();
                }
            }

        }, 7000);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onfaill=true;
        last = "";
        gson = new Gson();
        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        prefs = getActivity().getSharedPreferences("UserLogin",
                Context.MODE_PRIVATE);

        if(MainApplication.facebookuser!=null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userID", MainApplication.facebookuser.getId());
            editor.putString("IDName", MainApplication.facebookuser.getName());
            editor.commit();
        }

        //Pregunto si tenemos el id o nombre
        if(preferences.getString("userID","").equals("")&&preferences.getString("IDName","").equals("")){
            CustomDialogFragment myDiag =  CustomDialogFragment.newInstance(
                    CustomDialogFragment.SERVER_ERROR_LOGIN, HomeFragment.this.getActivity());
            myDiag.show(HomeFragment.this.getActivity().getFragmentManager(), "Diag");

        }


        View activityView = inflater.inflate(R.layout.fragment_home, container, false);
        tooQuietAdvice = (LinearLayout) activityView.findViewById(R.id.advise);
        recList = (RecyclerView) activityView.findViewById(R.id.activity_card_list);
        loading = (View) activityView.findViewById(R.id.loading_fragment);

        recList.setHasFixedSize(true);
        recList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) (getActivity().findViewById(R.id.fab));
        fab.attachToRecyclerView(recList);
        loading.setVisibility(View.VISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) activityView.findViewById(
            R.id.swipe_container);
        // the refresh listner. this would be called when the layout is pulled down
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (MainApplication.facebookuser != null) {
                    ActivitiesAllRequest
                            .excecute(HomeFragment.this.getActivity().getApplicationContext(),
                                    HomeFragment.this,
                                    "1023045adlkjwo4uosflkjf4w090",
                                    MainApplication.facebookuser.getId());


                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (onfaill) {
                            arrac = gson.fromJson(prefs.getString(OFFLINEACTIVITIES, ""), ArrayActivityAll.class);
                            MainApplication.lastActivitiesHome =arrac;
                            refreshActivities();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                }, 7000);
            }

        });

        swipeRefreshLayout
            .setColorSchemeResources(R.color.primary, R.color.font_gray, R.color.primary_dark,
                    R.color.black_overlay);

        return activityView;
    }


    @Override
    public void onActivitiesResponse(ArrayActivityAll arrayActivityAll) {
        last = gson.toJson(arrayActivityAll);
        onfaill=false;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(OFFLINEACTIVITIES,last);
        editor.commit();
        arrac = arrayActivityAll;
        MainApplication.lastActivitiesHome = arrayActivityAll;
        refreshActivities();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onSuccess(JSONObject response) {

        try {
            temp = DevuelParse(response, "version");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!temp.equals("129345967")) {


                CustomDialogFragment myDiag =  CustomDialogFragment.newInstance(
                        CustomDialogFragment.ACTUALIZE, HomeFragment.this.getActivity());
                myDiag.show(HomeFragment.this.getActivity().getFragmentManager(), "Diag");


        }

    }

    @Override
    public void onFail(VolleyError error) {
        /*Log.i("Error", error.toString());
        loading.setVisibility(View.INVISIBLE);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CustomDialogFragment myDiag =  CustomDialogFragment.newInstance(
                    CustomDialogFragment.SERVER_ERROR, HomeFragment.this.getActivity());
                myDiag.show(HomeFragment.this.getActivity().getFragmentManager(), "Diag");
            }
        });
        swipeRefreshLayout.setRefreshing(false);*/
    }


    public void refreshActivities() {
        activities.clear();
        if (arrac != null && !arrac.getItemsE().isEmpty()) {
            for (int x = 0; x < arrac.getItemsE().size(); x++) {
                /*String userName = "User admin";
                if (MainApplication.facebookFriends.get(arrac.getItemsE().get(x).getCreator())
                    != null) {
                    userName = MainApplication.facebookFriends
                        .get(arrac.getItemsE().get(x).getCreator()).getName();
                } else if (arrac.getItemsE().get(x).getCreator()
                    .equals(MainApplication.facebookuser.getId())) {
                    userName = MainApplication.facebookuser.getName();
                }*/
                activities.add(new ActicityItemInfo(arrac.getItemsE().get(x).getId(),
                                                    arrac.getItemsE().get(x).getCreator_name(),
                                                    arrac.getItemsE().get(x).getTitle(),
                                                    arrac.getItemsE().get(x).getDatetime(),
                                                    arrac.getItemsE().get(x).getCreator(), 1,
                                                    arrac.getItemsE().get(x).getHeart() == 0
                                                    ? false
                                                    : true,
                                                    Integer.parseInt(arrac.getItemsE().get(x).getClaps()),
                                                    arrac.getItemsE().get(x).getComment(),
                                                    Integer.parseInt(arrac.getItemsE().get(x).getQuota()),
                                                    arrac.getItemsE().get(x).getConfirmed().size(),
                                                    Long.parseLong(arrac.getItemsE().get(x).getDatetime_creation())));
            }
            adapter.notifyDataSetChanged();
            recList.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            tooQuietAdvice.setVisibility(View.GONE);

        } else {
            recList.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            tooQuietAdvice.setVisibility(View.VISIBLE);

        }


    }

    public String DevuelParse( JSONObject TojsonObj ,String buscar) throws JSONException {
        String text="";

        JSONObject jsonObj =  TojsonObj;
        text=jsonObj.getString(buscar);

        return text;
    }
}
