package com.example.shang.dailynews_master.config;

/**
 * Created by shang on 2017/7/26.
 */

public class ContentValues {
    public static final String CITY ="city";
    public static final String ID ="id";


    public static final String DATE = "date";

    // key 为自己申请的项目的key值
    public static final String WEATHER = "http://apicloud.mob.com/v1/weather/query?key=1fb46ee5ca1f4&city=";
    // 基本的url加上id就是该信息的详细
    public static final String ZHIHU ="http://news-at.zhihu.com/api/4/news/";
    // 最新消息，包括头条新闻和列表新闻
    public static final String LASTER = ZHIHU +"latest";
    // 后面加个日期，格式为20170101就是该天的早些新闻
    public static final String BEFORE = ZHIHU + "before/";

}
