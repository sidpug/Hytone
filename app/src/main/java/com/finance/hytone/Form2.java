package com.finance.hytone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.finance.hytone.constants.Constants;
import com.finance.hytone.filter.PanFilter;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.regex.Pattern;

public class Form2 extends AppCompatActivity {

    Bitmap bit;
    private static final int REQUEST_CAMERA = 107;
    Uri imageUri;
    String currentPhotoPath;
    private ImageView target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);
        EditText pan_number = findViewById(R.id.pan_number);
        pan_number.setFilters(new InputFilter[]{new PanFilter()});
        aadhar();
        findViewById(R.id.submit_form2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handleSection1() && aadhar() && pan() && voter_id() && dl() && passport() && bank())
                {
                    submitForm();
                }
            }
        });
    }

    private void submitForm() {

    }

    public boolean handleSection1(){
        String mob = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.mob)).getText()).toString().trim();
        String fName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.fName)).getText()).toString().trim();
        String lName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.lName)).getText()).toString().trim();
        String nName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.nickName)).getText()).toString().trim();
        String email = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.email)).getText()).toString().trim();
        String dep = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.dependent)).getText()).toString().trim();
        String error = null;
        if (fName.length()==0)
            error = "Please enter your First name";
        //else if(eLname..)


        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean address(){
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

        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean office_address(){
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
        /*if (fName.length()==0)
            error = "Please enter your First name";*/
        //else if(eLname..)

        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean aadhar() {
        String aadhar_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.aadhar_number)).getText()).toString().trim();
        String error = null;
        if (aadhar_no.length() == 0 || aadhar_no.length() < 12) {
            ((TextInputEditText) findViewById(R.id.aadhar_number)).setError("Enter Valid Aadhar Number");
            requestFocus(findViewById(R.id.aadhar_number));
        }
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

        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean pan(){
        String pan_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.pan_number)).getText()).toString().trim();
        String error = null;
        if (pan_no.length() == 0 || pan_no.length() < 10)
            ((TextInputEditText) findViewById(R.id.pan_number)).setError("Enter Valid Pan Number");

        ImageView front = findViewById(R.id.front_pan);
        ImageView back = findViewById(R.id.back_pan);
        findViewById(R.id.pan_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.pan_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean voter_id(){
        String voter_id_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.voter_id_number)).getText()).toString().trim();
        String error = null;
        if (voter_id_no.length() == 0 )
            ((TextInputEditText) findViewById(R.id.voter_id_number)).setError("Enter Valid Voter Id Number");

        ImageView front = findViewById(R.id.front_voter_id);
        ImageView back = findViewById(R.id.back_voter_id);
        findViewById(R.id.voter_id_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.voter_id_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean dl(){
        String dl_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.dl_no)).getText()).toString().trim();
        String error = null;
        if (dl_no.length() == 0 )
            ((TextInputEditText) findViewById(R.id.dl_no)).setError("Enter Valid Driving Licence Number");

        ImageView front = findViewById(R.id.front_dl_no);
        ImageView back = findViewById(R.id.back_dl_no);
        findViewById(R.id.dl_no_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.dl_no_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean passport(){
        String passport = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.passport_number)).getText()).toString().trim();
        String error = null;
        if (passport.length() == 0 )
            ((TextInputEditText) findViewById(R.id.passport_number)).setError("Enter Valid Passport Number");

        ImageView front = findViewById(R.id.front_passport);
        ImageView back = findViewById(R.id.back_passport);
        findViewById(R.id.passport_f_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });
        findViewById(R.id.passport_b_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent(); // TODO: here we have to pass the image view id to camera intent
            }
        });

        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean bank(){
        String bankname = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.bank_name)).getText()).toString().trim();
        String acc_no = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.acc_no)).getText()).toString().trim();
        String ifsc = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.ifsc_code)).getText()).toString().trim();
        String error = null;
        if (bankname.length() == 0 )
            ((TextInputEditText) findViewById(R.id.bank_name)).setError("Enter Valid Bank Name");

        if (acc_no.length() == 0 )
            ((TextInputEditText) findViewById(R.id.acc_no)).setError("Enter Valid Account Number");

        if (ifsc.length() == 0 )
            ((TextInputEditText) findViewById(R.id.ifsc_code)).setError("Enter Valid Ifsc Code");

        ImageView front = findViewById(R.id.statement);
        findViewById(R.id.statement_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();//TODO: here we have to pass the image view id to camera intent
            }
        });


        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
        return true;
    }

    public boolean assets(){
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

        if (error!=null)
        {
            Helper.showdialog(Form2.this, true, "", error);
            return false;
        }
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

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK )
            try {
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                onCaptureImageResult(data, thumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    int previewImgResId=-1;

    private void onCaptureImageResult(Intent data, Bitmap thumbnail) {
        currentPhotoPath = null;
        bit = thumbnail;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final int factor = Helper.getCompressFactor(bit, bytes);
        if(factor<0){
            target = null;
            bit =null;
            Helper.showdialog(Form2.this,false,"Image is large","Image size is more than "+ Constants.LIMIT_IMAGE_SIZE2+"! Please attach image of lower size");
            return;

        }
        Helper.log("insideonCaptureImageResult",""+factor);
        Helper.log("insideonCaptureImageResult2",""+ bit.getWidth()+","+ bit.getHeight());

        File destination = new File(Constants.PATH_HYTONE_FOLDER);
        if (!destination.exists())
            destination.mkdirs();

        destination = new File(Constants.PATH_HYTONE_FOLDER,
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            boolean bb = destination.createNewFile();
            fo = new FileOutputStream(Constants.PATH_HYTONE_FOLDER);
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

    public void requestFocus(View view)
    {
        if(view.requestFocus())
        {
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