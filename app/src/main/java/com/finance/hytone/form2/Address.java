package com.finance.hytone.form2;

    import  androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.core.view.GravityCompat;
    import okhttp3.MediaType;
    import okhttp3.MultipartBody;
    import okhttp3.RequestBody;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Environment;
    import android.view.View;
    import android.widget.Toast;

    import com.finance.hytone.Form2;
    import com.finance.hytone.Helper;
    import com.finance.hytone.R;
    import com.finance.hytone.SplashPermission;
    import com.finance.hytone.Welcome;
    import com.finance.hytone.constants.Constants;
    import com.finance.hytone.constants.HttpResponseUtils;
    import com.finance.hytone.retrofit.GetDataService;
    import com.finance.hytone.retrofit.RetrofitClientInstance;
    import com.finance.hytone.utils.ApiUtils;
    import com.google.android.material.textfield.TextInputEditText;

    import java.io.BufferedWriter;
    import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.util.Objects;

public class Address extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Helper.showdialogStayOrGoBack(Address.this,true,"","You will lose data of this screen if you go back!");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_24px);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawer.openDrawer(GravityCompat.START);
                Helper.showdialogStayOrGoBack(Address.this,true,"","You will lose data of this screen if you go back!");
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address()){
                    try {
                        String fullPath = Helper.makeFile(Address.this, Constants.FORM_NAME_BASIC, ss);
                        uploadAddressDetails(fullPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


        ProgressDialog pd;
        private void uploadAddressDetails(String fullPath) {
            pd = new ProgressDialog(Address.this);
            pd.setCancelable(false);
            pd.setMessage("Please wait...");
            pd.show();
            Helper.log("params", "insidemeth");
            ApiUtils.uploadDetails(Address.this,Constants.FORM_NAME_BASIC, fullPath, pd);

    }


    String ss;
    public boolean address() {
        ss="";
        String address = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.address)).getText()).toString().trim();
        String landmark = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.landmark)).getText()).toString().trim();
        String ps = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.ps)).getText()).toString().trim();
        String po = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.po)).getText()).toString().trim();
        String pincode = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.pincode)).getText()).toString().trim();
        String ownership_type = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.ot)).getText()).toString().trim();
        String error = null;
        /*if (fName.length()==0)
            error = "Please enter your First name";*/
        //else if(eLname..)
        if(address.length() == 0)
            error = "Please enter valid Address!";
        else if (landmark.length() == 0)
            error = "Please enter landmark";
        else if (ps.length() == 0)
            error = "Please enter Police station";
        else if (po.length() == 0)
            error = "Please enter post office!";
        else if (pincode.length() == 0)
            error = "Please enter pincode";
        else if (ownership_type.length() == 0)
            error = "Please enter Ownership type";


        if (error != null) {
            Helper.showdialog(Address.this, true, "", error);
            return false;
        }

        ss += "\n";
        ss += "\nPERSONAL ADDRESS";
        ss += "\n________________________";
        ss += "\nAddress: "+address;
        ss += "\nLandmark: "+landmark;
        ss += "\nPolice Stn: "+ps;
        ss += "\nPost Office: "+po;
        ss += "\nPincode: "+pincode;
        ss += "\nOwnership type: "+ownership_type;

        return true;
    }
//    public void next()
//    {
//    }
}




