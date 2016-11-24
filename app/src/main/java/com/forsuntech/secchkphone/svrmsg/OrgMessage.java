package com.forsuntech.secchkphone.svrmsg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import com.forsuntech.secchkphone.util.MD5Util;
import com.forsuntech.secchkphone.util.MyApplication;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vicken on 2016/11/11.
 */

public class OrgMessage {

    private Context context=null;
    private static final String TAG=OrgMessage.class.getName();
    public  OrgMessage(){
        context= MyApplication.getContextObject().getApplicationContext();
    }

    public String getSmsInPhone() {
        final String SMS_URI_ALL = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";
        final String SMS_URI_OUTBOX = "content://sms/outbox";
        final String SMS_URI_FAILED = "content://sms/failed";
        final String SMS_URI_QUEUED = "content://sms/queued";

        StringBuffer smsBuffer = new StringBuffer();

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信

            if (cur.moveToFirst()) {
                smsBuffer.append("{\"ret\":\"0\",\"desc\":\"成功\",\"opType\":\"2\",\"message\":[");
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
//                    int intPerson = cur.getInt(index_Person);
                    String strBody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else {
                        strType = "草稿";
                    }
                    smsBuffer.append("{\"desNum\":\"");
                    smsBuffer.append(MD5Util.getMD5(strAddress));
                    smsBuffer.append("\",\"content\":\"");
                    smsBuffer.append(MD5Util.getMD5(strBody));
                    smsBuffer.append("\",\"action\":\"");
                    smsBuffer.append(MD5Util.getMD5(strType));
                    smsBuffer.append("\",\"date\":\"");
                    smsBuffer.append(MD5Util.getMD5(strDate));
                    smsBuffer.append("\"},");

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }

        } catch (SQLiteException ex) {
            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }
        Log.i(TAG,smsBuffer.length()>0?smsBuffer.substring(0,smsBuffer.lastIndexOf(",")).concat("]}"):"{\"ret\":\"-1\",\"desc\":\"服务器异常\",\"opType\":\"1\"}");


        return smsBuffer.length()>0?smsBuffer.substring(0,smsBuffer.lastIndexOf(",")).concat("]}"):"{\"ret\":\"-1\",\"desc\":\"服务器异常\",\"opType\":\"1\"}";
    }


}


