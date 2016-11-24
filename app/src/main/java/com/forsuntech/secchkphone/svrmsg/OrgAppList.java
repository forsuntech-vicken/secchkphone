package com.forsuntech.secchkphone.svrmsg;

import java.util.List;

import com.forsuntech.secchkphone.bean.AppInfoBean;
import com.forsuntech.secchkphone.util.MyApplication;

import android.R;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

public class OrgAppList {
	private Context context;
	private PackageManager manager;
	private StringBuffer sBuilder;
	private StringBuffer sb;
	public OrgAppList() {
		context = MyApplication.getContextObject();
		manager = context.getPackageManager();
	}

	public StringBuffer getAppList() {
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		sBuilder = new StringBuffer();
		sBuilder.append("{\"ret\":\"");
		sBuilder.append(0);
		sBuilder.append("\",\"desc\":\"");
		sBuilder.append("成功");
		sBuilder.append("\",\"opType\":\"");
		sBuilder.append("4");
		sBuilder.append("\",\"appList\":[");
		Log.i("OrgAppList","包的数量："+ packages.size());
		for (int i = 0; i < packages.size(); i++) {
//		for (int i = 0; i < 160; i++) {
			PackageInfo packageInfo = packages.get(i);
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				Log.i("OrgAppList","系统应用："+ i);
				continue;
			}
			AppInfoBean tmpInfo = new AppInfoBean();
			tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
			tmpInfo.setPackageName(packageInfo.packageName);
			tmpInfo.setVersionName(packageInfo.versionName);
			PackageInfo pkgInfo;
			try {
				pkgInfo = manager.getPackageInfo(tmpInfo.getPackageName(), PackageManager.GET_PERMISSIONS);

				String[] permissionStrings = pkgInfo.requestedPermissions;
				sb = new StringBuffer();
				if (permissionStrings != null) {
					for (String permission : permissionStrings) {
						sb.append(permission + "|");
					}
				}

				tmpInfo.setPermission(sb.toString());

				sBuilder.append("{\"appName\":\"");
				sBuilder.append(tmpInfo.getAppName());

				sBuilder.append("\",\"packageName\":\"");
				sBuilder.append(tmpInfo.getPackageName());

				sBuilder.append("\",\"opPerssion\":\"");
				sBuilder.append(tmpInfo.getPermission());
//				sBuilder.append("");

				sBuilder.append("\",\"versionName\":\"");
				sBuilder.append(tmpInfo.getVersionName());

				sBuilder.append("\",\"codePath\":\"");
				sBuilder.append(tmpInfo.getCodePath());

				sBuilder.append("\",\"sourceDir\":\"");
				sBuilder.append(tmpInfo.getSourceDir());

				sBuilder.append("\",\"iconContent\":\"");
				sBuilder.append(tmpInfo.getIconContent());
				sBuilder.append("\",\"MD5\":\"");
				sBuilder.append(tmpInfo.getMD5());
				sBuilder.append("\"},");
				String str=sBuilder.substring(0);
				Log.i("OrgAppList", str);
				
				
				Log.i("OrgAppList", "数据大小："+String.valueOf(sBuilder.toString().getBytes().length));
			} catch (NameNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			tmpInfo.setAppName("");
			tmpInfo.setCodePath("");
			
			tmpInfo.setIconContent("");
			tmpInfo.setMD5("");
			tmpInfo.setPackageName("");
			tmpInfo.setPermission("");
			tmpInfo.setSourceDir("");
			tmpInfo.setVersionName("");
			sb.setLength(0);
		}
		
		
		sBuilder.deleteCharAt(sBuilder.lastIndexOf(","));
		sBuilder.append("]}");

		Log.i("权限监测",sBuilder.toString() );
		Log.i("OrgAppList", "数据大小："+String.valueOf(sBuilder.toString().getBytes().length));
		return sBuilder;
	}

	public Bitmap getAppIconByPkg(String pkgName){
		PackageInfo packageInfo;
		try {
			packageInfo = manager.getPackageInfo(pkgName, 0);
			Drawable icon=packageInfo.applicationInfo.loadIcon(context.getPackageManager());
			
			return drawable2Bitmap(icon);
		} catch (NameNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
	
	 private Bitmap drawable2Bitmap(Drawable drawable) {  
	        if (drawable instanceof BitmapDrawable) {  
	            return ((BitmapDrawable) drawable).getBitmap();  
	        } else if (drawable instanceof NinePatchDrawable) {  
	            Bitmap bitmap = Bitmap  
	                    .createBitmap(  
	                            drawable.getIntrinsicWidth(),  
	                            drawable.getIntrinsicHeight(),  
	                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
	                                    : Bitmap.Config.RGB_565);  
	            Canvas canvas = new Canvas(bitmap);  
	            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
	                    drawable.getIntrinsicHeight());  
	            drawable.draw(canvas);  
	            return bitmap;  
	        } else {  
	            return null;  
	        }  
	    }  
}
