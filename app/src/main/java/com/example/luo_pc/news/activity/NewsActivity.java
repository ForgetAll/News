package com.example.luo_pc.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.example.luo_pc.news.R;


/**
 * Created by luo-pc on 2016/5/21.
 */
public class NewsActivity extends Activity {

    private WebView wv_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);

        wv_news = (WebView) findViewById(R.id.wv_news);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        wv_news.loadUrl(url);


    }
}
