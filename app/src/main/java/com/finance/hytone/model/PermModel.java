package com.finance.hytone.model;

import java.util.ArrayList;

public class PermModel {
    private String title;
    private String subtitle;
    private int img;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public PermModel(String title, String subtitle, int img) {
        this.title = title;
        this.subtitle = subtitle;
        this.img = img;
    }

}
