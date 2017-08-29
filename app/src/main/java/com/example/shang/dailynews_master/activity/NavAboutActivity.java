package com.example.shang.dailynews_master.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.shang.dailynews_master.R;

/**
 * Created by shang on 2017/8/29.
 */

public class NavAboutActivity extends Activity{
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_about_me);
        initView();
    }


    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.nav_about_me_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
