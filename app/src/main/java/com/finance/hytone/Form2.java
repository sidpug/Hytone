package com.finance.hytone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.finance.hytone.constants.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class Form2 extends AppCompatActivity {

    Bitmap bit;
    private static final int REQUEST_CAMERA = 107;
    Uri imageUri;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);
        findViewById(R.id.submit_form2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handleSection1())
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

    public boolean handleSection2(){
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

    public boolean handleSection3(){
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
            e.printStackTrace();
            Toast.makeText(this, "Error creating file!", Toast.LENGTH_LONG).show();
            return;
        }

        ImageView imageView = findViewById(previewImgResId);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bit);
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

}