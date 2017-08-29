package com.example.shang.dailynews_master.utils;

import android.content.Context;
import android.util.Log;

import com.example.shang.dailynews_master.config.ContentValues;
import com.example.shang.dailynews_master.domain.ZhihuJson;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.example.shang.dailynews_master.config.ContentValues.DATE;

/**
 * Created by shang on 2017/8/12.
 */

public class NewsHelper {

    List<String> newsId = new ArrayList<>();
    List<String> newsTitle = new ArrayList<>();
    List<ArrayList<String>> newsImage = new ArrayList<>();
    List<ZhihuJson.Stories>  moreStories = new ArrayList<>();



    public void getMoreNewsJson(final Context context){
        String date = SpUtils.getString(context,DATE,"");
        Log.i("date","date = "+ date );
        String  s = ContentValues.BEFORE + date;
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

    private void parseNews(String result,Context context) {
        Gson gson = new Gson();
        ZhihuJson zhihuJson = gson.fromJson(result, ZhihuJson.class);

        // 新闻列表
        moreStories = zhihuJson.stories;
        for (int i = 0; i < moreStories.size(); i++) {
            newsId.add(moreStories.get(i).id);
            newsTitle.add(moreStories.get(i).title);
            newsImage.add(moreStories.get(i).images);
            Log.i("xyz", "titlemain =" + moreStories.get(i).getTitle());
            Log.i("xyz", "imagemain = " + moreStories.get(i).getImages().get(0));
        }
    }

    public void setNewsTitle(List<String> newsTitle) {
        this.newsTitle = newsTitle;
    }

    public List<ArrayList<String>> getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(List<ArrayList<String>> newsImage) {
        this.newsImage = newsImage;
    }




    public List<String> getNewsId() {
        return newsId;
    }

    public void setNewsId(List<String> newsId) {
        this.newsId = newsId;
    }

    public List<String> getNewsTitle() {
        return newsTitle;
    }
    public List<ZhihuJson.Stories> getMoreStories() {
        return moreStories;
    }

    public void setMoreStories(List<ZhihuJson.Stories> moreStories) {
        this.moreStories = moreStories;
    }

}
