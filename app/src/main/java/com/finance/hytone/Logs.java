package com.finance.hytone;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.finance.hytone.model.LogModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logs {
    public List<LogModel> getCallDetails(Context ctx) {

        //StringBuffer sb = new StringBuffer();
        List<LogModel> listLogs = new ArrayList<LogModel>();

        Cursor managedCursor = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        assert managedCursor != null;
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        //sb.append( "Call Details :");
        while (managedCursor.moveToNext()) {
            LogModel obj = new LogModel();
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.parseLong(callDate));
            String callDuration = managedCursor.getString(duration);

            obj.setAddress(phNumber);
            obj.setDate(callDayTime.toString());
            obj.setDuration(callDuration);
            obj.setType("NA");

            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    obj.setType(dir);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    obj.setType(dir);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    obj.setType(dir);
                    break;
            }

            listLogs.add(obj);
            //sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
            //sb.append("\n----------------------------------");
        }
        managedCursor.close();
        return listLogs;
    }
}
