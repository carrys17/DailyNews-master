package com.example.shang.dailynews_master.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shang.dailynews_master.R;
import com.example.shang.dailynews_master.adapter.NewsViewAdapter;
import com.example.shang.dailynews_master.config.ContentValues;
import com.example.shang.dailynews_master.domain.WeatherJson;
import com.example.shang.dailynews_master.domain.ZhihuJson;
import com.example.shang.dailynews_master.utils.GlideImageLoader;
import com.example.shang.dailynews_master.utils.LocationHelper;
import com.example.shang.dailynews_master.utils.NewsHelper;
import com.example.shang.dailynews_master.utils.SpUtils;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.example.shang.dailynews_master.config.ContentValues.DATE;
import static com.example.shang.dailynews_master.config.ContentValues.ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView mTextView;

    private TextView tv_temp,tv_city,tv_weather,tv_wind,tv_humidity,tv_airCondition;
    private TextView tv_tomorrow_1,tv_week_1,tv_weather_1,tv_temp_1;
    private TextView tv_tomorrow_2,tv_week_2,tv_weather_2,tv_temp_2;
    private ImageView iv_weather_1,iv_weather_2;

    private ImageView refresh;

    private Banner mBanner;

    private RecyclerView mRecyclerView;

    private ImageView shareImage;

    public static final String KEYID = "com.example.shang.id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBanner = (Banner) findViewById(R.id.banner);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        shareImage = (ImageView) findViewById(R.id.share);
        shareImage.setOnClickListener(this);
        getNewsJson(ContentValues.LASTER,this);

        initDrawerLayout();

        refreshLocationAndWeather();
    }



    private void refreshLocationAndWeather() {
        // 获取地理位置
        LocationHelper.getLocation(getApplicationContext());
        // 获取城市
        String city = SpUtils.getString(getApplicationContext(), ContentValues.CITY, "");
        String [] s = city.split("市");
        // 根据获取天气预报
        try {
            getWeatherJson(s[0]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_layout);
        mNavigationView.inflateHeaderView(R.layout.layout_main_left);
        View headerView = mNavigationView.getHeaderView(0);

        headerView.findViewById(R.id.iv_refresh).setOnClickListener(this);
        headerView.findViewById(R.id.tv_home).setOnClickListener(this);
        headerView.findViewById(R.id.tv_deedback).setOnClickListener(this);
        headerView.findViewById(R.id.tv_about).setOnClickListener(this);
        headerView.findViewById(R.id.tv_exit).setOnClickListener(this);
        initView(headerView);
    }

    private void initView(View view) {
        refresh = (ImageView)view.findViewById(R.id.iv_refresh);

        // 今天
        tv_temp = (TextView) view.findViewById(R.id.tv_temp);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_weather = (TextView) view.findViewById(R.id.tv_weather);
        tv_wind = (TextView) view.findViewById(R.id.tv_wind);
        tv_humidity = (TextView) view.findViewById(R.id.tv_humidity);
        tv_airCondition= (TextView) view.findViewById(R.id.tv_airCondition);
        iv_weather_1 = (ImageView) view.findViewById(R.id.iv_weather_image_1);
        // 明天
        tv_tomorrow_1 = (TextView) view.findViewById(R.id.tv_tomrrow_1);
        tv_week_1 = (TextView) view.findViewById(R.id.tv_week_1);
        tv_weather_1 = (TextView) view.findViewById(R.id.tv_weather_1);
        tv_temp_1 = (TextView) view.findViewById(R.id.tv_temp_1);
        // 后天
        tv_tomorrow_2 = (TextView) view.findViewById(R.id.tv_tomrrow_2);
        tv_week_2 = (TextView) view.findViewById(R.id.tv_week_2);
        tv_weather_2 = (TextView) view.findViewById(R.id.tv_weather_2);
        tv_temp_2 = (TextView) view.findViewById(R.id.tv_temp_2);
        iv_weather_2 = (ImageView) view.findViewById(R.id.iv_weather_image_2);

    }

    @Override
    public void onClick(final View v) {
        //mDrawerLayout.closeDrawers();
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (v.getId()) {
                    // 主页的分享功能
                    case R.id.share:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT,"发现一个有趣的资讯类应用天天日报，快来一起看看吧~~");
                        shareIntent.setType("text/plain");
                        startActivity(Intent.createChooser(shareIntent,"分享"));
                        break;

                    // 天气刷新
                    case R.id.iv_refresh:
                        RotateAnimation animation = new RotateAnimation(0,-720, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                        animation.setDuration(1500);
                        refresh.startAnimation(animation);
                        refreshLocationAndWeather();
                        break;
                    // 返回主页
                    case R.id.tv_home:
                        mDrawerLayout.closeDrawers();
                        break;
                    // 意见反馈
                    case R.id.tv_deedback:
                        Intent intent = new Intent(MainActivity.this,NavDeedBackActivity.class);
                        startActivity(intent);
                        break;
                    // 关于项目
                    case R.id.tv_about:
                        Intent intent1 = new Intent(MainActivity.this,NavAboutActivity.class);
                        startActivity(intent1);
                        break;
                    // 退出应用
                    case R.id.tv_exit:
                        System.exit(0);
                        break;
                }
            }
        }, 260);
    }



    public void getWeatherJson(String city) throws UnsupportedEncodingException {

        String s = ContentValues.WEATHER;
        try {
            s += URLEncoder.encode(city,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams(s);

        x.http().get(params, new Callback.CommonCallback<String >() {
            @Override
            public void onSuccess(String result) {
                Log.i("xyz","result = "+result);
                parseWeatherJson(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("xyz","请求失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void parseWeatherJson(String result) {
        Gson gson = new Gson();
        WeatherJson weatherJson = gson.fromJson(result,WeatherJson.class);
        WeatherJson.WeatherResultJson weatherResultJson = weatherJson.result.get(0);

        // 今天
        tv_temp.setText(weatherResultJson.temperature);
        tv_city.setText(weatherResultJson.city);
        tv_weather.setText(weatherResultJson.weather);
        tv_wind.setText(weatherResultJson.wind);
        tv_humidity.setText(weatherResultJson.humidity);
        tv_airCondition.setText(String.format("空气质量：%s", weatherResultJson.airCondition));

        // 明天
        WeatherJson.WeatherFeatureJson tomorrow = weatherResultJson.future.get(1);
        String[] strSplit = tomorrow.date.split("-");   //2017-07-27
        String date = strSplit[2]+"号";
        tv_tomorrow_1.setText(date);
        tv_week_1.setText(tomorrow.week);
        tv_weather_1.setText(tomorrow.dayTime);
        tv_temp_1.setText(tomorrow.temperature);
        setWeatherImage(tomorrow.dayTime,iv_weather_1);

        // 后天
        WeatherJson.WeatherFeatureJson afterTomorrow = weatherResultJson.future.get(2);
        String[] strSplit2 = afterTomorrow.date.split("-");
        String date2 = strSplit2[2]+"号";
        tv_tomorrow_2.setText(date2);
        tv_week_2.setText(afterTomorrow.week);
        tv_weather_2.setText(afterTomorrow.dayTime);
        tv_temp_2.setText(afterTomorrow.temperature);
        setWeatherImage(afterTomorrow.dayTime,iv_weather_2);
    }

    private void setWeatherImage(String weather, ImageView image) {
        if(weather.contains("晴"))image.setImageResource(R.drawable.icon_sunny);
        if(weather.contains("阴"))image.setImageResource(R.drawable.icon_overcast);
        if(weather.contains("云"))image.setImageResource(R.drawable.icon_cloudy);
        if(weather.contains("雨"))image.setImageResource(R.drawable.icon_ice_rain);
        if(weather.contains("暴"))image.setImageResource(R.drawable.icon_rain);
        if(weather.contains("雷"))image.setImageResource(R.drawable.icon_t_storm);
        if(weather.contains("雪")||weather.contains("雹"))image.setImageResource(R.drawable.icon_snow);
        if(weather.contains("雾")||weather.contains("霾"))image.setImageResource(R.drawable.icon_fog);
        if(weather.contains("沙")||weather.contains("尘"))image.setImageResource(R.drawable.icon_sand);
    }



    public void getNewsJson(String s, final Context context){

        Log.i("xyz","s ="+s);
        RequestParams params = new RequestParams(s);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("xyz","result = "+result);
                parseNews(result,context);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("xyz","新闻请求出错");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void parseNews(String result, final Context context) {
        Gson gson = new Gson();
        ZhihuJson zhihuJson = gson.fromJson(result, ZhihuJson.class);
        // 日期
        String date = zhihuJson.date;
        SpUtils.putString(context.getApplicationContext(), DATE, date);
        // 头条新闻
        List<ZhihuJson.TopStories>top_stories = zhihuJson.top_stories;

        final List<String> topNewsId = new ArrayList<>();
        List<String> topNewsTitle = new ArrayList<>();
        List<String> topNewsImage = new ArrayList<>();
        for (int i = 0; i < top_stories.size(); i++) {
            topNewsId.add(top_stories.get(i).id);
            topNewsTitle.add(top_stories.get(i).title);
            topNewsImage.add(top_stories.get(i).image);
            Log.i("xyz", "imagetop = " + top_stories.get(i).image);
        }

        // 新闻列表
        List<ZhihuJson.Stories> stories = zhihuJson.stories;
        List<String> newsId = new ArrayList<>();
        List<String> newsTitle = new ArrayList<>();
        List<ArrayList<String>> newsImage = new ArrayList<>();

        for (int i = 0; i < stories.size(); i++) {
            newsId.add(stories.get(i).id);
            newsTitle.add(stories.get(i).title);
            newsImage.add(stories.get(i).images);
            Log.i("xyz", "titlemain =" + stories.get(i).getTitle());
            Log.i("xyz", "imagemain = " + stories.get(i).getImages().get(0));
        }

        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(topNewsImage);
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        mBanner.setBannerTitles(topNewsTitle);
        mBanner.setBannerAnimation(Transformer.Default);

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String id = topNewsId.get(position);
                Bundle bundle = new Bundle();
                bundle.putString(KEYID,id);
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mBanner.start();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final NewsViewAdapter adapter = new NewsViewAdapter(mRecyclerView, this, stories);
        // 加载更多
        adapter.setLoadingMore(new NewsViewAdapter.OnLoadingMore() {
            NewsHelper helper = new NewsHelper();
            @Override
            public void onLoadMore() {
                adapter.setLoading(true);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // 加载更多新闻
                            helper.getMoreNewsJson(context);
                            Thread.sleep(1000); // 这个睡眠一秒很灵性啊，后台加载列表需要时间的
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<ZhihuJson.Stories > list = helper.getMoreStories();
                                for (int i = 0;i<list.size();i++ ){
                                    adapter.addData(list.get(i));
                                }
                                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                        if (newState == RecyclerView.SCROLL_STATE_IDLE){
                                            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                            int itemCount = manager.getItemCount();
                                            int lastPosition = manager.findLastVisibleItemPosition(); // 最后一项
                                            if (lastPosition == itemCount -1){
                                                adapter.setCanLoadMore(false);
                                            }
                                        }
                                    }
                                });

                                //如果超过50个item 不再加载，实际开发中可以通过判定有没有更多的数据的方式来决定要不要加载更多
                                //  adapter.setCanLoadMore(adapter.getItemCount() <= 50);
                            }
                        });
                    }
                }.start();
            }
        });
        // 点击监听
        adapter.setOnItemClick(new NewsViewAdapter.onMyItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {


                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(200); // 给个两毫秒，让后面的sputils把数据put进去
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String id = SpUtils.getString(context,ID,"");
                        Log.i("xyy","bundle 放入的id"+id);
                        Bundle bundle = new Bundle();
                        bundle.putString(KEYID,id);
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }.start();

            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }









}
