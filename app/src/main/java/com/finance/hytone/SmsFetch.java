package com.finance.hytone;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.finance.hytone.model.SmsModel;

import java.util.ArrayList;
import java.util.List;

public class SmsFetch {
    public List<SmsModel> getAllSms(Context ct) {
        List<SmsModel> lstSms = new ArrayList<SmsModel>();
        SmsModel objSmsModel = new SmsModel();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = ct.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        //mActivity.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
//            for (int i = 0; i < 5; i++) {

                objSmsModel = new SmsModel();
                objSmsModel.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSmsModel.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSmsModel.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSmsModel.setReadState(c.getString(c.getColumnIndex("read")));
                objSmsModel.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSmsModel.setFolderName("inbox");
                } else {
                    objSmsModel.setFolderName("sent");
                }

                lstSms.add(objSmsModel);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }
}
