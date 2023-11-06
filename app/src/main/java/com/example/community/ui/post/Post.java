package com.example.community.ui.post;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Post implements Serializable {

    private Bitmap cover;
    private String title;
    private String username;
    private String content;
    private long time;

    public Post(Bitmap cover, String title, String username, String content, long time) {
        this.cover = cover;
        this.title = title;
        this.username = username;
        this.content = content;
        this.time = time;
    }

    public Bitmap getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }
}
