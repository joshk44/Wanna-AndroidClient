package com.wanna.client.ui;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.android.Facebook;
import com.melnykov.fab.FloatingActionButton;
import com.wanna.client.R;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class HomeActivity extends ActionBarActivity implements MaterialTabListener {
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    MaterialTabHost tabHost;
    private Toolbar toolbar;
    private TextView title;
    private ImageView titleWanna;

    private Resources res;
    private FloatingActionButton fab;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private NotificationsFragment notificationsFragment;
    private SearchFragment seachFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        res = this.getResources();
        // init toolbar (old action bar)

        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        title = (TextView) this.findViewById(R.id.title_home);
        titleWanna = (ImageView) this.findViewById(R.id.title_logo);
        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        tabHost.setIconColor(getResources().getColor(R.color.primary_dark));
        tabHost.setAccentColor(getResources().getColor(R.color.primary));
        tabHost.setPrimaryColor(Color.WHITE);

        fab = (FloatingActionButton) this.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewActivity.class));
                HomeActivity.this.overridePendingTransition(R.anim.fade_in_bot,
                                                            R.anim.fade_out_bot);

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Session session = Session.getActiveSession();
                session.closeAndClearTokenInformation();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.menu_home);

        pager = (ViewPager) this.findViewById(R.id.pager);
        // init view pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setIconColor(getResources().getColor(R.color.primary_dark));
                switch (position) {
                case 0:
                    titleWanna.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                    break;
                case 1:
                    titleWanna.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText(getResources().getText(R.string.search).toString().toUpperCase());
                    break;
                case 2:
                    titleWanna.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText(getResources().getText(R.string.notifications));

                    break;
                case 3:
                    titleWanna.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText(getResources().getText(R.string.profile));
                    break;
                }
                tabHost.setSelectedNavigationItem(position);
            }
        });
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                tabHost.newTab()
                    .setIcon(getIcon(i))
                    .setTabListener(this)
            );
        }
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        notificationsFragment = new NotificationsFragment();
        seachFragment = new SearchFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
// when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
        tab.setIconColor(Color.DKGRAY);
        tab.setPrimaryColor(0xeeeeee);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {
            switch (num) {
            case 0:
                return HomeActivity.this.homeFragment;
            case 1:
                return HomeActivity.this.seachFragment;
            case 2:
                return HomeActivity.this.notificationsFragment;
            case 3:
                return HomeActivity.this.profileFragment;
            default:
                return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "tab";
        }
    }

    /*
    * It doesn't matter the color of the icons, but they must have solid colors
    */
    private Drawable getIcon(int position) {

        switch (position) {
        case 0:
            return res.getDrawable(R.drawable.ic_home);
        case 1:
            return res.getDrawable(R.drawable.ic_search);
        case 2:
            return res.getDrawable(R.drawable.ic_notifications);
        case 3:
            return res.getDrawable(R.drawable.ic_profile);
        default:
            return res.getDrawable(R.drawable.ic_wanna);

        }


    }
}
