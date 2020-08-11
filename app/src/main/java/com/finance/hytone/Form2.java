package com.finance.hytone;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.finance.hytone.constants.Constants;
import com.finance.hytone.constants.HttpResponseUtils;
import com.finance.hytone.filter.PanFilter;
import com.finance.hytone.retrofit.GetDataService;
import com.finance.hytone.retrofit.RetrofitClientInstance;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class Form2 extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 107;
    Bitmap bit;
    Uri imageUri;
    String currentPhotoPath;
    int previewImgResId = -1;
    private ImageView target;
    String ss = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);
        EditText pan_number = findViewById(R.id.pan_number);
        pan_number.setFilters(new InputFilter[]{new PanFilter()});
        //aadhar();
        findViewById(R.id.submit_form2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ss = "";
                if (handleSection1() && address() && office_address() && assets() && checkOthers()){// && aadhar() && pan() && voter_id() && dl() && passport() && bank()) {
                    try {
                        submitForm();
                    } catch (Exception e) {
                        Helper.showDialog(Form2.this,false, "","Failed!"+e);
                        e.printStackTrace();
                    }
                }
            }
        });
        aadhar();
        pan();
        voter_id();
        dl();
        passport();
        bank();
    }
    private boolean checkOthers(){
        String error = null;
        String pan_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.pan_number)).getText()).toString().trim();
        if (pan_no.length() == 0 || pan_no.length() < 10)
        {
            ((TextInputEditText) findViewById(R.id.pan_number)).setError("Enter Valid Pan Number");
            error = "Enter Valid Pan Number";
        }

        String aadhar_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.aadhar_number)).getText()).toString().trim();
        if (aadhar_no.length() == 0 || aadhar_no.length() < 12) {
            ((TextInputEditText) findViewById(R.id.aadhar_number)).setError("Enter Valid Aadhar Number");
            error = "Enter Valid Aadhar Number";
            requestFocus(findViewById(R.id.aadhar_number));
        }
        String voter_id_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.voter_id_number)).getText()).toString().trim();
        if (voter_id_no.length() == 0)
        {
            ((TextInputEditText) findViewById(R.id.voter_id_number)).setError("Enter Valid Voter Id Number");
            error = "Enter Valid Voter Id Number";
        }

        String dl_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.dl_no)).getText()).toString().trim();
        if (dl_no.length() == 0)
        {
            ((TextInputEditText) findViewById(R.id.dl_no)).setError("Enter Valid Driving Licence Number");
            error = "Enter Valid Driving Licence Number";
        }

        String bankname = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.bank_name)).getText()).toString().trim();
        String acc_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.acc_no)).getText()).toString().trim();
        String ifsc = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.ifsc_code)).getText()).toString().trim();
        if (bankname.length() == 0)
        {
            ((TextInputEditText) findViewById(R.id.bank_name)).setError("Enter Valid Bank Name");
            error = "Enter Valid Bank Name";
        }
        else if (acc_no.length() == 0)
        {
            ((TextInputEditText) findViewById(R.id.acc_no)).setError("Enter Valid Account Number");
            error = "Enter Valid Account Number";
        }
        else if (ifsc.length() == 0)
        {
            ((TextInputEditText) findViewById(R.id.ifsc_code)).setError("Enter Valid Ifsc Code");
            error = "Enter Valid Ifsc Code";
        }


        if (error != null) {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }

        ss += "\n";
        ss += "\nPan no: "+pan_no;
        ss += "\n";
        ss += "\nAadhar no: "+aadhar_no;
        ss += "\n";
        ss += "\nVoter ID: "+voter_id_no;
        ss += "\n";
        ss += "\nDriving License No: "+dl_no;
        ss += "\n";
        ss += "\nBANK DETAILS: ";
        ss += "\n___________________";
        ss += "\nName: "+bankname;
        ss += "\nAccNo: "+acc_no;
        ss += "\nIFSC: "+ifsc;

        return true;
    }
    private void submitForm() throws Exception {
        Toast.makeText(this, "Submitting!", Toast.LENGTH_LONG).show();
        final String fullPath = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + "form2Detail" + System.currentTimeMillis() + ".txt";
        File ff = new File(fullPath);
        if (ff.exists())
            ff.delete();

        BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
        PrintWriter pw = new PrintWriter(fw);
        pw.println(ss);
        pw.close();

        uploadContent(fullPath);

    }
