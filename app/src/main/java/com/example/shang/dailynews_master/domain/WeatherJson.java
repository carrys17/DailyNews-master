package com.example.shang.dailynews_master.domain;

import java.util.ArrayList;

/**
 * Created by shang on 2017/7/27.
 */

//http://apicloud.mob.com/v1/weather/query?key=1fb46ee5ca1f4&city=揭阳
public class WeatherJson {

    public ArrayList<WeatherResultJson> result;

    public class WeatherResultJson {
        public String city;
        public String humidity;
        public String wind;
        public String temperature;
        public String weather;
        public String airCondition;
        public ArrayList<WeatherFeatureJson> future;
    }

    public class WeatherFeatureJson {
        public String date;    //日期
        public String week;   // 星期几
        public String dayTime; // 白天天气
        public String temperature;
    }
}
