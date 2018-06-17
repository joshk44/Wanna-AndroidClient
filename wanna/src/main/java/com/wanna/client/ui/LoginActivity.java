package com.wanna.client.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.wanna.client.R;

public class LoginActivity extends FragmentActivity {

    private LoginFragment loginFragment;

    // Declare Variables
    ViewPager viewPager;
    PagerAdapter adapter;
    int[] flag;
    LinearLayout llDots;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            loginFragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, loginFragment).commit();

        } else {
            // Or set the fragment from restored state info
            loginFragment = (LoginFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
            setContentView(R.layout.main);

        }

        flag = new int[]{R.drawable.help_1, R.drawable.help_2,
                R.drawable.help_3, R.drawable.help_4};

        llDots = (LinearLayout) findViewById(R.id.llDots);
        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new ViewPagerAdapter(this, flag);
        viewPager.setAdapter(adapter);

        for (int i = 0; i < adapter.getCount(); i++) {
            // if(i==5){i=0;}
            ImageButton imgDot = new ImageButton(this);
            imgDot.setTag(i);
            imgDot.setImageResource(R.drawable.dot_selector);
            imgDot.setBackgroundResource(0);
            imgDot.setPadding(5, 5, 5, 5);
            LayoutParams params = new LayoutParams(20, 20);
            imgDot.setLayoutParams(params);
            if (i == 0)
                imgDot.setSelected(true);

            llDots.addView(imgDot);
        }

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                Log.e("", "Page Selected is ===> " + pos);
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (i != pos) {
                        ((ImageView) llDots.findViewWithTag(i))
                                .setSelected(false);
                    }
                }
                ((ImageView) llDots.findViewWithTag(pos)).setSelected(true);
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }
}