ProgressDialog pd;
    private void uploadContent(String fullPath) {
        pd = new ProgressDialog(Form2.this);
        pd.setCancelable(false);
        pd.setMessage("Posting details");
        pd.show();
        Helper.log("params", "insidemeth");
        GetDataService service = RetrofitClientInstance.getRetrofitInstanceForFile().create(GetDataService.class);
        Call<String> call = null;
        try {

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
                        HttpResponseUtils.processHeaderCode(Form2.this, rescode);
                        return;
                    }
                    Helper.log("passsed7", "here");
                    HttpResponseUtils.showBasicResponseLogsString(response);
                    try {
                        String resBody = response.body();
                        Toast.makeText(Form2.this, "Done!", Toast.LENGTH_LONG).show();
                        Intent ii = new Intent(Form2.this,Welcome.class);
                        startActivity(ii);
                        finish();
                        //JSONObject jo = new JSONObject(resBody);
                        //int response_code = Integer.parseInt(jo.getString("response_code"));

                        // if (response_code == 200) {

                    } catch (Exception e) {
                        Helper.showDialog(Form2.this, false, "Something went wrong. " + e.getMessage(), "" + e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    //HttpResponseUtils.handleFailure(Form2.this, call, t);
                    //Helper.log("failcase",t.toString());
                    pd.dismiss();
                    t.printStackTrace();
                    Helper.log("agog111", "" + call.request().toString());
                    Helper.log("agog1111", "1" + call.request().headers());
                    Helper.showDialog(Form2.this, false, "Network failure!", "" + t.getMessage());
                }
            });

        } catch (Exception e) {
            Helper.showDialog(Form2.this, false, "Error occurred", "" + e);
            e.printStackTrace();
        }
    }
    public boolean handleSection1() {
        String mob = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.mob)).getText()).toString().trim();
        String fName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.fName)).getText()).toString().trim();
        String lName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.lName)).getText()).toString().trim();
        String nName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.nickName)).getText()).toString().trim();
        String email = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.email)).getText()).toString().trim();
        String dep = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.dependent)).getText()).toString().trim();
        String error = null;
        if (fName.length() == 0)
            error = "Please enter your First name";
        //else if(eLname..)
        else if (lName.length() == 0)
            error = "Please enter your last name";
        else if(nName.length() == 0)
            error = "Please enter nickname";
        else if(!email.contains("@"))
            error = "Please enter valid email";
        else if(dep.length() == 0)
            error = "Please enter number of dependents";
        else if(mob.length() != 10)
            error = "Please enter valid phone number";

        if (error != null) {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        ss += "\nBASIC DETAILS:";
        ss += "\n________________________";
        ss += "\nName: "+fName+" "+lName+" ("+nName+")";
        ss += "\nName: "+fName+" "+lName;
        ss += "\nEmail: "+email;
        ss += "\nDependents: "+dep;
        ss += "\nMobile: "+mob;

        return true;
    }

    public boolean address() {
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
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }

        ss += "\n";
        ss += "\nPERSONAL ADDRESS";
        ss += "\n________________________";
        ss += "\nAddress: "+address;
        ss += "\nLandmark: "+landmark;
        ss += "\nPolice Stn: "+ps;
        ss += "\nPost Offic: "+po;
        ss += "\nPincode: "+pincode;
        ss += "\nOwnership type: "+ownership_type;

        return true;
    }

    public boolean office_address() {
        String profession = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.profession)).getText()).toString().trim();
        String mi = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.mi)).getText()).toString().trim();
        String emi = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.emi)).getText()).toString().trim();
        String cName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.cName)).getText()).toString().trim();
        String cAddress = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.cAddress)).getText()).toString().trim();
        String cLandmark = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.cLandmark)).getText()).toString().trim();
        String cPs = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.cPs)).getText()).toString().trim();
        String cPo = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.cPo)).getText()).toString().trim();
        String cPincode = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.cPincode)).getText()).toString().trim();

        String error = null;
        if(profession.length() == 0)
            error = "Please enter valid profession!";
        else if (cLandmark.length() == 0)
            error = "Please enter office landmark";
        else if (cPs.length() == 0)
            error = "Please enter office Police station";
        else if (cPo.length() == 0)
            error = "Please enter office post office!";
        else if (cPincode.length() == 0)
            error = "Please enter office pincode";
        else if (cAddress.length() == 0)
            error = "Please enter valid office address";
        else if (cName.length() == 0)
            error = "Please enter company name";

        else if (mi.length() == 0)
            error = "Please enter monthly income";
