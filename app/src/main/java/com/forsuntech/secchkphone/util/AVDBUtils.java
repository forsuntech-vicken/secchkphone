package com.forsuntech.secchkphone.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AVDBUtils {

	private Context ctx;

	public AVDBUtils(Context ctx) {
		this.ctx = ctx;
	}
	
	private static final String TABLE_MD5 = "datable";
	private static final String TABLE_VERSION = "version";
	
	/**
	 * 判断某个MD5值 是否符合病毒特征
	 * @param md5
	 * @return
	 */
	public String isCheckVirus(String md5){
		String result = null;
		
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir().getAbsolutePath()+"/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);
		
		Cursor cursor = db.query(TABLE_MD5, new String[]{"desc"}, "md5 = ?", new String[]{md5}, null, null, null);
		
		
		if(cursor.moveToNext()){
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		
		return result;
			
	}
	
	/**
	 * 返回当前病毒数据库的版本
	 * @return
	 */
	public String getCurrVersion(){
		
		String version = null;
		
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir().getAbsolutePath()+
				"/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);
		
		Cursor cursor = db.query(TABLE_VERSION, new String[]{"subcnt"}, null, null, null, null, null);
		
		if(cursor.moveToNext()){
			version = cursor.getString(0);
		}
		cursor.close();
		db.close();
		
		return version;
		
	}

	/**
	 * 新插入一条病毒特征
	 * @param md5
	 * @param desc
	 */
	public void insertVirus(String md5, String desc) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir().getAbsolutePath()+
				"/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
		
		ContentValues values = new ContentValues();
		
		values.put("md5", md5);
		values.put("type", 6);
		values.put("name", "android.chaojibingdu");
		values.put("desc", desc);
		
		db.insert(TABLE_MD5,"_id",values);
		
		db.close();
		
	}

	/**
	 * 更新数据库版本号
	 * @param version
	 */
	public void updateVirusVersion(String version) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir().getAbsolutePath()+
				"/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
		
		ContentValues values = new ContentValues();
		values.put("subcnt", version);
		db.update(TABLE_VERSION, values, null, null);
		
		db.close();
		
	}
	
	
}
