package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beefe.picker.cityBean.PriBean;
import com.beefe.picker.cityBean.CityBean;
import com.beefe.picker.cityBean.AllBean;
import com.beefe.picker.dao.CityDao;
import com.google.gson.Gson;
import com.sgs.next.comcourier.sfservice.fourlevel.FourlevelAddressUtil;

import java.util.LinkedList;

public class MainActivity extends Activity {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // REMOTE RESOURCE
        // mWebView.loadUrl("https://example.com");
        // mWebView.setWebViewClient(new MyWebViewClient());

        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");


        new Thread(new Runnable() {
            @Override
            public void run() {
                CityDao cityDao = new CityDao(MainActivity.this);
                AllBean cb = cityDao.selectProvinceAll();

                Gson g = new Gson();
                String sb = g.toJson(cb);
                Log.e("cb", "cb" + sb);

                for (int i = 0; i < cb.PriBeans.size(); i++) {
                    PriBean dc = cb.PriBeans.get(i);
                    for (int t = 0; t < dc.city.size(); t++) {
                        com.beefe.picker.cityBean.CityBean bc = dc.city.get(t);
                        for (int dt = 0; dt < bc.area.size(); dt++) {
                            String b = bc.area.get(dt);
                            LinkedList arrayList = new LinkedList();
                            arrayList.add(dc.name);
                            arrayList.add(bc.name);
                            arrayList.add(b);
                            Log.e("BB", dc.name);
                            String att = FourlevelAddressUtil.splitAddressFuzzy1(arrayList);
                            Log.e("KK", att);
                        }
                    }
                }

                String[] att = FourlevelAddressUtil.splitAddressFuzzy("香港特别行政区香港特别行政区湾仔区灣仔軒尼詩道226號",true);
                Log.e("BB", att.toString());

            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