//
//        else if (emi.length() == 0)
//            error = "Please enter company name";



        if (error != null) {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        ss += "\n";
        ss += "\nOFFICE DETAILS";
        ss += "\n________________________";
        ss += "\nProfession: "+profession;
        ss += "\nCompany Name: "+cName;
        ss += "\nAddress: "+cAddress;
        ss += "\nLandmark: "+cLandmark;
        ss += "\nPolice Stn: "+cPs;
        ss += "\nPost Office: "+cPo;
        ss += "\nPincode: "+cPincode;
        ss += "\nMonthly Income: "+mi;
        ss += "\nEMI: "+(emi.equals("")?"none":emi);

        return true;
    }

    public boolean aadhar() {
        String aadhar_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.aadhar_number)).getText()).toString().trim();
        String error = null;
//        if (aadhar_no.length() == 0 || aadhar_no.length() < 12) {
//            ((TextInputEditText) findViewById(R.id.aadhar_number)).setError("Enter Valid Aadhar Number");
//            requestFocus(findViewById(R.id.aadhar_number));
//        }
        final ImageView front = findViewById(R.id.front_aadhar);
        final ImageView back = findViewById(R.id.back_aadhar);
        findViewById(R.id.aadhar_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = front;
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.aadhar_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = back;
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

        if (error != null) {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }

        return true;
    }

    public boolean pan() {
        String pan_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.pan_number)).getText()).toString().trim();
        String error = null;
//        if (pan_no.length() == 0 || pan_no.length() < 10)
//            ((TextInputEditText) findViewById(R.id.pan_number)).setError("Enter Valid Pan Number");

        final ImageView front = findViewById(R.id.front_pan);
        final ImageView back = findViewById(R.id.back_pan);
        findViewById(R.id.pan_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = front;
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.pan_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = back;
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

//        if (error != null) {
//            Helper.showdialog(Form2.this, true, "", error);
//            return false;
//        }
        return true;
    }

    public boolean voter_id() {
        String voter_id_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.voter_id_number)).getText()).toString().trim();
        String error = null;
//        if (voter_id_no.length() == 0)
//            ((TextInputEditText) findViewById(R.id.voter_id_number)).setError("Enter Valid Voter Id Number");

        final ImageView front = findViewById(R.id.front_voter_id);
        final ImageView back = findViewById(R.id.back_voter_id);
        findViewById(R.id.voter_id_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = front;
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.voter_id_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = back;
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

//        if (error != null) {
//            Helper.showdialog(Form2.this, true, "", error);
//            return false;
//        }
        return true;
    }

    public boolean dl() {
        String dl_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.dl_no)).getText()).toString().trim();
        String error = null;
