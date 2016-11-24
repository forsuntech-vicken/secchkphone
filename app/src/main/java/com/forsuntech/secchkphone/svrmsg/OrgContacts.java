package com.forsuntech.secchkphone.svrmsg;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.forsuntech.secchkphone.util.MyApplication;

/**
 * Created by vicken on 2016/11/11.
 */

public class OrgContacts {

    private Context context = null;
    private static final String TAG = OrgContacts.class.getName();

    public OrgContacts() {
        context = MyApplication.getContextObject().getApplicationContext();
    }


    /*
     * 读取联系人的信息
     */
    public String getAllContacts() {
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;
        StringBuffer retStb=new StringBuffer();
        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            retStb.append("{\"ret\":\"0\",\"desc\":\"成功\",\"opType\":\"3\",\"contact\":[");
        }
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name="";
            name = cursor.getString(nameIndex);
            retStb.append("{\"contactName\":\"");
            retStb.append(name);
            retStb.append("\",\"contactId\":\"");
            retStb.append(contactId);
            Log.i(TAG, contactId);

            Log.i(TAG, name);

            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            String phoneNumber ="";
            while (phones.moveToNext()) {
                phoneNumber += phones.getString(phoneIndex)+",";

                Log.i(TAG, phoneNumber);
            }
            if(phones !=null){
                phones.close();
            }
            retStb.append("\",\"phoneNum\":\"");
            retStb.append(phoneNumber);
            retStb.append("\"},");

//            /*
//             * 查找该联系人的email信息
//             */
//            Cursor emails = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
//                    null, null);
//            int emailIndex = 0;
//            if (emails.getCount() > 0) {
//                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//            }
//            while (emails.moveToNext()) {
//                String email = emails.getString(emailIndex);
//                Log.i(TAG, email);
//            }

        }
        if(cursor!=null){
            cursor.close();
        }
        Log.i(TAG,retStb.length()>0?retStb.substring(0,retStb.lastIndexOf(",")).concat("]}"):"{\"ret\":\"-1\",\"desc\":\"服务器异常\",\"opType\":\"3\"}");
        return retStb.length()>0?retStb.substring(0,retStb.lastIndexOf(",")).concat("]}"):"{\"ret\":\"-1\",\"desc\":\"服务器异常\",\"opType\":\"3\"}";
    }


}
