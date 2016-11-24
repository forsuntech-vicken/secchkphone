package com.forsuntech.secchkphone.dao;

import com.forsuntech.secchkphone.bean.VirusBean;
import com.forsuntech.secchkphone.util.MyApplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 查询病毒的数据库封装
 * 
 * @author Kevin
 * 
 */
public class VirusDao {

//	private static final String PATH = "/data/data/com.forsuntech.secchkphone/files/antivirus.db";

	/**
	 * 判断是否是病毒
	 * 
	 * @param md5
	 *            apk文件的md5
	 * @return
	 */
	public static boolean isVirus(String md5) {
//		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
//				SQLiteDatabase.OPEN_READONLY);// 只接收data/data路径下的数据库文件
		
		SQLiteDatabase database = SQLiteDatabase.openDatabase(MyApplication.getContextObject().getFilesDir().getAbsolutePath()+
				"/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
		
		
		Cursor cursor = database.rawQuery(
				"select desc from datable where md5=?", new String[] { md5 });
		
		boolean isVirus = false;
		if (cursor.moveToFirst()) {
			isVirus = true;
		}

		cursor.close();
		database.close();

		return isVirus;
	}

	
	public static VirusBean  getVirusInfo(String md5) {
//		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
//				SQLiteDatabase.OPEN_READONLY);// 只接收data/data路径下的数据库文件
		
		SQLiteDatabase database = SQLiteDatabase.openDatabase(MyApplication.getContextObject().getFilesDir().getAbsolutePath()+
				"/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
		
		
		Cursor cursor = database.rawQuery(
				"select md5,name,type,desc from datable where md5=?", new String[] { md5 });
		VirusBean vBean=null;

		if (cursor.moveToFirst()) {
			 vBean=new VirusBean();
			 vBean.setMd5(cursor.getString(cursor.getColumnIndex("md5")));
			 vBean.setName(cursor.getString(cursor.getColumnIndex("name")));
			 vBean.setType(cursor.getString(cursor.getColumnIndex("type")));
			 vBean.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
		}

		cursor.close();
		database.close();

		return vBean;
	}
}
