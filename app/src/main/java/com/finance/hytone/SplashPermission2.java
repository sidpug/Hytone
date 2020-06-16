package com.finance.hytone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashPermission2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_permission2);

        final Button accept = findViewById(R.id.allow);
        CheckBox checkBox = findViewById(R.id.checkBox);
        accept.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    accept.setEnabled(true);
                } else
                    accept.setEnabled(false);

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.putAccepted(SplashPermission2.this, true);
                if (Permission.checkAllPermissions(SplashPermission2.this)) {

                    startActivity(new Intent(SplashPermission2.this, MainActivity.class));
                } else
                    Permission.permission(SplashPermission2.this);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (Permission.checkAllPermissions(SplashPermission2.this)) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                Toast.makeText(SplashPermission2.this, "All permission GRANTED", Toast.LENGTH_SHORT).show();
                Log.e("log_case2", "GRANTED!");

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(SplashPermission2.this, "One or more Permissions denied", Toast.LENGTH_SHORT).show();
                Log.e("log_case3", "NOT GRANTED!");

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}