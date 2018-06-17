package com.wanna.client.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanna.client.R;

import java.util.Date;
import java.util.List;

public class ProfileActivityItemAdapter extends RecyclerView.Adapter<ProfileActivityItemAdapter.ProfileActivityViewHolder> {

    private List<ProfileActicityItemInfo> activityList;
    private Activity activity;
    private String idUser;

    public ProfileActivityItemAdapter(List<ProfileActicityItemInfo> activityList,
                                      Activity activity) {
        this.activityList = activityList;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    @Override
    public void onBindViewHolder(ProfileActivityViewHolder profileActivityViewHolder, int i) {

        final ProfileActicityItemInfo ci = activityList.get(i);

        if ( idUser.equals(ci.creator)) {
            String wantsText = activity.getString(R.string.you_created) + " " + ci.wants;
            int nameIndex = activity.getString(R.string.you_created).length() + 1;

            Spannable colorCityName = new SpannableString(wantsText);

            colorCityName.setSpan(
                new ForegroundColorSpan(activity.getResources().getColor(R.color.font_gray))
                , 0, nameIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            colorCityName
                .setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.primary))
                    , nameIndex, wantsText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            profileActivityViewHolder.vWantsText.setText(colorCityName);
        } else {
            String wantsText =
                ci.name + " " + activity.getString(R.string.wants_to_text) + " " + ci.wants;
            int nameIndex = ci.name.length();
            int wantsIndex = nameIndex + activity.getString(R.string.wants_to_text).length() + 1;

            Spannable colorCityName = new SpannableString(wantsText);

            // Span to set text color to some RGB value
            final ForegroundColorSpan fcs = new ForegroundColorSpan(
                activity.getResources().getColor(
                    R.color.primary));
            final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

            colorCityName
                .setSpan(fcs
                    , 0, nameIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            colorCityName
                .setSpan(bss
                    , 0, nameIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            colorCityName.setSpan(
                new ForegroundColorSpan(activity.getResources().getColor(R.color.font_gray))
                , nameIndex + 1, wantsIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            colorCityName
                .setSpan(fcs, wantsIndex + 1, wantsText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            profileActivityViewHolder.vWantsText.setText(colorCityName);
        }
        if (!ci.time.equals("")) {
            profileActivityViewHolder.vTimeCreated.setText(getTimeAgo(Long.parseLong(ci.time)));
        }

        profileActivityViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent(ProfileActivityItemAdapter.this.activity,
                                                 DetailsActivity.class);

                resultIntent.putExtra("activityID", ci.activityID);
                resultIntent.setAction(Intent.ACTION_MAIN);
                resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resultIntent
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ProfileActivityItemAdapter.this.activity.startActivity(resultIntent);
            }
        });
    }

    private String getTimeAgo(Long time) {

        if (time < System.currentTimeMillis()) {
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
        } else {
            Date date = new Date(time);
            return "" + date.getDate() + "/" + date.getMonth() + "\n" + formatHour(date.getHours(),
                                                                                   date.getMinutes());
        }
    }

    private String formatHour(int hours, int minutes) {
        StringBuilder date = new StringBuilder();
        date.append(hours + ":");
        if (minutes < 10) {
            date.append("0" + minutes);
        } else {
            date.append(minutes);
        }
        return date.toString();
    }

    @Override
    public ProfileActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
            from(viewGroup.getContext()).
            inflate(R.layout.item_profile_activities, viewGroup, false);
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(viewGroup.getContext().getApplicationContext());
        idUser = preferences.getString("userID", null);

        return new ProfileActivityViewHolder(itemView);
    }

    public static class ProfileActivityViewHolder extends RecyclerView.ViewHolder {
        protected TextView vWantsText;
        protected TextView vTimeCreated;
        protected View itemView;


        public ProfileActivityViewHolder(View v) {
            super(v);
            vWantsText = (TextView) v.findViewById(R.id.wants_text);
            vTimeCreated = (TextView) v.findViewById(R.id.time_ago);
            itemView = v;
        }
    }
}

class ProfileActicityItemInfo {
    protected String userId;
    protected String name;
    protected String wants;
    protected String time;
    protected String activityID;
    protected int clapsCount;
    protected int comment;
    protected String creator;



    ProfileActicityItemInfo(String userId, String name, String wants, String time,
                            String activityID, int clapsCount,int comment,String creator) {
        this.userId = userId;
        this.name = name;
        this.wants = wants;
        this.time = time;
        this.activityID = activityID;
        this.clapsCount = clapsCount;
        this.comment = comment;
        this.creator=creator;
    }
}




