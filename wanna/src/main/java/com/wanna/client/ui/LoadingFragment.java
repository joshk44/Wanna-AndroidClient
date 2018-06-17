package com.wanna.client.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanna.client.R;
import com.wanna.client.ui.custom.MyAnimationView;

public class LoadingFragment extends Fragment {



    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_loading, container, false);
        MyAnimationView anim_view = (MyAnimationView) rootView.findViewById(R.id.anim_view);
        anim_view.loadAnimation("loading", 24, true,30);
        return rootView;
    }
}
