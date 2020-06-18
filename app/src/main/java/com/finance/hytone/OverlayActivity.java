package com.finance.hytone;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class OverlayActivity extends Activity {

    private ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new ImageView(this);
        setContentView(view);
    }

    @Override
    public void onBackPressed() {

    }
}