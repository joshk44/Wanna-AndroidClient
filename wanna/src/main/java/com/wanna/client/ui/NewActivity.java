package com.wanna.client.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wanna.client.R;
import com.wanna.network.NewActivityRequest;
import com.wanna.network.handler.NewActivityHandler;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class NewActivity extends ActionBarActivity implements NewActivityHandler, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    EditText whatText;
    EditText whereText;
    TextView whenText;
    EditText howManyText;
    RecyclerView inviteList;
    Date selected_Date;
    AvatarItemAdapter avatarAdapter;
    String[] usersArray;
    TextView minus;
    TextView plus;
    List<AvatarItemInfo> avatarLists = new ArrayList<AvatarItemInfo>();
    String idUser,idname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        idUser = preferences.getString("userID", null);
        idname = preferences.getString("IDName",null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        final Button create = (Button) findViewById(R.id.create_activity);
        whatText = (EditText) findViewById(R.id.what_edit);
        whereText = (EditText) findViewById(R.id.where_edit);
        whenText = (TextView) findViewById(R.id.when_edit);
        howManyText = (EditText) findViewById(R.id.how_many_edit);
        inviteList = (RecyclerView) findViewById(R.id.selected_friends);
        inviteList.setHasFixedSize(true);
        inviteList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        inviteList.setLayoutManager(layoutManager);
        avatarAdapter = new AvatarItemAdapter(avatarLists, this);
        inviteList.setAdapter(avatarAdapter);
        whenText.setKeyListener(null);
        whatText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                whatText.setError(null);
                return false;
            }
        });

        whenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        NewActivity.this,
                        now.get(Calendar.HOUR_OF_DAY) + 1,
                        0,
                        false
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        selected_Date = new Date(System.currentTimeMillis());
        selected_Date.setHours(selected_Date.getHours() + 1);
        selected_Date.setMinutes(0);
        selected_Date.setSeconds(0);

        whenText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                whenText.setError(null);
                return false;
            }
        });


        howManyText.setText("∞");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whatText.getText().toString().length() == 0)
                    whatText.setError("What do you want is required");
                if (whenText.getText().toString().length() == 0)
                    whenText.setError("When is required!");
                if (whatText.getText().toString().length() != 0 && whenText.getText().toString().length() != 0) {

                    //Agregar usuario de facebook
                    String count = "0";
                    if (!howManyText.getText().toString().equals("∞")) {
                        count = howManyText.getText().toString();
                    }

                        NewActivityRequest.excecute(NewActivity.this.getApplicationContext(), NewActivity.this, whatText.getText().toString(), "",
                                idUser, whereText.getText().toString(), selected_Date.getTime(),
                            System.currentTimeMillis(), count, "", MainApplication.facebookuser.getName(), usersArray == null ? null : new ArrayList<String>(Arrays.asList(usersArray)));
                    //boton false esperando respuesta del servidos en un CustomDialog
                    create.setClickable(false);

                }
            }
        });

        minus = (TextView) findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         if (!howManyText.getText().toString().equals("∞")) {
                                             int count = Integer.parseInt(howManyText.getText().toString());
                                             if (count > 1) {
                                                 howManyText.setText("" + (--count));
                                             } else {
                                                 howManyText.setText("∞");
                                             }
                                         }
                                     }
                                 }
        );
        plus = (TextView) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        if (howManyText.getText().toString().equals("∞")) {
                                            howManyText.setText("1");
                                        } else {
                                            int count = Integer.parseInt(howManyText.getText().toString());
                                            if (count > 0) {
                                                howManyText.setText("" + (++count));
                                            }
                                        }
                                    }
                                }
        );

        updateTextDateView();
        updateInvitedSize();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new, menu);
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
    public void onSuccess(JSONObject response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDialogFragment myDiag =  CustomDialogFragment.newInstance(CustomDialogFragment.CREATED,
                                                                       NewActivity.this);
                myDiag.show(getFragmentManager(), "Diag");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NewActivity.this.finish();
        NewActivity.this.overridePendingTransition(R.anim.fade_in_to_bot,
                R.anim.fade_out_to_bot);
    }

    @Override
    public void onFail(VolleyError error) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CustomDialogFragment myDiag = CustomDialogFragment.newInstance(CustomDialogFragment.GENERAL_ERROR, NewActivity.this);
                myDiag.show(getFragmentManager(), "Diag");
            }
        });
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        selected_Date.setHours(hourOfDay);
        selected_Date.setMinutes(minute);
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                NewActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        //timeTextView.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selected_Date.setYear(year - 1900);
        selected_Date.setMonth(monthOfYear);
        selected_Date.setDate(dayOfMonth);
        updateTextDateView();
    }


    private void updateTextDateView() {
        long millisecDifference = selected_Date.getTime() - System.currentTimeMillis();
        long minutesDifference = (millisecDifference / (1000 * 60));
        final StringBuilder textWhen = new StringBuilder();
        if (minutesDifference <= 1440) {
            if ((selected_Date.getDate() - new Date(System.currentTimeMillis()).getDate()) < 1) {
                textWhen.append("Today at " + formatHour(selected_Date.getHours(), selected_Date.getMinutes()));
            } else {
                textWhen.append("Tomorrow at " + formatHour(selected_Date.getHours(), selected_Date.getMinutes()));
            }
        } else if (minutesDifference <= 2880) {
            textWhen.append("Tomorrow at " + formatHour(selected_Date.getHours(), selected_Date.getMinutes()));
        } else {
            textWhen.append("Next " + selected_Date.getDate() + "/" + selected_Date.getMonth()
                    + "  " + formatHour(selected_Date.getHours(), selected_Date.getMinutes()));
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                whenText.setText(textWhen);
            }
        });
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                avatarLists.clear();
                Bundle bundle = data.getExtras();
                HashSet<String> users = (HashSet<String>) bundle.get("result");
                usersArray = users.toArray(new String[users.size()]);
                for (int i = 0; i < usersArray.length; i++) {
                    avatarLists.add(new AvatarItemInfo(usersArray[i]));
                }
                avatarAdapter.notifyDataSetChanged();
                updateInvitedSize();
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    private void updateInvitedSize() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int count = avatarAdapter.getItemCount();
                if (count < 5) {
                    float pixels = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, count * 55,
                            getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                            inviteList.getLayoutParams();
                    params.width = (int) pixels;
                    inviteList.setOnScrollListener(null);
                    inviteList.setLayoutParams(params);
                } else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) inviteList
                            .getLayoutParams();
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    inviteList.setLayoutParams(params);
                }
            }
        });

    }


}
