package com.finance.hytone.model;

public class PermModel {
    private String title;
    private String subtitle;
    private int img;

    public PermModel(String title, String subtitle, int img) {
        this.title = title;
        this.subtitle = subtitle;
        this.img = img;
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

    public void setTitle(String title) {
        this.title = title;
    }

}
