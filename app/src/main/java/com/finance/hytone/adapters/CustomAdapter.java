package com.finance.hytone.adapters;

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


public class CustomAdapter extends ArrayAdapter<PermModel> {

    Context c;
    /*String[] imgurls;
    int layout;*/
    LayoutInflater inflater;

    public CustomAdapter(Activity context, ArrayList<PermModel> permModel) {
        super(context, R.layout.list_item_simple, permModel);
        this.c = context;
        /*this.layout = layout;
        this.imgurls = imgurls;*/
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PermModel permModel = getItem(position);
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
            Log.e("in position-->" + position, ":" + permModel.title.get(position));
            holder.img.setImageDrawable(ResourcesCompat.getDrawable(c.getResources(), permModel.img.get(position), null));
            holder.titleTxt.setText(permModel.title.get(position));
            holder.subtitleTxt.setText(permModel.subtitle.get(position));

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






