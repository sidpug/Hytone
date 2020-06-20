package com.finance.hytone.model;

import java.util.ArrayList;

public class PermModel {
    public ArrayList<String> title;
    public ArrayList<String> subtitle;
    public ArrayList<Integer> img;

    public PermModel(ArrayList<String> title, ArrayList<String> subtitle, ArrayList<Integer> img) {
        this.title = title;
        this.subtitle = subtitle;
        this.img = img;
    }
}
