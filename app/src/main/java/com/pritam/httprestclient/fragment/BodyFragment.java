package com.pritam.httprestclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pritam.httprestclient.HomeActivity;
import com.pritam.httprestclient.R;

import java.util.HashMap;

public class BodyFragment extends Fragment {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_text, container, false);

        TextView tv = (TextView) view.findViewById(R.id.txt_respo_body);

        HashMap<String, Object> hm = HomeActivity.Response_hm;
        String s = "";
        if (hm != null) {
            s = hm.get("bodyRes").toString();
            if (s != null && s.length() > 0 ) {
                tv.setText(s);
            } else {
                tv.setVisibility(View.GONE);
            }
        } else {
            tv.setVisibility(View.GONE);
        }
        return view;
    }

}
