package com.finance.hytone.constants;

import android.os.Environment;

import java.io.File;

public class Constants {

    public static final boolean IS_DEBUG = true;//seconds
    public static final int LOC_UPDATE_INTERVAL = 1800;//seconds
    public static final int LOCATION_REQUEST_CODE = 500;
    public static final String LOGINTYPE_GOOGLE = "google";
    public static final String PATH_HYTONE_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "Hytone";
    public static final float LIMIT_IMAGE_SIZE_MB = 1f;
    public static final String LIMIT_IMAGE_SIZE2 = "1 MB";
    public static final int REQUEST_CHECK_SETTINGS = 99;

    public static final String FORM_NAME_BASIC = "basic";
    public static final String FORM_NAME_OFFICE = "office";
    public static final String FORM_NAME_ASSETS = "assets";
    public static final String FORM_NAME_BANK = "bank";
    public static final String FORM_NAME_AADHAR = "aadhar";
    public static final String FORM_NAME_PAN = "pan";
    public static final String FORM_NAME_VOTER = "voter";
    public static final String FORM_NAME_DRIVING = "dl";
    public static final String FORM_NAME_VEHICLE = "vehicle";
    public static final String FORM_NAME_REFERENCE = "refer";
    public static final String FORM_NAME_PASSPORT = "passport";

}
