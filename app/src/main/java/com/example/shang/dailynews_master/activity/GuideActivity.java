package com.example.shang.dailynews_master.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.shang.dailynews_master.R;

/**
 * Created by shang on 2017/7/25.
 */

public class GuideActivity extends Activity {

    private ImageView iv_guide_rotate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_main);

        iv_guide_rotate = (ImageView) findViewById(R.id.iv_guide_rotate);

        RotateAnimation animation = new RotateAnimation(0,1080, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animin,R.anim.animout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        iv_guide_rotate.startAnimation(animation);



    }
}
