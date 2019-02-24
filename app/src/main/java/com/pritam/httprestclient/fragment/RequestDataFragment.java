package com.pritam.httprestclient.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pritam.httprestclient.HomeActivity;
import com.pritam.httprestclient.R;

import java.util.HashMap;

public class RequestDataFragment extends Fragment {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_req, container, false);


        HashMap<String, Object> hm = HomeActivity.Response_hm;
        String s = "";
        if(hm != null){

            s = hm.get("request_url").toString();
            if(s != null && s.length() > 0){
                ((TextView) view.findViewById(R.id.txt_req_url)).setText(s);
            } else {
                ((TextView) view.findViewById(R.id.txt_req_url)).setText("");
            }

            s = hm.get("req_body").toString();
            if(s != null && s.length() > 0){
                ((TextView) view.findViewById(R.id.txt_req_body)).setText(s);
            } else {
                ((TextView) view.findViewById(R.id.txt_req_body)).setText("");
            }

            s = hm.get("headersList").toString();
            if(s != null && s.length() > 2){
                ((TextView) view.findViewById(R.id.txt_req_header)).setText(s);
            } else {
                ((TextView) view.findViewById(R.id.txt_req_header)).setText("");
            }

            s = hm.get("responsecode").toString();
            if(s != null && s.length() > 0){
                String s1 = hm.get("responsemsg").toString();
                if(s1 != null && s1.length() > 0) {
                    ((TextView) view.findViewById(R.id.txt_respo_code)).setText(s + " " + s1);
                } else {
                    ((TextView) view.findViewById(R.id.txt_respo_code)).setText(s);
                }
            } else {
                ((TextView) view.findViewById(R.id.txt_respo_code)).setText("");
            }

            /*s = hm.get("responsemsg").toString();
            if(s != null && s.length() > 0){
                ((TextView) view.findViewById(R.id.txt_respo_msg)).setText(s);
            } else {
                ((TextView) view.findViewById(R.id.txt_respo_msg)).setText("");
            }*/

            s = hm.get("responseheaders").toString();
            if(s != null && s.length() > 0){
                ((TextView) view.findViewById(R.id.txt_respo_header)).setText(s);
            } else {
                ((TextView) view.findViewById(R.id.txt_respo_header)).setText("");
            }
        }

        return view;
    }


}
