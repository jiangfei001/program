package com.qiuajy.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.web_view);

        WebSettings settings = webView.getSettings();
        // 开启 javascript 功能；原因：站点采用了 javascript
        settings.setJavaScriptEnabled(true);
        // 这句解决本地跨域问题，如果你的 PDF 文件在站点里，是不需要的，但是，我们一般情况是加载站点外部 PDF 文件
        settings.setAllowFileAccessFromFileURLs(true);

        // demo code
        /*
         * "file:///android_asset/pdf-website/index.html?pdf="这里是固定的，当然 `pdf-website`
         * 取决于开发者自己目录名称
         * 参数：pdf = 这里是 PDF 文件路径
         */
        //webView.loadUrl("file:///android_asset/pdf-website/index.html?pdf=/storage/emulated/0/1.pdf");
        webView.loadUrl("file:///android_asset/pdftest/test25.html");
    }
}
