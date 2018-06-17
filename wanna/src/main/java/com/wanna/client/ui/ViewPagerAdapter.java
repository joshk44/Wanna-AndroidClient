package com.wanna.client.ui;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanna.client.R;

public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;

    int[] flag;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, int[] flag) {
        this.context = context;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return flag.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    public float getPageWidth(int position) {
        return 1f;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imgflag;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);
        String helpText = "";
        switch (position) {
            case 0:
                helpText = "Receive proposals from your friends to do cool stuff";
                break;
            case 1:
                helpText = "Check out what your friends want to do";
                break;
            case 2:
                helpText = "Do whatever is in your mind";
                break;
            case 3:
                helpText = "Look up your friends or find new events";
                break;
        }


        ((TextView) itemView.findViewById(R.id.help_text)).setText(helpText);

        // Locate the ImageView in viewpager_item.xml
        imgflag = (ImageView) itemView.findViewById(R.id.flag);
        // Capture position and set to the ImageView
        imgflag.setImageResource(flag[position]);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
