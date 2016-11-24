package com.forsuntech.secchkphone.svrmsg;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.forsuntech.secchkphone.util.MyApplication;

/**
 * Created by vicken on 2016/11/14.
 */


public class OrgCallHistory {

    private Context context = null;

    private static final int MY_PERMISSIONS_REQUEST_CALL_LOG = 0;
    private static final String TAG=OrgCallHistory.class.getName();

    public OrgCallHistory() {
        context = MyApplication.getContextObject();
    }

    /**
     * 获取通话记录
     */
    public String getCallHistory() {
        Log.i("callHistor", "通话：1");

        String callInfo = "";

        StringBuffer retStb=new StringBuffer();
        if (context == null) {
            return "{\"ret\":\"-1\",\"desc\":\"服务器异常\",\"opType\":\"9\"}";
        }
        ContentResolver contentResolver = context.getContentResolver();
//        long dure = System.currentTimeMillis() - 1000 * 60;
        Cursor cursor = null;
        try {

//            cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, "date>?", new String[]{Long.toString(dure)}, CallLog.Calls.DATE + " desc");
            cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc");
            if (cursor == null) {
                return "{\"ret\":\"-1\",\"desc\":\"服务器异常\",\"opType\":\"9\"}";
            }


            if (cursor.moveToFirst()) {
                retStb.append("{\"ret\":\"0\",\"desc\":\"成功\",\"opType\":\"9\",\"callHistory\":[");
                do {
                    callInfo += cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    callInfo += "类型：" + cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    callInfo += "日期" + cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                    callInfo += "持续时间：" + cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)) + "\r";
                    String destnum = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String callDuration = Long.toString(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                    String callTime = Long.toString(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
                    String type = "1";
                    switch (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))) {
                        case CallLog.Calls.INCOMING_TYPE:
                            type = "1"; // 呼入
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            type = "0"; // 呼出
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            type = "1"; // 未接
                            break;
                        default:
                            break;
                    }

                    retStb.append("{\"desNum\":\"");
                    retStb.append(destnum);
                    retStb.append("\",\"duration\":\"");
                    retStb.append(callDuration);
                    retStb.append("\",\"action\":\"");
                    retStb.append(type);
                    retStb.append("\",\"date\":\"");
                    retStb.append(callTime);
                    retStb.append("\"},");

                } while (cursor.moveToNext());
            }


        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i(TAG,retStb.length()>0?(retStb.substring(0, retStb.lastIndexOf(","))).concat("]}"):"{\"ret\":\"0\",\"desc\":\"成功\",\"opType\":\"9\"}");
        return retStb.length()>0?(retStb.substring(0, retStb.lastIndexOf(","))).concat("]}"):"{\"ret\":\"0\",\"desc\":\"成功\",\"opType\":\"9\"}";
    }
}