//        if (dl_no.length() == 0)
//            ((TextInputEditText) findViewById(R.id.dl_no)).setError("Enter Valid Driving Licence Number");

        final ImageView front = findViewById(R.id.front_dl_no);
        final ImageView back = findViewById(R.id.back_dl_no);
        findViewById(R.id.dl_no_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = front;
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.dl_no_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = back;
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

//        if (error != null) {
//            Helper.showdialog(Form2.this, true, "", error);
//            return false;
//        }
        return true;
    }

    public boolean passport() {
        String passport = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.passport_number)).getText()).toString().trim();
        String error = null;
        if (passport.length() == 0)
            ((TextInputEditText) findViewById(R.id.passport_number)).setError("Enter Valid Passport Number");

        final ImageView front = findViewById(R.id.front_passport);
        final ImageView back = findViewById(R.id.back_passport);
        findViewById(R.id.passport_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = front;
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.passport_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = back;
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

//        if (error != null) {
//            Helper.showdialog(Form2.this, true, "", error);
//            return false;
//        }
        return true;
    }

    public boolean bank() {
//        String bankname = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.bank_name)).getText()).toString().trim();
//        String acc_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.acc_no)).getText()).toString().trim();
//        String ifsc = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.ifsc_code)).getText()).toString().trim();
//        String error = null;
//        if (bankname.length() == 0)
//            ((TextInputEditText) findViewById(R.id.bank_name)).setError("Enter Valid Bank Name");
//
//        if (acc_no.length() == 0)
//            ((TextInputEditText) findViewById(R.id.acc_no)).setError("Enter Valid Account Number");
//
//        if (ifsc.length() == 0)
//            ((TextInputEditText) findViewById(R.id.ifsc_code)).setError("Enter Valid Ifsc Code");

        final ImageView front = findViewById(R.id.statement);
        findViewById(R.id.statement_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target = front;
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });


//        if (error != null) {
//            Helper.showdialog(Form2.this, true, "", error);
//            return false;
//        }
        return true;
    }

    public boolean assets() {
        String asset1 = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.an1)).getText()).toString().trim();
        String a_value1 = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.av1)).getText()).toString().trim();
        String asset2 = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.an2)).getText()).toString().trim();
        String a_value2 = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.av2)).getText()).toString().trim();
        String asset3 = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.an3)).getText()).toString().trim();
        String a_value3 = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.av3)).getText()).toString().trim();

        String error = null;
        /*if (fName.length()==0)
            error = "Please enter your First name";*/
        //else if(eLname..)
        if (asset1.equals("") || a_value1.equals("") || asset2.equals("") || a_value2.equals(""))
            error = "Please enter all details of two assets at least";
        if (error != null) {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        ss += "\n";
        ss += "\nASSETS";
        ss += "\n________________________";
        ss += "\nAsset 1: "+asset1;
        ss += "\n"+a_value1;
        ss += "\n";
        ss += "\nAsset 2: "+asset2;
        ss += "\n"+a_value2;

        return true;
    }

    private void cameraIntent() {

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please provide write permissions from settings", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK)
            try {
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                onCaptureImageResult(data, thumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private void onCaptureImageResult(Intent data, Bitmap thumbnail) {
        currentPhotoPath = null;
        bit = thumbnail;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final int factor = Helper.getCompressFactor(bit, bytes);
        if (factor < 0) {
            target = null;
            bit = null;
            Helper.showdialog(Form2.this, false, "Image is large", "Image size is more than " + Constants.LIMIT_IMAGE_SIZE2 + "! Please attach image of lower size");
            return;

        }
        Helper.log("insideonCaptureImageResult", "" + factor);
        Helper.log("insideonCaptureImageResult2", "" + bit.getWidth() + "," + bit.getHeight());

        File destination = new File(Constants.PATH_HYTONE_FOLDER);
        if (!destination.exists())
            destination.mkdirs();

        destination = new File(Constants.PATH_HYTONE_FOLDER,
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            boolean bb = destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            if (bb)
                currentPhotoPath = destination.getAbsolutePath();
        } catch (Exception e) {
            target = null;
            e.printStackTrace();
            Toast.makeText(this, "Error creating file!", Toast.LENGTH_LONG).show();
            return;
        }

        ////ImageView imageView = findViewById(previewImgResId);
        // imageView.setVisibility(View.VISIBLE);
        target.setImageBitmap(bit);
        Log.e("onacti1", "nothing");
        Log.e("onacti", "11");// + descimage);
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /*public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }*/
    /*private Pattern pattern = Pattern.compile("-?\\\\d+(\\\\.\\\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }*/
}