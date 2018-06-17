package com.wanna.client.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanna.client.R;
import com.wanna.client.ui.custom.ProfilePictureView;

import java.util.HashSet;
import java.util.List;

/**
 * Created by josh on 20/4/15.
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder> {

    private static List<FriendItemInfo> friendList;
    private static HashSet<String> selected;

    public FriendsListAdapter(List<FriendItemInfo> friendList) {
        this(friendList, null);
    }

    public FriendsListAdapter(List<FriendItemInfo> friendList, HashSet<String> selected) {
        this.friendList = friendList;
        this.selected = selected;
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }


    public HashSet<String> getSelectedFriends() {
        return selected;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder friendViewHolder, int i) {
        FriendItemInfo ci = friendList.get(i);
        friendViewHolder.profilePictureView.setProfileId(ci.profileID);
        friendViewHolder.userName.setText(ci.name);
        if (ci.hasSelector && ci.isSelected) {
            friendViewHolder.selectedTick.setImageResource(R.drawable.ticked);
        } else if (ci.hasSelector && !ci.isSelected) {
            friendViewHolder.selectedTick.setImageResource(R.drawable.nonticked);
        } else {
            friendViewHolder.selectedTick.setVisibility(View.GONE);
        }

    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_friends_selector, viewGroup, false);

        return new FriendsViewHolder(itemView);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        protected ProfilePictureView profilePictureView;
        protected TextView userName;
        protected ImageView selectedTick;


        public FriendsViewHolder(View v) {
            super(v);
            profilePictureView = (ProfilePictureView) v.findViewById(R.id.profile_picture);
            userName = (TextView) v.findViewById(R.id.user_name);
            selectedTick = (ImageView) v.findViewById(R.id.tick_icon);
            if (selected != null) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendItemInfo itemInfo = getFriendItem(profilePictureView.getProfileId());
                        if (itemInfo != null && itemInfo.isSelected) {
                            selectedTick.setImageResource(R.drawable.nonticked);
                            selected.remove(itemInfo.profileID);
                            itemInfo.isSelected = false;

                        } else if (itemInfo != null && !itemInfo.isSelected) {
                            selectedTick.setImageResource(R.drawable.ticked);
                            selected.add(itemInfo.profileID);
                            itemInfo.isSelected = true;
                        }
                    }
                });
            } else {
                selectedTick.setVisibility(View.GONE);
            }
        }

        public FriendItemInfo getFriendItem(String profileId) {
            for (int i = 0; i < friendList.size(); i++) {
                if (friendList.get(i).profileID.equals(profileId)) {
                    return friendList.get(i);
                }
            }
            return null;
        }


    }
}

class FriendItemInfo {

    protected String name;
    protected String profileID;
    protected boolean isSelected;
    protected boolean hasSelector;


    FriendItemInfo(String name, String profileID) {
        this.name = name;
        this.profileID = profileID;
        hasSelector = false;
    }

    FriendItemInfo(String name, String profileID, boolean isSelected) {
        this.name = name;
        this.profileID = profileID;
        this.isSelected = isSelected;
        hasSelector = true;

    }
}

