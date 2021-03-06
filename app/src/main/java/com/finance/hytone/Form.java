package com.finance.hytone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.finance.hytone.constants.Constants;
import com.finance.hytone.constants.HttpResponseUtils;
import com.finance.hytone.retrofit.GetDataService;
import com.finance.hytone.retrofit.RetrofitClientInstance;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Form extends AppCompatActivity {

    ProgressDialog pd;
    private String login_type;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        if (getIntent().hasExtra("login_type")) {
            TextInputEditText phone, fname, lname, pincode;
            phone = findViewById(R.id.editTextPhone);
            fname = findViewById(R.id.editTextName);
            lname = findViewById(R.id.editTextName_last);
            pincode = findViewById(R.id.editText_pincode);
            phone.setText(Helper.getPhone(this));
            fname.setText(Helper.getFname(this));
            lname.setText(Helper.getLname(this));
            pincode.setText(Helper.getPincode(this));

            login_type = getIntent().getStringExtra("login_type");
            assert login_type != null;
            if (login_type.equals(Constants.LOGINTYPE_GOOGLE)) {
                String name = getIntent().getStringExtra("name");
                email = getIntent().getStringExtra("email");
                //fname.setText(name);
            }
        }
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_type == null) {
                } else if (login_type.equals(Constants.LOGINTYPE_GOOGLE)) {
                    TextInputEditText phone, fname, lname, pincode;
                    phone = findViewById(R.id.editTextPhone);
                    fname = findViewById(R.id.editTextName);
                    lname = findViewById(R.id.editTextName_last);
                    pincode = findViewById(R.id.editText_pincode);
                    if (Objects.requireNonNull(phone.getText()).toString().length() != 10) {
                        phone.setError("Phone number should be 10 digits");
                    } else if (Objects.requireNonNull(fname.getText()).toString().trim().length() == 0) {
                        fname.setError("Please enter first name");
                    } else if (Objects.requireNonNull(lname.getText()).toString().trim().length() == 0) {
                        lname.setError("Please enter last name");
                    } else if (Objects.requireNonNull(pincode.getText()).toString().trim().length() != 6) {
                        pincode.setError("Pin code should be 6 digits");
                    } else
                        uploadDetails(phone.getText().toString(),
                                fname.getText().toString().trim(),
                                lname.getText().toString().trim(),
                                pincode.getText().toString()
                        );
                }
                startActivity(new Intent(Form.this, Welcome.class));
            }
        });
    }

    private void uploadDetails(String phone, String fname, String lname, String pincode) {
        try {
            Helper.putFname(this, fname);
            Helper.putLname(this, lname);
            Helper.putPhone(this, phone);
            Helper.putPincode(this, pincode);
            Helper.putEmail(this, email);

            final String fullPath = Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_ALARMS)).getAbsolutePath() + File.separator + "personalDetails" + fname + phone + System.currentTimeMillis() + ".txt";
            File ff = new File(fullPath);
            if (ff.exists())
                ff.delete();

            BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
            PrintWriter pw = new PrintWriter(fw);

            pw.println("Name: " + fname + "  " + lname);
            pw.println("Phone: " + phone);
            pw.println("Pin: " + pincode);
            pw.println("Location: " + Helper.getLastLocation(getApplicationContext()));
            //pw.println("Email: "+email);
            pw.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //pd.dismiss();
                    uploadContent(fullPath);
                    //doContactWork();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Helper.showDialogUIThread(Form.this, false, "Error creating file", "" + e);
        }
    }

    private void uploadContent(String fullPath) {
        pd = new ProgressDialog(Form.this);
        pd.setCancelable(false);
        pd.setMessage("Getting details");
        pd.show();
        Helper.log("params", "insidemeth");
        GetDataService service = RetrofitClientInstance.getRetrofitInstanceForFile().create(GetDataService.class);
        Call<String> call;
        try {

            /*File ff = new File(getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath());
            if (!ff.exists())
                ff.mkdir();
            String mFileName = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath()+File.separator+ "logs01.txt";
            Log.e("mfilename",mFileName);*/
            //String stringValue = "stringValue";
            File file = new File(fullPath);

            RequestBody requestFile =
                    RequestBody.create(
                            // MediaType.parse("image/jpg"),
                            MediaType.parse("text/plain"),
                            file
                    );

            //String items = "[1,2,4]";
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestFile);

            //RequestBody items = RequestBody.create(MediaType.parse("application/json"), items);
            //RequestBody stringValue = RequestBody.create(MediaType.parse("text/plain"), stringValue);

            call = service.upload(body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    pd.dismiss();
                    int rescode = response.code();
                    if (rescode != 200) {
                        HttpResponseUtils.showBasicResponseLogsString(response);
                        HttpResponseUtils.processHeaderCode(Form.this, rescode);
                        return;
                    }
                    Helper.log("passsed7", "here");
                    HttpResponseUtils.showBasicResponseLogsString(response);
                    try {
                        String resBody = response.body();
                        //Toast.makeText(Form.this, ",,,,"+resBody, Toast.LENGTH_SHORT).show();
                        //setResult(RESULT_OK);
                        Helper.putFormWorkDone(Form.this,"1");
                        Intent ii = new Intent(Form.this,Welcome.class);
                        startActivity(ii);
                        finish();
                        //JSONObject jo = new JSONObject(resBody);
                        //int response_code = Integer.parseInt(jo.getString("response_code"));
                        // if (response_code == 200) {

                    } catch (Exception e) {
                       // Helper.showDialog(Form.this, false, "Something went wrong. " + e.getMessage(), "" + e.toString());
                        e.printStackTrace();
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    pd.dismiss();
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    //HttpResponseUtils.handleFailure(Form.this, call, t);
                    //Helper.log("failcase",t.toString());
                    t.printStackTrace();
                    Helper.log("agog111", "" + call.request().toString());
                    Helper.log("agog1111", "1" + call.request().headers());
                    Helper.showDialog(Form.this, false, "Network failure!", "" + t.getMessage());
                }
            });

        } catch (Exception e) {
            Helper.showDialog(Form.this, false, "Error occurred", "" + e);
            e.printStackTrace();
        }
    }
}