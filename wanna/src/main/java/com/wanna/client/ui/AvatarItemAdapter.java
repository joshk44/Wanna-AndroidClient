package com.wanna.client.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wanna.client.R;
import com.wanna.client.ui.custom.ProfilePictureView;

import java.util.List;

/**
 * Created by josh on 20/4/15.
 */
public class AvatarItemAdapter extends RecyclerView.Adapter<AvatarItemAdapter.AvatarViewHolder> {

    private static List<AvatarItemInfo> friendList;
    private Context context;
    private int size = 0;


    public AvatarItemAdapter(List<AvatarItemInfo> friendList, Context context) {
        this.friendList = friendList;
        this.context = context;
    }

    public AvatarItemAdapter(List<AvatarItemInfo> friendList, Context context, int size) {
        this.friendList = friendList;
        this.context = context;
        this.size = size;
    }

    @Override
    public int getItemCount() {
        return friendList.size() + 1;
    }

    @Override
    public void onBindViewHolder(AvatarViewHolder friendViewHolder, int i) {
        if (i + 1 == getItemCount()) {
            friendViewHolder.profilePictureView.setVisibility(View.INVISIBLE);
            friendViewHolder.setIsRecyclable(false);
            if (size == 0) {
                friendViewHolder.itemView.setBackgroundResource(R.drawable.more_avatar);
                friendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FreindSelector.class);
                        ((Activity) context).startActivityForResult(intent, 1);
                    }
                });
            }
        } else {
            friendViewHolder.profilePictureView.setVisibility(View.VISIBLE);
            AvatarItemInfo ci = friendList.get(i);
            friendViewHolder.profilePictureView.setProfileId(ci.profileID);
        }
    }

    @Override
    public AvatarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
            from(viewGroup.getContext()).
            inflate(R.layout.avatar_list_item, viewGroup, false);

        if (size > 0) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                itemView.getLayoutParams();
            params.width = 60;
            params.height = 60;
            itemView.setLayoutParams(params);
            View avatarItem = itemView.findViewById(R.id.avatar_profile_picture);
            LinearLayout.LayoutParams paramsAvatar = (LinearLayout.LayoutParams)
                avatarItem.getLayoutParams();
            paramsAvatar.width = 60;
            paramsAvatar.height = 60;
            itemView.setLayoutParams(paramsAvatar);
        }
        return new AvatarViewHolder(itemView);
    }

    public static class AvatarViewHolder extends RecyclerView.ViewHolder {
        protected ProfilePictureView profilePictureView;

        public AvatarViewHolder(View v) {
            super(v);
            profilePictureView = (ProfilePictureView) v.findViewById(R.id.avatar_profile_picture);
        }
    }
}

class AvatarItemInfo {
    protected String profileID;

    AvatarItemInfo(String profileID) {
        this.profileID = profileID;
    }
}

