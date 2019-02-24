package com.pritam.httprestclient.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pritam.httprestclient.DBHelper;
import com.pritam.httprestclient.HomeActivity;
import com.pritam.httprestclient.R;
import com.pritam.httprestclient.Utility;

import java.io.IOException;
import java.util.ArrayList;
import android.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home_Fragment extends Fragment {

    View view;
    private ProgressDialog progress;
    private TextView mReq_meth_txt;
    private Spinner mRequest_method;
    private Spinner mRequest_http;
    private EditText mRequest_url;
    private TextView mConn_txt;
    private EditText mConnectTimeout;
    private TextView mWr;
    private EditText mWriteTimeout;
    private EditText mReadTimeout;
    private LinearLayout mPara_view;
    private ImageView mAdd_para;
    private LinearLayout mHeader_view;
    private ImageView mAdd_header;
    private Spinner mRequest_body_type;
    private EditText mEt_body, auth_user, auth_pass;
    private Switch sw_auth;
    private CardView respo_card;

    private TextView txt_respo_code, txt_respo_header, txt_respo_body;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mReq_meth_txt = (TextView) view.findViewById(R.id.req_meth_txt);
        mRequest_method = (Spinner) view.findViewById(R.id.request_method);
        mRequest_http = (Spinner) view.findViewById(R.id.request_http);
        mRequest_url = (EditText) view.findViewById(R.id.request_url);
        mConn_txt = (TextView) view.findViewById(R.id.conn_txt);
        mConnectTimeout = (EditText) view.findViewById(R.id.connectTimeout);
        mWr = (TextView) view.findViewById(R.id.wr);
        mWriteTimeout = (EditText) view.findViewById(R.id.writeTimeout);
        mReadTimeout = (EditText) view.findViewById(R.id.readTimeout);

        mAdd_para = (ImageView) view.findViewById(R.id.add_para);

        mAdd_header = (ImageView) view.findViewById(R.id.add_header);
        mRequest_body_type = (Spinner) view.findViewById(R.id.request_body_type);
        mEt_body = (EditText) view.findViewById(R.id.et_body);
        auth_user = (EditText) view.findViewById(R.id.auth_user);
        auth_pass = (EditText) view.findViewById(R.id.auth_pass);
        sw_auth = (Switch) view.findViewById(R.id.sw_auth);

        txt_respo_code = (TextView) view.findViewById(R.id.txt_respo_code);
        txt_respo_header = (TextView) view.findViewById(R.id.txt_respo_header);
        txt_respo_body = (TextView) view.findViewById(R.id.txt_respo_body);

        mPara_view = (LinearLayout) view.findViewById(R.id.para_view);
        mHeader_view = (LinearLayout) view.findViewById(R.id.header_view);
        respo_card = (CardView) view.findViewById(R.id.respo_card);

        ((Button) view.findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                httpRequest();
            }
        });

        ((ImageView) view.findViewById(R.id.add_header)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addHeader();
            }
        });

        ((ImageView) view.findViewById(R.id.add_para)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addParameter();
            }
        });

        sw_auth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    auth_user.setVisibility(View.VISIBLE);
                    auth_pass.setVisibility(View.VISIBLE);
                } else {
                    auth_user.setVisibility(View.GONE);
                    auth_pass.setVisibility(View.GONE);
                }
            }
        });


        //http://worldapp.ymngttz8kn.ap-south-1.elasticbeanstalk.com/restdb
        //https://fiberbase-4b621.firebaseio.com/photos.json
        //mRequest_url.setText("worldapp.ymngttz8kn.ap-south-1.elasticbeanstalk.com/restdb");
        // mRequest_url.setText("fiberbase-4b621.firebaseio.com/photos.json");
        //mRequest_url.setText("fiberbase-4b621.firebaseio.com/photos/images/1.json");
        // mRequest_url.setText("api.myjson.com/bins/103ozo");
        // https://www.w3schools.com/xml/cd_catalog.xml
        //mRequest_url.setText("https://www.w3schools.com/xml/note.xml");


        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }

        HashMap<String, Object> hm = HomeActivity.Request_hm;
        if(hm != null && hm.size() > 2) {
            String s = "";
            s = hm.get("connectTimeout").toString();
            if(s != null && s.length() > 0){
                mConnectTimeout.setText(s);
            }
            s = hm.get("writeTimeout").toString();
            if(s != null && s.length() > 0){
                mWriteTimeout.setText(s);
            }
            s = hm.get("readTimeout").toString();
            if(s != null && s.length() > 0){
                mReadTimeout.setText(s);
            }
            s = hm.get("request_url").toString();
            if(s != null && s.length() > 0){
                if (s.contains("https://")) {
                    s = s.replace("https://", "");
                    mRequest_http.setSelection(0);
                } else if (s.contains("http://")) {
                    s = s.replace("http://", "");
                    mRequest_http.setSelection(1);
                }
                url = s;
                mRequest_url.setText(url);
            }
            s = hm.get("request_method").toString();
            if(s != null && s.length() > 0){
                mRequest_method.setSelection(((ArrayAdapter<String>)mRequest_method.getAdapter()).getPosition(s));
            }
            s = hm.get("req_body").toString();
            if(s != null && s.length() > 0) {
                mEt_body.setText(s);
            }
            s = hm.get("req_body_type").toString();
            if(s != null && s.length() > 0) {
                mRequest_body_type.setSelection(((ArrayAdapter<String>)mRequest_body_type.getAdapter()).getPosition(s));
            }

            s = hm.get("headersList").toString();
            if(s != null && s.length() > 5) {
                headersList = new  ArrayList<HashMap<String, String>>();
                JsonObject jsonObject = (new JsonParser()).parse(s).getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                for(Map.Entry<String,JsonElement> entry : entrySet){
                    HashMap<String, String> hmo = new HashMap<>();
                    hmo.put(entry.getKey(), jsonObject.get(entry.getKey()).toString().replace("\"",""));
                    headersList.add(hmo);
                }
                    addHeader(headersList);
            } else {
                mRequest_http.setSelection(0);
                addHeader();
                addParameter();
            }

        } else {
            mRequest_http.setSelection(0);
            addHeader();
            addParameter();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return view;
    }


    private void addHeader() {
        //int count = mHeader_view.getChildCount();
        countHead = countHead + 1;
        final View childlayout = LayoutInflater.from(getActivity()).inflate(R.layout.key_value, mHeader_view, false);
        //Log.d("Head Tag -->>", countHead + "");
        childlayout.setTag("head_" + countHead + "");

        ImageView img = childlayout.findViewById(R.id.remove);
        img.setTag("img_head_" + countHead + "");
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHeader_view.removeView((View) childlayout);
                //Log.d("Head child getTag -->>", childlayout.getTag().toString() + " - "+childlayout.toString());
            }
        });

        mHeader_view.addView(childlayout);
    }


    private void addHeader(ArrayList<HashMap<String, String>> headersList) {
        for(int i = 0; i < headersList.size(); i++){
            countHead = mHeader_view.getChildCount();
            countHead = countHead + 1;
            final View childlayout = LayoutInflater.from(getActivity()).inflate(R.layout.key_value, mHeader_view, false);
            //Log.d("Head Tag -->>", countHead + "");
            childlayout.setTag("head_" + countHead + "");

            ((EditText) childlayout.findViewById(R.id.key)).setText(headersList.get(i).get("key"));
            ((EditText) childlayout.findViewById(R.id.value)).setText(headersList.get(i).get("value"));

            ImageView img = childlayout.findViewById(R.id.remove);
            img.setTag("img_head_" + countHead + "");
            img.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mHeader_view.removeView((View) childlayout);
                }
            });

            mHeader_view.addView(childlayout);
        }

        addHeader();
        addParameter();
    }

    int countParameter = 0, countHead = 0;

    private void addParameter() {
        //int count = mPara_view.getChildCount();
        countParameter = countParameter + 1;
        final View childlayout = LayoutInflater.from(getActivity()).inflate(R.layout.key_value, mPara_view, false);
        //Log.d("Para Tag -->>", countParameter + "");
        childlayout.setTag("para_" + countParameter + "");

        ImageView img = childlayout.findViewById(R.id.remove);
        img.setTag("img_para_" + countParameter + "");
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPara_view.removeView((View) childlayout);
                //Log.d("Para child getTag -->>", childlayout.getTag().toString() + " - "+childlayout.toString());
            }
        });
        mPara_view.addView(childlayout);
    }


    ArrayList<HashMap<String, String>> headersList;
    int connectTimeout = 10;
    int writeTimeout = 10;
    int readTimeout = 30;
    String request_method = "GET";
    String url;
    MediaType mediaType;

    private void httpRequest() {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        url = mRequest_url.getText().toString();
        if (url != null && url.length() > 0) {

            if (url.contains("https://")) {
                url = url.replace("https://", "");
                mRequest_http.setSelection(0);
            } else if (url.contains("http://")) {
                url = url.replace("http://", "");
                mRequest_http.setSelection(1);
            }

            url = mRequest_http.getSelectedItem().toString() + url + getParameter();

            String s = mConnectTimeout.getText().toString();
            if (s != null && s.length() > 0) {
                connectTimeout = Integer.valueOf(s);
                if (connectTimeout == 0) {
                    connectTimeout = 10;
                    mConnectTimeout.setText(String.valueOf(connectTimeout));
                }
            } else {
                connectTimeout = 10;
                mConnectTimeout.setText(String.valueOf(connectTimeout));
            }


            s = mWriteTimeout.getText().toString();
            if (s != null && s.length() > 0) {
                writeTimeout = Integer.valueOf(s);
                if (writeTimeout == 0) {
                    writeTimeout = 10;
                    mWriteTimeout.setText(String.valueOf(writeTimeout));
                }
            } else {
                writeTimeout = 10;
                mWriteTimeout.setText(String.valueOf(writeTimeout));
            }

            s = mReadTimeout.getText().toString();
            if (s != null && s.length() > 0) {
                readTimeout = Integer.valueOf(s);
                if (readTimeout == 0) {
                    readTimeout = 30;
                    mReadTimeout.setText(String.valueOf(readTimeout));
                }
            } else {
                readTimeout = 30;
                mReadTimeout.setText(String.valueOf(readTimeout));
            }

            String media_type = mRequest_body_type.getSelectedItem().toString();
            switch (media_type) {
                case "JSON": {
                    mediaType = MediaType.parse("application/json; charset=utf-8");
                }
                break;
                case "FORM": {
                    mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
                }
                break;
                case "Javascript": {
                    mediaType = MediaType.parse("application/javascript; charset=utf-8");
                }
                case "XML": {
                    mediaType = MediaType.parse("application/xml; charset=utf-8");
                }
                break;
                case "XML(text/xml)": {
                    mediaType = MediaType.parse("text/xml; charset=utf-8");
                }
                break;
                case "CSV": {
                    mediaType = MediaType.parse("text/csv; charset=utf-8");
                }
                break;
                case "HTML": {
                    mediaType = MediaType.parse("text/html; charset=utf-8");
                }
                break;
                case "CSS": {
                    mediaType = MediaType.parse("text/css; charset=utf-8");
                }
                break;
                default:
                case "Text": {
                    mediaType = MediaType.parse("text/plain; charset=utf-8");
                }
                break;

            }

            if (getHeaders()) {
                onRequest();
            }

        } else {
            Toast.makeText(getActivity(), "Enter valid URL", Toast.LENGTH_SHORT).show();
        }


    }

    private Boolean getHeaders() {
        headersList = new ArrayList();
        if (sw_auth.isChecked()) {
            String auth_userStr = auth_user.getText().toString();
            String auth_passStr = auth_pass.getText().toString();
            if (auth_userStr != null && auth_userStr.length() > 0) {
                if (auth_passStr != null && auth_passStr.length() > 0) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("key", "Authorization");
                    String encoding = Base64.encodeToString((auth_userStr + ":" + auth_passStr).getBytes(), android.util.Base64.DEFAULT);
                    hm.put("value", "Basic " + encoding);
                    headersList.add(hm);
                } else {
                    Toast.makeText(getActivity(), "Enter valid Authorization Password", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getActivity(), "Enter valid Authorization Username", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        int count = mHeader_view.getChildCount();
        for (int i = 1; i < count; i++) {
            View v = mHeader_view.getChildAt(i);
            String keyStr = ((EditText) v.findViewById(R.id.key)).getText().toString();
            String valueStr = ((EditText) v.findViewById(R.id.value)).getText().toString();
            if (keyStr != null && keyStr.length() > 0 && valueStr != null && valueStr.length() > 0) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("key", keyStr);
                hm.put("value", valueStr);
                headersList.add(hm);
            }

        }
        return true;
    }

    private String getParameter() {
        String s = "";

        int count = mPara_view.getChildCount();
        for (int i = 1; i < count; i++) {
            View v = mPara_view.getChildAt(i);
            String keyStr = ((EditText) v.findViewById(R.id.key)).getText().toString();
            String valueStr = ((EditText) v.findViewById(R.id.value)).getText().toString();
            if (keyStr != null && keyStr.length() > 0) {
                if (s.length() < 2) {
                    s += "?" + keyStr + "=" + valueStr;
                } else {
                    s += "&" + keyStr + "=" + valueStr;
                }
            }
        }
        if (s.endsWith("&=") || s.endsWith("?=")) {
            s = s.replace(s.substring(s.length() - 2), "");
        }
        return s;

    }

    private String bodyRes = "";

    private void onRequest() {
        if (Utility.isNetworkAvaiable(getActivity())) {
            try {

                Headers.Builder headersBuilder = new Headers.Builder();
                for (int i = 0; i < headersList.size(); i++) {
                    headersBuilder.add(headersList.get(i).get("key"), headersList.get(i).get("value"));
                }
                Headers headhm = headersBuilder.build();

                String postString = mEt_body.getText().toString();
                if (!(postString != null && postString.length() > 0)) {
                    postString = "";
                }

                RequestBody body = RequestBody.create(mediaType, postString);
                Request.Builder requestBuilder = new Request.Builder().url(url);

                Request request = null;

                request_method = mRequest_method.getSelectedItem().toString();
                switch (request_method) {
                    case "GET": {
                        if (headersList.size() > 0) {
                            request = new Request.Builder()
                                    .headers(headhm)
                                    .url(url)
                                    .build();
                        } else {
                            request = new Request.Builder()
                                    .url(url)
                                    .build();
                        }
                    }
                    break;
                    case "HEAD": {
                        if (headersList.size() > 0) {
                            request = new Request.Builder()
                                    .headers(headhm)
                                    .url(url)
                                    .head()
                                    .build();
                        } else {
                            request = new Request.Builder()
                                    .url(url)
                                    .head()
                                    .build();
                        }
                    }
                    break;
                    case "CREATE": {
                        if (headersList.size() > 0) {
                            request = new Request.Builder()
                                    .headers(headhm)
                                    .url(url)
                                    .method("CREATE", body)
                                    .build();
                        } else {
                            request = new Request.Builder()
                                    .url(url)
                                    .method("CREATE", body)
                                    .build();
                        }
                    }
                    break;
                    case "POST": {
                        requestBuilder.post(body);
                        if (headersList.size() > 0) {
                            requestBuilder.headers(headhm);
                        }
                        request = requestBuilder.build();
                    }
                    break;
                    case "PUT": {
                        requestBuilder.put(body);
                        if (headersList.size() > 0) {
                            requestBuilder.headers(headhm);
                        }
                        request = requestBuilder.build();
                    }
                    break;
                    case "DELETE": {
                        if (postString.length() > 0) {
                            requestBuilder.delete(body);
                        } else {
                            requestBuilder.delete();
                        }
                        if (headersList.size() > 0) {
                            requestBuilder.headers(headhm);
                        }
                        request = requestBuilder.build();
                    }
                    break;
                    case "PATCH": {
                        requestBuilder.patch(body);
                        if (headersList.size() > 0) {
                            requestBuilder.headers(headhm);
                        }
                        request = requestBuilder.build();
                    }
                    break;
                }

                if(request != null) {
                    Log.d("request -->>", request.toString());
                }


                progress = new ProgressDialog(getActivity(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);//Theme.AppCompat.Light android.R.style.Theme_Holo_Light_Dialog
                //progress.setTitle("Please wait");
                progress.setMessage("Please wait ...");
                progress.setCancelable(false);
                progress.show();


                DBHelper dbHelper = new DBHelper(getActivity());
                HashMap<String,Object> dataal = new HashMap<>();
                dataal.put("request_url",url);
                if (url.contains("https://")) {
                    dataal.put("request_name", url.replace("https://", ""));
                } else if (url.contains("http://")) {
                    dataal.put("request_name", url.replace("https//", ""));
                }
                dataal.put("request_method",request_method);
                dataal.put("headersList",headersList.toString());
                dataal.put("req_body",postString);
                dataal.put("req_body_type",mRequest_body_type.getSelectedItem().toString());
                dataal.put("connectTimeout", connectTimeout);
                dataal.put("writeTimeout", writeTimeout);
                dataal.put("readTimeout", readTimeout);

                HomeActivity.Response_hm = dataal;

                dbHelper.createHistory(dataal, false);
                dbHelper.close();

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                        .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                        .readTimeout(readTimeout, TimeUnit.SECONDS)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException ioe) {
                        call.cancel();
                        ioe.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Utility.alertDialog(getActivity(), "Request Failed", Utility.getStackTrace(ioe));
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                try {
                                    bodyRes = response.body().string();

//                                    ResponseBody responseBody = response.body();
//                                    BufferedSource source = responseBody.source();
//                                    source.request(Long.MAX_VALUE); // request the entire body.
//                                    Buffer buffer = source.buffer();
//                                    // clone buffer before reading from it
//                                    bodyRes = buffer.clone().readString(Charset.forName("UTF-8"));
//                                    Log.d("TAG -->", bodyRes);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("response -->> ", "Exception " + e.toString());
                                    bodyRes = e.toString();
                                }
                                if (response.isSuccessful()) {
                                    responseHandler(response);
                                } else {
                                    Log.d("response Issue-->>", response.toString());
                                }
                                respo_card.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                });
            } catch (Exception e) {
                progress.dismiss();
                e.printStackTrace();
                Utility.alertDialog(getActivity(), "Exception", Utility.getStackTrace(e));
            }
        } else {
            Utility.alertDialog(getActivity(), "Offline", "No Internet Connection");
        }
    }


    private void responseHandler(final Response response) {
        Log.d("response toString-->>", response.toString());


        int st = response.code();
        HomeActivity.Response_hm.put("responsecode",st);
        HomeActivity.Response_hm.put("responsemsg",response.message());
        String contentType = response.body().contentType().toString();
        HomeActivity.Response_hm.put("contentType",contentType);
        HomeActivity.Response_hm.put("responseheaders",response.headers().toString());

        if ((contentType.contains("html") || contentType.contains("javascript"))) {
            HomeActivity.Response_hm.put("bodyRes",bodyRes);
            HomeActivity.Response_hm.put("bodyType","Web");
        } else if (contentType.contains("json") ) {
            try {
                Gson gson = new Gson();
                Map<String, Object> mapParent = new HashMap<String, Object>();
                List<String> mapArray = new ArrayList<>();
                // mapParent = (Map<String, Object>) gson.fromJson(bodyRes, mapParent.getClass());
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(bodyRes);
                HomeActivity.Response_hm.put("bodyRes",bodyRes);
                if (jsonElement.isJsonObject()) {
                    mapParent = (Map<String, Object>) gson.fromJson(bodyRes, mapParent.getClass());
                    bodyRes = (new GsonBuilder().setPrettyPrinting().create()).toJson(mapParent);
                } else if (jsonElement.isJsonArray()) {
                    mapArray = (List<String>) gson.fromJson(bodyRes, mapArray.getClass());
                    bodyRes = (new GsonBuilder().setPrettyPrinting().create()).toJson(mapArray);
                }
                HomeActivity.Response_hm.put("bodyResJSON",bodyRes);
                HomeActivity.Response_hm.put("bodyType","JSON");
            } catch (Exception e) {
                e.printStackTrace();
                HomeActivity.Response_hm.put("bodyRes",bodyRes);
                HomeActivity.Response_hm.put("bodyType","text");
            }
        } else if (contentType.contains("xml") ) {
            HomeActivity.Response_hm.put("bodyResJSON",bodyRes);
            HomeActivity.Response_hm.put("bodyRes",bodyRes);
            HomeActivity.Response_hm.put("bodyType","XML");
        }
        else {
            HomeActivity.Response_hm.put("bodyRes",bodyRes);
            HomeActivity.Response_hm.put("bodyType","text");
        }

        Response_Fragment fragment = new Response_Fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    }

    private void responseHandler_old(final Response response) {

        Log.d("response toString-->>", response.toString());
//        Log.d("response code -->>", response.code() + " " + response.message());
//        Log.d("response headers -->>", response.headers().toString());
//        Log.d("response type-->>", response.body().contentType().toString());

        int st = response.code();
        String contentType = "";
        if (st > 0) {
            contentType = response.body().contentType().toString();
            txt_respo_code.setText(response.code() + " " + response.message() + "\n" + contentType);
        } else {
            txt_respo_code.setText("");
        }

        String s = response.headers().toString();
        if (s != null && s.length() > 0) {
            txt_respo_header.setText(s);
        } else {
            txt_respo_header.setText("");
        }


        WebView wv = (WebView) view.findViewById(R.id.webview_resp);
        if ((contentType.contains("html") || contentType.contains("javascript"))) {
            //wv.loadDataWithBaseURL(null, bodyRes, response.body().contentType().type().toString(), response.body().contentType().charset().toString(), null);
            wv.loadData(bodyRes, "text/html", "UTF-8");
            txt_respo_body.setText("");
            txt_respo_body.setVisibility(View.GONE);
            wv.setVisibility(View.VISIBLE);
        } else if (contentType.contains("json") ){
            try{
                Gson gson = new Gson();
                Map<String, Object> mapParent = new HashMap<String, Object>();
                List<String> mapArray = new ArrayList<>();
                // mapParent = (Map<String, Object>) gson.fromJson(bodyRes, mapParent.getClass());
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(bodyRes);
                if (jsonElement.isJsonObject()) {
                    mapParent = (Map<String, Object>) gson.fromJson(bodyRes, mapParent.getClass());
                    bodyRes = (new GsonBuilder().setPrettyPrinting().create()).toJson(mapParent);
                } else if (jsonElement.isJsonArray()) {
                    mapArray = (List<String>) gson.fromJson(bodyRes, mapArray.getClass());
                    bodyRes = (new GsonBuilder().setPrettyPrinting().create()).toJson(mapArray);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            txt_respo_body.setText(bodyRes);
            txt_respo_body.setVisibility(View.VISIBLE);
            wv.setVisibility(View.GONE);
        } else
        //if (contentType.contains("json") || contentType.contains("xml"))
        {
            txt_respo_body.setText(bodyRes);
            txt_respo_body.setVisibility(View.VISIBLE);
            wv.setVisibility(View.GONE);
        }

    }


}

