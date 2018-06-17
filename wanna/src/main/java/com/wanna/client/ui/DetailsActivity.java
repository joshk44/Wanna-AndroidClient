package com.wanna.client.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wanna.client.R;
import com.wanna.client.ui.custom.ProfilePictureView;
import com.wanna.data.ArrayActivityAll;
import com.wanna.models.ActivityAll;
import com.wanna.network.ActivityAllRequest;
import com.wanna.network.CancelActivityRequest;
import com.wanna.network.handler.ActivityAllHandler;
import com.wanna.network.handler.BaseHandler;
import com.wanna.network.updateConfirmedRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailsActivity extends ActionBarActivity implements ActivityAllHandler, BaseHandler {

    static String TOP = "DetailsActivity:activity_details_title";
    View loadingLayout;
    RelativeLayout mainDetails;
    ProfilePictureView profile;
    TextView name;
    TextView wanna;
    TextView location;
    TextView invites;
    TextView likes;
    TextView commnet;
    TextView time;
    Button notNow;
    Button join;
    String userName;
    String idUser;
    String idActivity = "";
    private RecyclerView inviteList;
    private RecyclerView inviteListNot;
    private ImageView availabilityIcon;
    private TextView detailsWants;
    private AvatarItemAdapter avatarAdapter;
    private AvatarItemAdapterNot avatarAdapterNot;
    private TextView statusText;
    List<AvatarItemInfo> avatarLists = new ArrayList<AvatarItemInfo>();
    List<AvatarItemInfoNot> avatarListsNot = new ArrayList<AvatarItemInfoNot>();
    String from = "";
    private String idUserForMenu;
    private boolean timeOut;
    private Toolbar toolbar;
    private String deleteRespon,succesRespon;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateValues(intent);
    }


    public void updateValues(Intent intent) {
        //Para time Out
        timeOut=true;

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        idUser = preferences.getString("userID", null);

        if (intent.getExtras().get("wannaMsg") != null) {
            String json = intent.getExtras().get("wannaMsg").toString();
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObj != null) {
                try {
                    idActivity = jsonObj.getString("id_activity");
                    userName = jsonObj.getString("user_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (intent.getExtras().getString("activityID",null) != null) {
            idActivity = intent.getExtras().getString("activityID");

        }

        if (!idActivity.equals("")) {
            loadingLayout.setVisibility(View.VISIBLE);
            mainDetails.setVisibility(View.GONE);
            ActivityAllRequest.excecute(this, this, "1023045adlkjwo4uosflkjf4w090", idActivity);

            //CAMBIAR ---------------------- PARCHE POR AHORA ----- PASAR TODOS LOS PARAMETROS
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (timeOut) {
                        CustomDialogFragment myDiag = CustomDialogFragment.newInstance(
                                CustomDialogFragment.SERVER_ERROR_ACTIVITY, DetailsActivity.this);
                        myDiag.show(DetailsActivity.this.getFragmentManager(), "Diag");
                    }
                }
            }, 6000);


        } else {
            loadingLayout.setVisibility(View.GONE);
            mainDetails.setVisibility(View.VISIBLE);
            ArrayActivityAll activities = null;
            switch (from) {
                case "SEARCH": {
                    activities = MainApplication.lastActivitiesSearch;

                    break;
                }
                case "HOME": {
                    activities = MainApplication.lastActivitiesHome;

                    break;
                }
            }
            ActivityAll activitySelected = activities.getItemsE()
                    .get(
                            Integer.parseInt((String) this.getIntent().getExtras().get("ACTIVITY_ID")));
            profile.setProfileId(activitySelected.getCreator());
            /*if (MainApplication.facebookuser != null && activitySelected.getCreator()
                    .equals(MainApplication.facebookuser.getId())) {
                name.setText(MainApplication.facebookuser.getName());
            } else if (MainApplication.facebookFriends != null) {
                name.setText(
                        MainApplication.facebookFriends.get(activitySelected.getCreator())
                                .getName());
            }*/
            //Obtengo id user para validar Edit y Cancel
            idUserForMenu=activitySelected.getCreator();

            //
            name.setText(activitySelected.getCreator_name());
            wanna.setText(capitalize(activitySelected.getTitle()));

            String wantsText = getResources().getString(R.string.wants_to);
            String wantsfull = wantsText + " " + activitySelected.getTitle();
            Spannable colorCityName = new SpannableString(wantsfull);
            final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
            final ForegroundColorSpan colorWant = new ForegroundColorSpan(Color.parseColor("#F7952C"));
            colorCityName.setSpan(colorWant, wantsText.length(), wantsfull.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            colorCityName.setSpan(bss, wantsText.length(), wantsfull.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            detailsWants.setText(colorCityName);

            //Comentarios
            commnet.setText(activitySelected.getComment()+"\t"+"Comentarios");

            location.setText(activitySelected.getLocation());
            invites.setText(
                    "" + activitySelected.getConfirmed().size() + "/" + activitySelected
                            .getQuota());
            updateTextDateView(Long.parseLong(activitySelected.getDatetime()));
            likes.setText(activitySelected.getClaps() + "\t" + getResources().getString(R.string.txtLikeTimeSheet));
            idActivity = activitySelected.getId();
            notNow.setVisibility(View.GONE);
            if (MainApplication.facebookuser != null) {
                if (activitySelected.getCreator().equals(MainApplication.facebookuser.getId())) {
                    join.setVisibility(View.GONE);
                }
            }
            updateIconAvailability(activitySelected);
            for (int i = 0; activitySelected.getConfirmed().size() > i; i++) {
                avatarLists.add(new AvatarItemInfo(activitySelected.getConfirmed().get(i)));
            }
           /* for (int i = 0; activitySelected.getNotconfirmed().size() > i; i++) {
                avatarListsNot.add(new AvatarItemInfoNot(activitySelected.getNotconfirmed().get(i)));
            }*/



            avatarAdapter.notifyDataSetChanged();

        }
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        this.getIntent().getExtras().get("ACTIVITY_ID");

        from = (String) this.getIntent().getExtras().get("FROM");
        profile = (ProfilePictureView) findViewById(R.id.profile_picture_details);
        name = (TextView) findViewById(R.id.details_name);
        wanna = (TextView) findViewById(R.id.details_wanna);
        location = (TextView) findViewById(R.id.details_site);
        invites = (TextView) findViewById(R.id.details_total);
        time = (TextView) findViewById(R.id.details_date);
        commnet = (TextView) findViewById(R.id.details_like_comment);
        detailsWants = (TextView) findViewById(R.id.details_wants_text);
        likes = (TextView) findViewById(R.id.details_like);
        loadingLayout = (View) findViewById(R.id.loading_screen);
        mainDetails = (RelativeLayout) findViewById(R.id.main_details);
        availabilityIcon = (ImageView) findViewById(R.id.availability_icon);
        join = (Button) findViewById(R.id.join_button);
        //toolbar
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        //button actions
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateConfirmedRequest
                        .excecute(DetailsActivity.this, DetailsActivity.this, idUser, idActivity);

            }
        });
        statusText = (TextView) findViewById(R.id.status_text);

        inviteList = (RecyclerView) findViewById(R.id.detail_invited_friends);
        inviteList.setHasFixedSize(true);
        inviteList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        inviteList.setLayoutManager(layoutManager);
        avatarAdapter = new AvatarItemAdapter(avatarLists, this, 30);
        inviteList.setAdapter(avatarAdapter);
        //invited_not_response
        /*inviteListNot = (RecyclerView) findViewById(R.id.detail_invited_friends_not);
        inviteListNot.setHasFixedSize(true);
        inviteListNot.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManagerNot = new LinearLayoutManager(this);
        layoutManagerNot.setOrientation(LinearLayoutManager.HORIZONTAL);
        inviteListNot.setLayoutManager(layoutManagerNot);
        avatarAdapterNot = new AvatarItemAdapterNot(avatarListsNot, this, 30);
        inviteListNot.setAdapter(avatarAdapterNot);*/

        notNow = (Button) findViewById(R.id.not_now_button);
        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.this.finish();
            }
        });
        updateValues(this.getIntent());

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                /*if (id == R.id.menueditActivity) {
                    Toast.makeText(getApplicationContext(), "Editar", Toast.LENGTH_SHORT).show();

                }*/

                if (id == R.id.menudeleteActivity && idUser.equals(idUserForMenu)) {
                    //Toast.makeText(getApplicationContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                    deleteDialog();
                } else if (id == R.id.menudeleteActivity) {
                    Toast.makeText(getApplicationContext(), "Debes ser Administrador", Toast.LENGTH_SHORT).show();
                }
                if (id == R.id.commentgo) {
                    //Toast.makeText(getApplicationContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                    commentgo();
                }

                return true;
            }


        });
        commnet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                commentgo();
            }
        });

        if(idUser.equals(idUserForMenu)){

        toolbar.inflateMenu(R.menu.menu_details);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.menueditActivity) {
            Toast.makeText(getApplicationContext(), "Editar", Toast.LENGTH_SHORT).show();

        }*/
        if (id == R.id.menudeleteActivity && idUser.equals(idUserForMenu)) {
            //Toast.makeText(getApplicationContext(), "Cancelar", Toast.LENGTH_SHORT).show();
            deleteDialog();
        }else if(id == R.id.menudeleteActivity){
            Toast.makeText(getApplicationContext(), "Debes ser Administrador", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.commentgo) {
            //Toast.makeText(getApplicationContext(), "Cancelar", Toast.LENGTH_SHORT).show();
            commentgo();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTextDateView(Long date) {
        Date selected_Date = new Date(date);
        int tempTim ;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        tempTim=cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(selected_Date);
        //Para ((am_pm==Calendar.AM)?"am":"pm"))
        int am_pm=cal.get(Calendar.AM_PM);

        long diff = selected_Date.getTime() - System.currentTimeMillis();
        long minsDiff = (diff / (1000 * 60 * 60));
        final StringBuilder textWhen = new StringBuilder();

        if ((cal.get(Calendar.DAY_OF_MONTH) - tempTim) <= 0 &&(cal.get(Calendar.DAY_OF_MONTH) - tempTim)>-1) {
                textWhen.append(
                        getResources().getString(R.string.today) + " " + formatHour(
                                cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE))+""+((am_pm==Calendar.AM)?"am":"pm"));
            } else if ((cal.get(Calendar.DAY_OF_MONTH) - tempTim) < 2 &&(cal.get(Calendar.DAY_OF_MONTH) - tempTim)>-1) {
                textWhen.append(getResources().getString(R.string.tomorrow) + " " + formatHour(
                        cal.get(Calendar.HOUR),
                        cal.get(Calendar.MINUTE))+""+ ((am_pm==Calendar.AM)?"am":"pm"));
            }
        if ((cal.get(Calendar.DAY_OF_MONTH) - tempTim) <= 4 && (cal.get(Calendar.DAY_OF_MONTH) - tempTim) >= 2) {
            textWhen.append(
                    getResources().getString(R.string.next) + " " + cal.get(Calendar.DAY_OF_MONTH) + "/"
                            + cal.get(Calendar.MONTH)
                            + "  " + formatHour(cal.get(Calendar.HOUR),
                            cal.get(Calendar.MINUTE)) + "" + ((am_pm == Calendar.AM) ? "am" : "pm"));
        }else if ((cal.get(Calendar.DAY_OF_MONTH) - tempTim) > 4) {
            textWhen.append(
                    "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
                            + "  " + formatHour(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)) + "" + ((am_pm == Calendar.AM) ? "am" : "pm"));
        }
        if ((cal.get(Calendar.DAY_OF_MONTH) - tempTim) <= -1) {
            textWhen.append(
                    "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
                            + "  " + formatHour(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)) + "" + ((am_pm == Calendar.AM) ? "am" : "pm"));
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                time.setText(textWhen);
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

    private void updateIconAvailability(ActivityAll activity) {
        long hoy = new Date().getTime();


        if (Long.parseLong(activity.getDatetime()) < hoy) {
            availabilityIcon.setImageResource(R.drawable.old_icon);
            statusText.setText(getResources().getString(R.string.not_available));
            join.setVisibility(View.GONE);
        } else {
            if ((!activity.getQuota().equals("") && Integer.parseInt(activity.getQuota()) != 0
                    && activity.getConfirmed().size() < Integer.parseInt(activity.getQuota()))
                    || Integer.parseInt(activity.getQuota()) == 0) {
                availabilityIcon.setImageResource(R.drawable.availabe_icon);
                statusText.setText(getResources().getString(R.string.available));

            } else {
                availabilityIcon.setImageResource(R.drawable.unavailabe_icon);
                statusText.setText(getResources().getString(R.string.full));
                join.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivitiesResponse(ArrayActivityAll arrayActivityAll) {
        timeOut=false;
        if(arrayActivityAll.getItemsE().size()!=0) {
            final ActivityAll activity = arrayActivityAll.getItemsE().get(0);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    wanna.setText(capitalize(activity.getTitle()));

                    //id For Menu
                    idUserForMenu = activity.getCreator();

                    String wantsText = getResources().getString(R.string.wants_to);
                    String wantsfull = wantsText + " " + activity.getTitle();
                    Spannable colorCityName = new SpannableString(wantsfull);
                    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                    final ForegroundColorSpan colorWant = new ForegroundColorSpan(Color.parseColor("#F7952C"));
                    colorCityName.setSpan(colorWant, wantsText.length(), wantsfull.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    colorCityName.setSpan(bss, wantsText.length(), wantsfull.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    detailsWants.setText(colorCityName);


                    name.setText(userName);
                    profile.setProfileId(activity.getCreator());
                    location.setText(activity.getLocation());
                    invites.setText("" + activity.getConfirmed().size() + "/" + activity.getQuota());
                    updateTextDateView(Long.parseLong(activity.getDatetime()));
                    likes.setText(activity.getClaps() + "\t" + getResources().getString(R.string.txtLikeTimeSheet));
                    commnet.setText(activity.getComment() + "\t" + getResources().getString(R.string.comment));
                    loadingLayout.setVisibility(View.GONE);
                    mainDetails.setVisibility(View.VISIBLE);
                    if (MainApplication.facebookuser != null) {
                        if (MainApplication.facebookuser != null && activity.getCreator()
                                .equals(MainApplication.facebookuser.getId())) {
                            notNow.setVisibility(View.GONE);
                            join.setVisibility(View.GONE);
                        }
                    }
                    updateIconAvailability(activity);
                    for (int i = 0; activity.getConfirmed().size() > i; i++) {
                        avatarLists.add(new AvatarItemInfo(activity.getConfirmed().get(i)));
                    }
                    //actualizo menu
                    if (idUser.equals(idUserForMenu)) {
                        toolbar.inflateMenu(R.menu.menu_details);
                    }
                    avatarAdapter.notifyDataSetChanged();

                }
            });
        }else{
            avatarAdapter.notifyDataSetChanged();
            CustomDialogFragment myDiag = CustomDialogFragment.newInstance(
                    CustomDialogFragment.SERVER_ERROR_ACTIVITY, DetailsActivity.this);
            myDiag.show(DetailsActivity.this.getFragmentManager(), "Diag");
        }

    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            succesRespon=DevuelParse(response.toString(),"succes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(succesRespon!=null){
            if(succesRespon.equals("001")) {
                CustomDialogFragment myDiag = CustomDialogFragment.newInstance(
                        CustomDialogFragment.CANCELACTIVITY, DetailsActivity.this);
                myDiag.show(DetailsActivity.this.getFragmentManager(), "Diag");
            }

        if(succesRespon.equals("00001")) {
                CustomDialogFragment myDiag = CustomDialogFragment.newInstance(CustomDialogFragment.JOINED,
                        DetailsActivity.this);
                myDiag.show(getFragmentManager(), "Diag");
            }
        }
    }
    @Override
    public void onFail(VolleyError error) {
                CustomDialogFragment myDiag = CustomDialogFragment.newInstance(
                        CustomDialogFragment.GENERAL_ERROR, DetailsActivity.this);
                myDiag.show(getFragmentManager(), "Diag");
            }

    public void chatFace(View v){
        chat();
    }
    private void chat (){
        Intent facebookIntent = getOpenFacebookIntent(this);
        startActivity(facebookIntent);

    }

    public Intent getOpenFacebookIntent(Context context) {
        Uri uri = Uri.parse("fb-messenger://user/");
       // Uri uri = Uri.parse("fb://chat/");
        //Uri uri = Uri.parse("fb://messaging/compose/");

        uri = ContentUris.withAppendedId(uri,Long.parseLong(idUser));

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana",0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW, uri);
                    //Uri.parse("fb-messenger://user/"+idUser)); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb-messenger://"));  //catches and opens a url to the desired page
        }
    }

    private void commentgo () {
        Intent intentComment = new Intent(this, CommentActivity.class);
        intentComment.putExtra("idactivity", idActivity);
        intentComment.putExtra("idAdminActivity", idUserForMenu);
        intentComment.putExtra("nameActivity", wanna.getText().toString());
        startActivity(intentComment);

    }

    private Dialog deleteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseas Eliminar:" + "\t" + wanna.getText().toString()+"?");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(5, 5, 5, 5);

        final TextView NombreBox = new TextView(this);
        NombreBox.setText("Notificaremos a todos los invitados");
        NombreBox.setTextSize(20);
        NombreBox.setPadding(8, 8, 8, 8);
        layout.addView(NombreBox);

        builder.setView(layout);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                CancelActivityRequest.excecute(getApplicationContext(), DetailsActivity.this, idUser, idActivity);

            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        return builder.show();

    }

    //Lo agrego para parsear datos el metodo DevuelParse, le paso el json lo convierto en jsonObjen y obtengo los datos
    public String DevuelParse(String TojsonObj, String buscar) throws JSONException {
        String text = "";

        JSONObject jsonObj = new JSONObject(TojsonObj);
        text = jsonObj.getString(buscar);

        return text;
    }

}


