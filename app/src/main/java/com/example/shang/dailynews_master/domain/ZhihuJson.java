package com.example.shang.dailynews_master.domain;

import java.util.ArrayList;

/**
 * Created by shang on 2017/7/29.
 */

public class ZhihuJson {
    public String date;
    public ArrayList<Stories> stories;
    public ArrayList<TopStories> top_stories;

    public class TopStories {
        public String image;
        public String title;
        public String id;
    }

    // 主要top新闻跟新闻列表的图片json，一个是image，一个是images，
    // 也就是字符串组，所以要ArrayList,得到时，get（0）获取到第一张图片（其实也就一张。。。）
    public class Stories {
        public String id;
        public String title;
        public ArrayList<String> images;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

    }



}
