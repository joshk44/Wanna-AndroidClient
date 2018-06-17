package com.wanna.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.facebook.model.GraphUser;
import com.wanna.client.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FreindSelector extends ActionBarActivity {

    private FriendsListAdapter friendsAdapter;
    private List<FriendItemInfo> friends = new ArrayList<FriendItemInfo>();
    RecyclerView friendsRecList;
    HashSet<String> selectedFriends = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freind_selector);
        friendsRecList = (RecyclerView) findViewById(R.id.list_friends);
        friendsRecList.setHasFixedSize(true);
        friendsRecList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        friendsRecList.setLayoutManager(layoutManager);
        friendsAdapter = new FriendsListAdapter(friends, selectedFriends);
        friendsRecList.setAdapter(friendsAdapter);
        refreshFriends();

        Button okButton = (Button) findViewById(R.id.accept_friend);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", selectedFriends);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    public void refreshFriends() {
        friends.clear();
        List<GraphUser> facebookFriends =
                new ArrayList<GraphUser>(MainApplication.facebookFriends.values());
        for (int x = 0; x < facebookFriends.size(); x++) {
            friends.add(new FriendItemInfo(facebookFriends.get(x).getName(), facebookFriends.get(x).getId(), false));
        }
        friendsAdapter.notifyDataSetChanged();
    }
}
