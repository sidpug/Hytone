package com.finance.hytone.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.finance.hytone.R;
import com.finance.hytone.model.PermModel;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<String> {

    Context c;
    /*String[] imgurls;
    int layout;*/
    LayoutInflater inflater;
    private ArrayList<PermModel> permModel;

    public CustomAdapter(Activity context, ArrayList<PermModel> permModel, ArrayList<String> title) {
        super(context, R.layout.list_item_simple, R.id.txt, title);
        this.c = context;
        this.permModel = permModel;
        /*this.layout = layout;
        this.imgurls = imgurls;*/
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //PermModel permModel = getItem(position);
        //if (convertView == null) {
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_simple, null);
        //}
        try {
            final ViewHolder holder = new ViewHolder();
            holder.titleTxt = convertView.findViewById(R.id.txt);
            holder.subtitleTxt = convertView.findViewById(R.id.txtSubtitle);
            holder.img = convertView.findViewById(R.id.img);
            assert permModel != null;
            Log.e("in position-->" + position, ":" + permModel.get(position).getTitle());
            holder.img.setImageDrawable(ResourcesCompat.getDrawable(c.getResources(), permModel.get(position).getImg(), null));
            holder.titleTxt.setText(permModel.get(position).getTitle());
            holder.subtitleTxt.setText(permModel.get(position).getSubtitle());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView titleTxt, subtitleTxt;
        ImageView img;
    }


}






