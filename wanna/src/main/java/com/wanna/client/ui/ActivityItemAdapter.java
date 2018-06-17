package com.wanna.client.ui;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanna.client.R;
import com.wanna.client.ui.custom.ProfilePictureView;
import com.wanna.network.handler.BaseHandler;
import com.wanna.network.setClapRequest;

import java.util.Date;
import java.util.List;

public class ActivityItemAdapter extends RecyclerView.Adapter<ActivityItemAdapter.ActivitiesViewHolder> {

    private List<ActicityItemInfo> activityList;
    private final Fragment fragment;


    public ActivityItemAdapter(List<ActicityItemInfo> activityList, Fragment fragment) {
        this.activityList = activityList;
        this.fragment = fragment;
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }


    @Override
    public void onBindViewHolder(final ActivitiesViewHolder activitiesViewHolder, int i) {

        final ActicityItemInfo ci = activityList.get(i);
        activitiesViewHolder.vName.setText(ci.name);
        activitiesViewHolder.vWantsText.setText(ci.wants);


        //commnets
        activitiesViewHolder.commnet.setText(ci.comment+"\t"+"Comentarios");


        String wantsfull =ci.wants;
        Spannable colorCityName = new SpannableString(wantsfull);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);


        activitiesViewHolder.vTimeCreated.setText(parsearTime(ci.date));



        activitiesViewHolder.profilePictureView.setProfileId(ci.facebookID);
        activitiesViewHolder.likesCount.setText("" + ci.clapsCount);
        activitiesViewHolder.claps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClapRequest
                    .excecute(fragment.getActivity(), (BaseHandler) fragment,
                              MainApplication.facebookuser.getId(),
                              ci.id_activity, (ci.clapState
                                               ? "0"
                                               : "1"));
                ci.clapState = !ci.clapState;
                updateHeartState(activitiesViewHolder, ci);
            }
        });
        long hoy = new Date().getTime();

        if (Long.parseLong(ci.time) < hoy) {
            activitiesViewHolder.availabilityIcon.setImageResource(R.drawable.old_icon);
        } else {
            activitiesViewHolder.availabilityIcon.setImageResource(R.drawable.availabe_icon);
            if (ci.quota != 0
                && ci.confirmed < ci.quota) {
                activitiesViewHolder.availabilityIcon.setImageResource(R.drawable.availabe_icon);
            } else {
                activitiesViewHolder.availabilityIcon.setImageResource(R.drawable.unavailabe_icon);
            }

        }
        activitiesViewHolder.likesCount.setText(
            "" + (Integer.parseInt(activitiesViewHolder.likesCount.getText().toString())));
        if (!ci.clapState) {
            activitiesViewHolder.claps.setImageResource(R.drawable.empty_like);
        } else {
            activitiesViewHolder.claps.setImageResource(R.drawable.heart_like);
        }
    }

    private void updateHeartState(ActivitiesViewHolder activitiesViewHolder, ActicityItemInfo ci) {
        if (!ci.clapState) {
            activitiesViewHolder.claps.setImageResource(R.drawable.empty_like);
            activitiesViewHolder.likesCount.setText(
                "" + (Integer.parseInt(activitiesViewHolder.likesCount.getText().toString())
                      - 1));

        } else {
            activitiesViewHolder.claps.setImageResource(R.drawable.heart_like);
            activitiesViewHolder.likesCount.setText(
                "" + (Integer.parseInt(activitiesViewHolder.likesCount.getText().toString())
                      + 1));
        }
    }

    @Override
    public ActivitiesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
            from(viewGroup.getContext()).
            inflate(R.layout.item_activity_timesheet, viewGroup, false);
        return new ActivitiesViewHolder(itemView, fragment, i);
    }

    public String parsearTime(Long time){

        //if (time < System.currentTimeMillis()) {
            Long difference = (System.currentTimeMillis() - time) / 1000;

            if (difference < 60) {
                return "" + difference + "s";
            } else if (difference < 3600) {
                return "" + difference / 60 + "m";
            } else if (difference < 86400) {
                return "" + difference / 3600 + "h";
            } else {
                return "" + difference / 86400 + "d";
            }
        /*} else {
            Date date = new Date(time);
            return "" + date.getDate() + "/" + date.getMonth() +""+ formatHour(date.getHours(), date.getMinutes());
        }*/
    }

 /*   private String formatHour(int hours, int minutes) {
        StringBuilder date = new StringBuilder();
        date.append(hours + ":");
        if (minutes < 10) {
            date.append("0" + minutes);
        } else {
            date.append(minutes);
        }
        return date.toString();
    }*/


    public static class ActivitiesViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vWantsText;
        protected TextView vTimeCreated;
        protected ImageView vActivityStatus;
        protected ProfilePictureView profilePictureView;
        protected Activity activity;
        protected ImageView claps;
        protected TextView likesCount;
        protected TextView commnet;
        protected ImageView availabilityIcon;


        public ActivitiesViewHolder(View v, final Fragment fragment, final int i) {
            super(v);
            this.activity = fragment.getActivity();
            vName = (TextView) v.findViewById(R.id.user_name);
            vWantsText = (TextView) v.findViewById(R.id.wants_text);
            vTimeCreated = (TextView) v.findViewById(R.id.time_created);
            vActivityStatus = (ImageView) v.findViewById(R.id.availability_icon);
            profilePictureView = (ProfilePictureView) v
                .findViewById(R.id.profile_picture_timesheet);
            claps = (ImageView) v.findViewById(R.id.like_icon);
            likesCount = (TextView) v.findViewById(R.id.likes_count);
            availabilityIcon = (ImageView) v.findViewById(R.id.availability_icon);
            commnet = (TextView) v.findViewById(R.id.txtCommentTimeSheet);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(fragment.getActivity(), DetailsActivity.class);
                    intent.putExtra("ACTIVITY_ID", "" + ActivitiesViewHolder.this.getPosition());
                    if (fragment instanceof HomeFragment) {
                        intent
                            .putExtra("FROM", "HOME" );
                    } else {
                        intent
                            .putExtra("FROM", "SEARCH");
                    }

                    String transitionName = fragment
                        .getString(R.string.transition_activity_details);
                    ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(),
                                                                           v.findViewById(
                                                                               R.id.base_layout),
                                                                           transitionName);
                    ActivityCompat.startActivity(fragment.getActivity(), intent, options.toBundle());
                }
            });
        }
    }


}


class ActicityItemInfo {
    protected String id_activity;
    protected String name;
    protected String wants;
    protected String time;
    protected String facebookID;
    protected int status;
    protected boolean clapState;
    protected int clapsCount;
    protected int comment;
    protected int quota;
    protected int confirmed;
    protected long date;


    ActicityItemInfo(String postion, String name, String wants, String time, String facebookID,
                     int status, boolean clapState, int clapsCount,int comment, int quota, int confirmed,
                     long date) {
        this.id_activity = postion;
        this.name = name;
        this.wants = wants;
        this.time = time;
        this.facebookID = facebookID;
        this.status = status;
        this.clapState = clapState;
        this.clapsCount = clapsCount;
        this.comment = comment;
        this.quota = quota;
        this.confirmed = confirmed;
        this.date = date;
    }
}

