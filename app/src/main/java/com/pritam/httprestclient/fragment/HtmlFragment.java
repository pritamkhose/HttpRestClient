package com.pritam.httprestclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;

import com.pritam.httprestclient.HomeActivity;
import com.pritam.httprestclient.R;

import java.util.HashMap;

public class HtmlFragment extends Fragment {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_web, container, false);

        WebView wv = (WebView) view.findViewById(R.id.webview_resp);

        HashMap<String, Object> hm = HomeActivity.Response_hm;
        String s = "";
        if (hm != null) {
            s = hm.get("bodyType").toString();
            if (s != null && s.length() > 0 && s.equalsIgnoreCase("web")) {
                s = hm.get("bodyRes").toString();
                if (s != null && s.length() > 0) {
                    wv.loadData(s, "text/html", "UTF-8");
                } else {
                    wv.setVisibility(View.GONE);
                }
            } else {
                wv.setVisibility(View.GONE);
            }
        }

        return view;
    }

}
