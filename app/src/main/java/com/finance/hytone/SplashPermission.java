package com.finance.hytone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_permission);
        if (Permission.checkAllPermissions(SplashPermission.this) && Helper.isAccepted(SplashPermission.this)) {
            startActivity(new Intent(SplashPermission.this, MainActivity.class));
            finish();
        }
        Button nxt = findViewById(R.id.next);
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashPermission.this, SplashPermission2.class));
            }
        });
    }
}