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
import android.widget.ImageView;
import android.widget.Toast;

import com.finance.hytone.constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Form2 extends AppCompatActivity {

    Bitmap bitm;
    private static final int REQUEST_CAMERA = 107;
    Uri imageUri;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);
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

    private void onCaptureImageResult(Intent data, Bitmap thumbnail) {
        currentPhotoPath = null;
        bitm = thumbnail;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final int factor = Helper.getCompressFactor(bitm, bytes);
        if(factor<0){
            bitm=null;
            Helper.showdialog(Form2.this,false,"Image is large","Image size is more than "+ Constants.LIMIT_IMAGE_SIZE2+"! Please attach image of lower size");
            return;

        }
        ErrorLogger.log("insideonCaptureImageResult",""+factor);
        ErrorLogger.log("insideonCaptureImageResult2",""+bitm.getWidth()+","+bitm.getHeight());

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

        ImageView imageView = findViewById(your image view resource id);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitm);
        Log.e("onacti1", "nothing");
        Log.e("onacti", "11");// + descimage);
    }

}