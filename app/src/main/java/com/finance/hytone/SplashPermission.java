package com.finance.hytone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.finance.hytone.adapters.CustomAdapter;
import com.finance.hytone.model.PermModel;

import java.util.ArrayList;

public class SplashPermission extends AppCompatActivity {


    ArrayList<PermModel> perm;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_permission);
        ListView listView = findViewById(R.id.list_item);

        ArrayList<String> title = new ArrayList<>(), subtitle = new ArrayList<>();
        ArrayList<Integer> img = new ArrayList<>();
        perm = new ArrayList<>();
        add(title, subtitle, img);

        for (int i = 0; i < title.size(); i++)
            perm.add(new PermModel(title.get(i), subtitle.get(i), img.get(i)));

        adapter = new CustomAdapter(this, perm, title);
        listView.setAdapter(adapter);

        if (Permission.checkAllPermissions(SplashPermission.this) && Helper.isAccepted(SplashPermission.this)) {
            startActivity(new Intent(SplashPermission.this, MainActivity.class));
            finish();
        }

        findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.putAccepted(SplashPermission.this, true);
                if (Permission.checkAllPermissions(SplashPermission.this)) {
                    startActivity(new Intent(SplashPermission.this, MainActivity.class));
                } else
                    Permission.permission(SplashPermission.this);
            }
        });
    }

    void add(ArrayList<String> title, ArrayList<String> subtitle, ArrayList<Integer> img) {
        img.add(R.drawable.sms);
        title.add("Sms");
        subtitle.add("Read and receive SMS to understand\n" +
                "your income and spending patters for\n" +
                "determining your credit limit");
        img.add(R.drawable.location);
        title.add("Location");
        subtitle.add("Collect and monitor your device location\n" +
                "to provide serviceability of your loan\n" +
                "application and customized offers");
        img.add(R.drawable.contact);
        title.add("Contacts");
        subtitle.add("Collect contacts information to detect\n" +
                "references and auto-fill the data during\n" +
                "your loan application");
        img.add(R.drawable.phone);
        title.add("Phone");
        subtitle.add("Collect hardware information, OS and \n" +
                "version, unique device identifier, wifi \n" +
                "and mobile network information, profile\n" +
                "information to prevent fraud by uniquely\n" +
                "identifying the devices and restricting \n" +
                "unauthorized devices to act on your behalf");
        img.add(R.drawable.storage);
        title.add("Storage");
        subtitle.add("Allow you to upload documents and\n" +
                "pictures for loan application");
        img.add(R.drawable.camera);
        title.add("Camera");
        subtitle.add("Allow you to capture images of\n" +
                "documents and pictures for loan\n" +
                "application");
        img.add(R.drawable.installed_apps);
        title.add("Installed Applications");
        subtitle.add("Collect list of apps installed in your device\n" +
                "for credit profile enrichment");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (Permission.checkAllPermissions(SplashPermission.this)) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                Toast.makeText(SplashPermission.this, "All permission GRANTED", Toast.LENGTH_SHORT).show();
                Log.e("log_case2", "GRANTED!");

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(SplashPermission.this, "One or more Permissions denied", Toast.LENGTH_SHORT).show();
                Log.e("log_case3", "NOT GRANTED!");
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}