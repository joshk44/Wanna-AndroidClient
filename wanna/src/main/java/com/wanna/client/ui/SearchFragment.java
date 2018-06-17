package com.wanna.client.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wanna.client.R;
import com.wanna.data.ArrayActivityAll;
import com.wanna.data.ArrayUser;
import com.wanna.network.SearchActivitiesRequest;
import com.wanna.network.SearchUserRequest;
import com.wanna.network.handler.ActivityAllHandler;
import com.wanna.network.handler.BaseHandler;
import com.wanna.network.handler.UserHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements ActivityAllHandler, UserHandler, BaseHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static boolean isActivitySelected = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayActivityAll arrac;
    private List<ActicityItemInfo> activities = new ArrayList<>();
    private ActivityItemAdapter adapter;
    private RecyclerView activitiesList;

    private FriendsListAdapter friendsAdapter;
    private List<FriendItemInfo> friends = new ArrayList<FriendItemInfo>();
    RecyclerView friendsRecList;
    private ArrayUser users;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        activitiesList = (RecyclerView) rootView.findViewById(R.id.activities_result);
        activitiesList.setHasFixedSize(true);
        activitiesList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutActivities = new LinearLayoutManager(this.getActivity());
        layoutActivities.setOrientation(LinearLayoutManager.VERTICAL);
        activitiesList.setLayoutManager(layoutActivities);
        adapter = new ActivityItemAdapter(activities, this);
        activitiesList.setAdapter(adapter);
        friendsRecList = (RecyclerView) rootView.findViewById(R.id.friends_result);
        friendsRecList.setHasFixedSize(true);
        friendsRecList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        friendsRecList.setLayoutManager(layoutManager);
        friendsAdapter = new FriendsListAdapter(friends);
        friendsRecList.setAdapter(friendsAdapter);
        refreshFriends();

        final EditText searchEdit = (EditText) rootView.findViewById(R.id.search_field);
        final Button activities = (Button) rootView.findViewById(R.id.button_activities_search);
        final Button friends = (Button) rootView.findViewById(R.id.button_friends_search);
        activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isActivitySelected = true;
                activities.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_baseline_orange));
                activities.setTextColor(getResources().getColor(R.color.primary));

                friends.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_baseline));
                friends.setTextColor(getResources().getColor(R.color.font_gray));

                activitiesList.setVisibility(View.VISIBLE);
                friendsRecList.setVisibility(View.GONE);
                if (searchEdit.getText().toString().length() > 0) {
                    performSearch(searchEdit.getText().toString());
                }
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isActivitySelected = false;
                activities.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_baseline));
                activities.setTextColor(getResources().getColor(R.color.font_gray));
                friends.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_baseline_orange));
                friends.setTextColor(getResources().getColor(R.color.primary));

                activitiesList.setVisibility(View.GONE);
                friendsRecList.setVisibility(View.VISIBLE);
                if (searchEdit.getText().toString().length() > 0) {
                    performSearch(searchEdit.getText().toString());
                }
            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                performSearch(searchEdit.getText().toString());
                return false;
            }
        });

        return rootView;
    }


    @Override
    public void onActivitiesResponse(ArrayActivityAll activities) {
        arrac = activities;
        MainApplication.lastActivitiesSearch = activities;
        refreshActivities();
    }

    @Override
    public void onUsersResponse(ArrayUser users) {
        this.users = users;
        refreshFriends();
    }

    @Override
    public void onSuccess(JSONObject response) {

    }

    @Override
    public void onFail(VolleyError error) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CustomDialogFragment myDiag =  CustomDialogFragment.newInstance(CustomDialogFragment.GENERAL_ERROR, SearchFragment.this.getActivity());
                myDiag.show(SearchFragment.this.getActivity().getFragmentManager(), "Diag");
            }
        });
    }


    private void performSearch(String value) {
        if (isActivitySelected) {
            SearchActivitiesRequest.excecute(this.getActivity(), SearchFragment.this, "1023045adlkjwo4uosflkjf4w090", value);
        } else {
            SearchUserRequest.excecute(this.getActivity(), SearchFragment.this, "1023045adlkjwo4uosflkjf4w090", value);
        }
        //Start Loading
    }

    public void refreshActivities() {
        activities.clear();
        if (arrac != null) {
            for (int x = 0; x < arrac.getItemsE().size(); x++) {
                /*String userName = "User admin";
                if (MainApplication.facebookFriends.get(arrac.getItemsE().get(x).getCreator()) != null) {
                    userName = MainApplication.facebookFriends.get(arrac.getItemsE().get(x).getCreator()).getName();
                } else if (arrac.getItemsE().get(x).getCreator().equals(MainApplication.facebookuser.getId())) {
                    userName = MainApplication.facebookuser.getName();
                }*/
                activities.add(new ActicityItemInfo(arrac.getItemsE().get(x).getId(), arrac.getItemsE().get(x).getCreator_name(),
                                                    arrac.getItemsE().get(x).getTitle(),
                                                    arrac.getItemsE().get(x).getDatetime(),
                                                    arrac.getItemsE().get(x).getCreator(), 1, false,
                                                    Integer.parseInt(arrac.getItemsE().get(x).getClaps()),
                                                    arrac.getItemsE().get(x).getComment(),
                                                    Integer.parseInt(arrac.getItemsE().get(x).getQuota()),
                                                    arrac.getItemsE().get(x).getConfirmed().size(),
                                                    Long.parseLong(arrac.getItemsE().get(x).getDatetime_creation())));
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void refreshFriends() {
        if (users != null && users.getItemsU() != null) {
            friends.clear();
            for (int x = 0; x < users.getItemsU().size(); x++) {
                friends.add(new FriendItemInfo(users.getItemsU().get(x).getName(), users.getItemsU().get(x).getUser_Name()));
            }
            friendsAdapter.notifyDataSetChanged();
        }
    }
}
