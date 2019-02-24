package com.pritam.httprestclient.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.pritam.httprestclient.DBHelper;
import com.pritam.httprestclient.HomeActivity;
import com.pritam.httprestclient.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class History_Fragment extends Fragment {

    View view;
    ListView listView;
    CustomListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collection, container, false);
//        TextView textView = (TextView) view.findViewById(R.id.textView);
//        textView.setText("History");
        setHasOptionsMenu(true);

        listView = (ListView) view.findViewById(R.id.listview);

        getListView();

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                // Do onlick on menu action here
                DBHelper dbHelper = new DBHelper(getActivity());
                dbHelper.deleteHistory(null);
                dbHelper.close();
                getListView();
                return true;
        }
        return false;
    }

    private void getListView() {

        DBHelper dbHelper = new DBHelper(getActivity());
        final List<HashMap<String, Object>> aList = dbHelper.getHistory("");
        dbHelper.close();

        adapter = new CustomListAdapter(getActivity(), aList);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                openRequest(aList.get(position));
            }
        });

    }

    private void openRequest(HashMap<String, Object> hm) {
        HomeActivity.Request_hm = hm;
        Home_Fragment fragment = new Home_Fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


}

