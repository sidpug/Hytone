package com.finance.hytone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.finance.hytone.adapters.CustomAdapter;
import com.finance.hytone.model.PermModel;

import java.util.ArrayList;

public class SplashPermission extends AppCompatActivity {

    ArrayList<String> title,subtitle;
    ArrayList<Integer> img;
    ArrayList<PermModel> perm;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_permission);
        ListView listView = findViewById(R.id.list_item);
        add(title,subtitle,img);
        PermModel t =new PermModel(title,subtitle,img);
        perm.add(t);
        adapter = new CustomAdapter(this,perm);
        listView.setAdapter(adapter);
        /*title = new ArrayList<>();
        subtitle = new ArrayList<>();
        img = new ArrayList<>();*/
        //add(title,subtitle,img);

        adapter.addAll();
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
        //perm = new ArrayList<>();
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
                "documents and pictures for loan\n" +
                "application");
        title.add("Installed Applications");
        subtitle.add("Collect list of apps installed in your device\n" +
                "for credit profile enrichment");
    }
}