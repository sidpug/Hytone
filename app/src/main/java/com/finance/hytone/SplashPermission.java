package com.finance.hytone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SplashPermission extends AppCompatActivity {

    private ScrollView Scroll;
    ArrayList<String> title,subtitle;
    ArrayList<Integer> img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_permission);
        title = new ArrayList<String>();
        subtitle = new ArrayList<String>();
        img = new ArrayList<Integer>();
        add(title,subtitle,img);
        if (Permission.checkAllPermissions(SplashPermission.this) && Helper.isAccepted(SplashPermission.this)) {
            startActivity(new Intent(SplashPermission.this, MainActivity.class));
            finish();
        }
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashPermission.this, SplashPermission2.class));
            }
        });
    }
    void add(ArrayList<String> title,ArrayList<String> subtitle, ArrayList<Integer> img)
    {
        //img.add(R.drawable.);
        title.add("Sms");
        subtitle.add("Read and receive SMS to understand\n" +
                "your income and spending patters for\n" +
                "determining your credit limit");
        title.add("Location");
        subtitle.add("Collect and monitor your device location\n" +
                "to provide serviceability of your loan\n" +
                "application and customized offers");
        title.add("Contacts");
        subtitle.add("Collect contacts information to detect\n" +
                "references and auto-fill the data during\n" +
                "your loan application");
        title.add("Phone");
        subtitle.add("Collect hardware information, OS and \n" +
                "version, unique device identifier, wifi \n" +
                "and mobile network information, profile\n" +
                "information to prevent fraud by uniquely\n" +
                "identifying the devices and restricting \n" +
                "unauthorized devices to act on your behalf");
        title.add("Storage");
        subtitle.add("Allow you to upload documents and\n" +
                "pictures for loan application");
        title.add("Camera");
        subtitle.add("Allow you to capture images of\n" +
                "documents and pcitures for loan\n" +
                "application");
        title.add("Installed Applications");
        subtitle.add("Collect list of apps installed in your device\n" +
                "for credit profile enrichment");
    }
}