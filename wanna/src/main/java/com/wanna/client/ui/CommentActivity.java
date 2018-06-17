package com.wanna.client.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wanna.client.R;
import com.wanna.client.ui.custom.ProfilePictureView;
import com.wanna.data.ArrayCommnets;
import com.wanna.models.Comment;
import com.wanna.network.CommentsActivityRequest;
import com.wanna.network.handler.BaseHandler;
import com.wanna.network.handler.CommnetsHandler;
import com.wanna.network.setCommentRequest;

import org.json.JSONObject;

import java.util.ArrayList;

public class CommentActivity extends Activity implements CommnetsHandler , BaseHandler {

    private ArrayCommnets arrayCommnets;
    private ArrayList<Comment> commentsArrayList = new ArrayList<Comment>();
    private CommentItemAdapter adapter;
    private RecyclerView recyclerView;
    private ProfilePictureView pic;
    private EditText commentEdi;
    private ImageButton imageButton;
    private String idUser,idname;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        idUser = preferences.getString("userID", null);
        idname = preferences.getString("IDName",null);

        CommentsActivityRequest.excecute(this.getApplicationContext(), CommentActivity.this, "1023045adlkjwo4uosflkjf4w090", getIntent().getStringExtra("idactivity"));

        recyclerView = (RecyclerView) findViewById(R.id.list_comment);
        pic = (ProfilePictureView) findViewById(R.id.profile_picture_comment);
        commentEdi = (EditText) findViewById(R.id.edit_comment);
        imageButton = (ImageButton) findViewById(R.id.imgbtnsend);

        pic.setProfileId(idUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentItemAdapter(commentsArrayList,R.layout.item_comment);
        recyclerView.setAdapter(adapter);




        commentEdi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == commentEdi.getImeActionId()) {
                    sendMessage();
                    handled = true;
                }
                return handled;
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                sendMessage();
            }
        });


    }

    public void refreshFriends() {
        commentsArrayList.clear();
        if (arrayCommnets != null) {
            for (int i = 0; i < arrayCommnets.getItemsC().size(); i++) {
                commentsArrayList.add(new Comment(arrayCommnets.getItemsC().get(i).getId(),arrayCommnets.getItemsC().get(i).getEvent_id(),arrayCommnets.getItemsC().get(i).getUser_id(),arrayCommnets.getItemsC().get(i).getName(),arrayCommnets.getItemsC().get(i).getComment(),arrayCommnets.getItemsC().get(i).getTime(),arrayCommnets.getItemsC().get(i).getAdmina()));
            }
            adapter.notifyDataSetChanged();
            if(arrayCommnets.getItemsC().size()>4) {
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUsersResponse(ArrayCommnets Acommnets) {

        arrayCommnets=Acommnets;
        refreshFriends();
    }

    @Override
    public void onSuccess(JSONObject response) {

    }

    @Override
    public void onFail(VolleyError error) {
        CustomDialogFragment myDiag = CustomDialogFragment.newInstance(
                CustomDialogFragment.SERVER_ERROR, CommentActivity.this);
        myDiag.show(CommentActivity.this.getFragmentManager(), "Diag");

    }

    public void sendMessage(){
        if(isOnline()) {
            if (commentEdi.getText().length() > 5 && !(getIntent().getStringExtra("idactivity").isEmpty())) {
                setCommentRequest.excecute(this.getApplicationContext(), CommentActivity.this, getIntent().getStringExtra("idactivity"), idUser, idname, commentEdi.getText().toString(), getIntent().getStringExtra("nameActivity"), String.valueOf(System.currentTimeMillis()),getIntent().getStringExtra("idAdminActivity"));
                commentEdi.setText("");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        CommentsActivityRequest.excecute(getApplication().getApplicationContext(), CommentActivity.this, "1023045adlkjwo4uosflkjf4w090", getIntent().getStringExtra("idactivity"));
                        if (arrayCommnets.getItemsC().size() > 4) {
                            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                }, 500);

            }
        }else{
            CustomDialogFragment myDiag = CustomDialogFragment.newInstance(
                    CustomDialogFragment.SERVER_ERROR, CommentActivity.this);
            myDiag.show(CommentActivity.this.getFragmentManager(), "Diag");
        }
    }

    public static Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
