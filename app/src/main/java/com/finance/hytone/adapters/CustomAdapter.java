package com.finance.hytone.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finance.hytone.R;



public class CustomAdapter extends ArrayAdapter<String> {


    Context c;
    String[] title, subTitle;
    int[] imgs;
    String[] imgurls;
    int layout;
    LayoutInflater inflater;

    public CustomAdapter(Activity context, String type,  String[] title, String[] subTitle, int[] imgs, String[] imgurls) {
        super(context, R.layout.list_item_simple, title);
        this.c = context;
        //this.layout = layout;
        this.title = title;
        this.subTitle = subTitle;
        this.imgs = imgs;
        this.imgurls = imgurls;
    }


    public static class ViewHolder {
        TextView titleTxt, subtitleTxt;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //if (convertView == null) {
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_simple, null);
        //}
        try {
            final ViewHolder holder = new ViewHolder();
            holder.titleTxt = convertView.findViewById(R.id.txt);
            holder.subtitleTxt = convertView.findViewById(R.id.txtSubtitle);
            holder.img = convertView.findViewById(R.id.img);
            Log.e("in position-->" + position, ":" + title[position]);
            holder.img.setImageDrawable(c.getResources().getDrawable(imgs[position]));

            holder.titleTxt.setText(title[position]);
            holder.subtitleTxt.setText(subTitle[position]);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


}






