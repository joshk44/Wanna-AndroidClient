package com.wanna.client.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wanna.client.R;
import com.wanna.client.ui.custom.ProfilePictureView;
import com.wanna.data.ArrayCommnets;
import com.wanna.models.Comment;
import com.wanna.network.CancelActivityRequest;
import com.wanna.network.CommentsActivityRequest;
import com.wanna.network.DeleteCommentRequest;
import com.wanna.network.handler.BaseHandler;
import com.wanna.network.handler.CommnetsHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fede on 05/08/15.
 */
public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.ViewHolder> implements BaseHandler , CommnetsHandler {

    private ArrayList<Comment> comments;
    private int itemLayout;
    private Context context;
    private String TempLeng;
    private String idUser, idname;



    public CommentItemAdapter(ArrayList<Comment> comments, int itemLayout) {
        this.comments = comments;
        this.itemLayout = itemLayout;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        context = viewGroup.getContext();
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(viewGroup.getContext().getApplicationContext());
        idUser = preferences.getString("userID", null);
        idname = preferences.getString("IDName", null);

        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Comment comment = comments.get(i);
        viewHolder.profile.setProfileId(comment.getUser_id());

        TempLeng=comment.getName()+":"+"\t"+comment.getComment();
        Spannable colorCityName = new SpannableString(TempLeng);
        final ForegroundColorSpan colorText = new ForegroundColorSpan(Color.GRAY);
        final StyleSpan style = new StyleSpan(Typeface.NORMAL);
        colorCityName.setSpan(colorText, comment.getName().length(), TempLeng.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        colorCityName.setSpan(style, comment.getName().length(), TempLeng.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.comment.setText(colorCityName);

        if (!comment.getTime().equals("")) {
            viewHolder.timeComment.setText(parsearTime(Long.parseLong(comment.getTime())));
        }
        viewHolder.commentRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (comment.getUser_id().equals(idUser) || comment.getAdmina().equals(idUser)) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(context);

                    ad.setCancelable(true);
                    ad.setTitle("Quieres borrar este Comentario?");
                    ad.setMessage("En unos segundos se eliminara");
                    ad.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            DeleteCommentRequest.excecute(context.getApplicationContext(), CommentItemAdapter.this, idUser, comment.getEvent_id(), comment.getId());

                        }
                    });
                    ad.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {


                        }
                    });
                    ad.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void onSuccess(JSONObject response) {

    }

    @Override
    public void onFail(VolleyError error) {

    }

    @Override
    public void onUsersResponse(ArrayCommnets Acommnets) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ProfilePictureView profile;
        public TextView comment;
        public TextView timeComment;
        public RelativeLayout commentRela;


        public ViewHolder(View itemView) {
            super(itemView);

            profile = (ProfilePictureView) itemView.findViewById(R.id.profile_picture_comment);
            comment = (TextView) itemView.findViewById(R.id.txtcomentid);
            timeComment = (TextView) itemView.findViewById(R.id.txtTimeComment);
            commentRela = (RelativeLayout) itemView.findViewById(R.id.commentRela);


        }

    }

    public String parsearTime(Long time){

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
            return "" + date.getDate() + "/" + date.getMonth() + "\n" + formatHour(date.getHours(), date.getMinutes());
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

    /*private Dialog deleteComment(final String idComment, final String idActivity, final String idUser) {

        View view = context.getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setView(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context.getApplicationContext());
        builder.setTitle("Deseas eliminar tú comentario?");
        builder.setIcon(R.drawable.ic_launcher);

        LinearLayout layout = new LinearLayout(this.context.getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(5, 5, 5, 5);

        builder.setView(layout);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                DeleteCommentRequest.excecute(context.getApplicationContext(),CommentItemAdapter.this,idUser,idActivity,idComment);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        return builder.show();

    }*/

}
