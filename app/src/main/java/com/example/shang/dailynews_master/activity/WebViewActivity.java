package com.example.shang.dailynews_master.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shang.dailynews_master.config.ContentValues;
import com.example.shang.dailynews_master.domain.DetailsJson;
import com.example.shang.dailynews_master.R;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import static com.example.shang.dailynews_master.activity.MainActivity.KEYID;

/**
 * Created by shang on 2017/8/13.
 */

public class WebViewActivity extends Activity{

    private TextView mTitle;
    private WebView mWebView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_main);
        initData();
    }

    private void initData(){
        mTitle = (TextView) findViewById(R.id.detail_title);
        mWebView = (WebView) findViewById(R.id.detail_WebView);
        mImageView = (ImageView) findViewById(R.id.detail_imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.detail_progress);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString(KEYID);
        Log.i("xyy","bundle 得到的id"+id);
        String details = ContentValues.ZHIHU+id;
        getDetailsNewsJson(details);
    }

    private void getDetailsNewsJson(String details) {
        RequestParams params = new RequestParams(details);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                parseDetailsNew(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(),"获取新闻详情失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void parseDetailsNew(String result) {
        if (result!=null){
            Gson gson = new Gson();
            DetailsJson detailsJson = gson.fromJson(result,DetailsJson.class);
            String body = detailsJson.body;
            String title = detailsJson.title;
            String image = detailsJson.image;
            String []css = detailsJson.css;
            String []js = detailsJson.js;
            String id = detailsJson.id;

            Glide.with(this).load(image).into(mImageView);
            mTitle.setText(title);

            WebSettings settings = mWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            // 把所有内容放大webview等宽的一列中
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            //为body添加 头link(应该就添加了css),并且去掉<div class="headline">不然头部有一大空白
            String head = "<head>\n" +
                    "\t<link rel=\"stylesheet\" href=\""+css[0]+"\"/>\n" +
                    "</head>";
            String img = "<div class=\"headline\">";
            String html =head + body.replace(img," ");

            mWebView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
            //所有链接在webview中显示
            //设置开始加载网页 和加载完成的事件
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
    @Override
    protected void onDestroy() {
            if (mWebView != null) {
                mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                mWebView.clearHistory();

                ((ViewGroup) mWebView.getParent()).removeView(mWebView);
                mWebView.destroy();
                mWebView = null;
            }
            super.onDestroy();

        }

}
