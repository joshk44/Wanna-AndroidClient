package com.wanna.client.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanna.client.R;


public class CustomDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int SERVER_ERROR = 0;
    public static final int GENERAL_ERROR = 1;
    public static final int JOINED = 2;
    public static final int CREATED = 3;
    public static final int ACTUALIZE = 4;
    public static final int SERVER_ERROR_ACTIVITY = 5;
    public static final int SERVER_ERROR_LOGIN = 6;
    public static final int CANCELACTIVITY = 7;

    private int type;
    private Activity activity;
    private static CustomDialogFragment newDialog;

    public static CustomDialogFragment newInstance (int type, Activity activity) {

        newDialog =  new CustomDialogFragment();
        newDialog.type = type;
        newDialog.activity = activity;

        return newDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_custom_dialog, container, false);
        TextView title = (TextView) mainView.findViewById(R.id.title_dialog);
        ImageView imageView = (ImageView) mainView.findViewById(R.id.image_dialog);
        TextView description = (TextView) mainView.findViewById(R.id.message_dialog);
        Button okDialog = (Button) mainView.findViewById(R.id.ok_dialog);
        Button canelDialog = (Button) mainView.findViewById(R.id.cancel_dialog);
            canelDialog.setVisibility(View.GONE);
            switch (type) {
            case GENERAL_ERROR:
                title.setText(R.string.oh_snap);
                imageView.setImageResource(R.drawable.general_error);
                description.setText(R.string.general_error);
                break;
            case SERVER_ERROR:
                title.setText(R.string.oops);
                imageView.setImageResource(R.drawable.fail_server);
                description.setText(R.string.server_error);
                break;
            case JOINED:
                title.setText(R.string.super_title);
                imageView.setImageResource(R.drawable.congratulations);
                description.setText(R.string.joined);
                break;
            case CREATED:
                title.setText(R.string.super_title);
                imageView.setImageResource(R.drawable.congratulations);
                description.setText(R.string.created_activity);
                break;
            case ACTUALIZE:
                title.setText(R.string.actualize_title);
                imageView.setImageResource(R.drawable.updateapp);
                description.setText(R.string.actualized);
                break;
                case SERVER_ERROR_ACTIVITY:
                    title.setText(R.string.oops);
                    imageView.setImageResource(R.drawable.fail_server);
                    description.setText(R.string.server_error);
                    break;
                case SERVER_ERROR_LOGIN:
                    title.setText(R.string.oops);
                    imageView.setImageResource(R.drawable.fail_server);
                    description.setText(R.string.server_error);
                    newDialog.setCancelable(false);
                    break;
                case CANCELACTIVITY:
                    title.setText(R.string.cancel_activity_title);
                    imageView.setImageResource(R.drawable.cancelactivity);
                    description.setText(R.string.cancel_activity);
                    newDialog.setCancelable(false);
                    break;


        }
        okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment.this.dismiss();
                if (type == CREATED || type == JOINED || type == SERVER_ERROR_ACTIVITY) {
                    activity.finish();
                    activity.overridePendingTransition(R.anim.fade_in_to_bot,
                                                       R.anim.fade_out_to_bot);
                }
                if (type == ACTUALIZE) {
                    String slink;
                    slink="https://play.google.com/store/apps/details?id=com.wanna.client";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(slink));
                    activity.finish();
                    activity.overridePendingTransition(R.anim.fade_in_to_bot,
                            R.anim.fade_out_to_bot);
                    activity.startActivity(i);

                }
                if (type == SERVER_ERROR_ACTIVITY) {
                    activity.finish();
                    activity.overridePendingTransition(R.anim.fade_in_to_bot,
                            R.anim.fade_out_to_bot);

                }

            }
        });

        return mainView;
    }

}
