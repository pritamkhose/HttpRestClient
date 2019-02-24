package com.pritam.httprestclient.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pritam.httprestclient.HomeActivity;
import com.pritam.httprestclient.R;

import java.util.HashMap;

public class Response_Fragment extends Fragment {

    View view;
    ViewPager mViewPager;
    TabLayout tabLayout;
    public static Boolean webHTML = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.response_card, container, false);

        HashMap<String, Object> hm = HomeActivity.Response_hm;
        String s = "";
        if (hm != null) {
            s = hm.get("bodyType").toString();
            if (s != null && s.length() > 0 && s.equalsIgnoreCase("Web")) {
                webHTML = true;
            }
        }

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Request").setTag("Request"));
        tabLayout.addTab(tabLayout.newTab().setText("Body").setTag("Body"));
        if(webHTML){
            tabLayout.addTab(tabLayout.newTab().setText("HTML").setTag("HTML"));
        }
        else if(s != null && s.equals("JSON")){
            tabLayout.addTab(tabLayout.newTab().setText("JSON").setTag("Text"));
        }
        else if(s != null && s.equals("XML")){
            tabLayout.addTab(tabLayout.newTab().setText("XML").setTag("Text"));
        }
        //    tabLayout.addTab(tabLayout.newTab().setText("Text").setTag("Text"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount()); //getSupportFragmentManager
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}

