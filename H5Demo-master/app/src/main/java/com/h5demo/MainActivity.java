package com.h5demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by mfp on 15-12-22.
 */
public class MainActivity extends Activity implements GestureDetector.OnGestureListener, View
        .OnClickListener {
/*    @InjectView(R.id.mProgress)
    ProgressBar mProgress;*/

    @InjectView(R.id.webview)
    WebView mWebView;

   /* @InjectView(R.id.layout_back)
    RelativeLayout layout_back;
*/
  /*  @InjectView(R.id.img_browser_back)
    ImageView img_browser_back;

    @InjectView(R.id.img_browser_next)
    ImageView img_browser_next;*/


    private String url;

    private GestureDetector detector;
    private int flingWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        /*layout_back.setOnClickListener(this);*/
       /* img_browser_back.setOnClickListener(this);
        img_browser_next.setOnClickListener(this);*/
        initweb();
        // url = "file:///android_asset/resources/project_9/493532b60cbc7613404c02ebf1453b8b.html";
        url = "https://cfs-api-sit.sf-financial.com:7443/entry/index.html?utm_source=fxg#/introduce";

        mWebView.addJavascriptInterface(new JsInterface(), "android");
        //url="http://mclasstest.club/test.html";
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //showToast(content);
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_LONG).show();
                String method = "javascript:testResult('" + content + "')";
                mWebView.loadUrl(method);
            }
        }
    }


    private class JsInterface {
        // 安卓原生与h5互调方法定义
        @JavascriptInterface //js接口声明
        public void takeScan() {
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class); //打开扫一扫
            startActivityForResult(intent, 1);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initweb() {
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
   /*     mWebView.setWebViewClient(new InnerWebViewClient());*/
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不另跳浏览器
                // 在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    mWebView.stopLoading();
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        //        if (!NetUtil.checkNet(MainActivity.this)) {
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        //        } else {
        //            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        //        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
      /*      case R.id.layout_back:
                finish();
                break;*/
          /*  case R.id.img_browser_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
                break;
            case R.id.img_browser_next:
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
                break;*/
        }
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            return false;
        }
        if (e2.getX() - e1.getX() > flingWidth && Math.abs(velocityX) > 200) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return false;
    }



 /*   private class InnerWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        *//**
         * 处理ssl请求
         *//*
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        *//**
         * 页面载入完成回调
         *//*
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("" +
                    "javascript:try{autoplay();}" +
                    "catch(e){}");//播放视频
            uichange();
        }
    }*/


    public void uichange() {
       /* if (mWebView.canGoBack()) {
            img_browser_back.setImageResource(R.drawable.icon_browser_back);
        } else {
            img_browser_back.setImageResource(R.drawable.icon_browser_unback);
        }
        if (mWebView.canGoForward()) {
            img_browser_next.setImageResource(R.drawable.icon_browser_next);
        } else {
            img_browser_next.setImageResource(R.drawable.icon_browser_unnext);
        }*/
    }

    @Override
    protected void onPause() {
        if (null != mWebView) {
            mWebView.onPause();
        }
        super.onPause();
    }
}
