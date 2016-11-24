package com.forsuntech.secchkphone.svrmsg;

import java.util.List;

import com.forsuntech.secchkphone.bean.VirusBean;
import com.forsuntech.secchkphone.dao.VirusDao;
import com.forsuntech.secchkphone.util.MD5Utils;
import com.forsuntech.secchkphone.util.MyApplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class OrgAntiVirus {
	private Context context;

	public OrgAntiVirus() {
		context = MyApplication.getContextObject();
	}

	public String checkApp(String pkgName) {
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> installedPackages = manager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

		Long cur1 = System.currentTimeMillis();
		for (PackageInfo packageInfo : installedPackages) {

			// new Thread(new Runnable() {

			// @Override
			// public void run() {
			// // TODO 自动生成的方法存根
			// try {
			// Thread.sleep(200);
			// } catch (InterruptedException e) {
			// // TODO 自动生成的 catch 块
			// e.printStackTrace();
			// }
			// }
			// });

			// // 计算apk的md5
			// String path = packageInfo.applicationInfo.sourceDir;// 获取apk文件路径
			// // System.out.println(path);
			// String md5 = MD5Utils.encodeFile(path);
			// // 判断是否是病毒
			// boolean isVirus = VirusDao.isVirus(md5);
			// if (isVirus) {
			// Log.i("PackageInfo", packageInfo.packageName+"发现病毒");
			// } else {
			// Log.i("PackageInfo", "扫描安全");
			// System.out.println(packageInfo.packageName+"扫描安全");
			// }

		}
		Long cur2 = System.currentTimeMillis();
		Log.i("PackageInfo", "杀毒耗时" + (cur2 - cur1));
		PackageInfo packageInfo;

		try {
			packageInfo = manager.getPackageInfo(pkgName, 0);

			// 计算apk的md5
			String path = packageInfo.applicationInfo.sourceDir;// 获取apk文件路径
			// System.out.println(path);
			String md5 = MD5Utils.encodeFile(path);
			// 判断是否是病毒
			boolean isVirus = VirusDao.isVirus(md5);
			if (isVirus) {
				System.out.println("发现病毒");
				
				VirusBean vBean=VirusDao.getVirusInfo(md5);
				StringBuilder sBuilder = new StringBuilder();

				if(vBean!=null){
					sBuilder.append("{\"ret\":\"");
					sBuilder.append(0);
					sBuilder.append("\",\"desc\":\"");
					sBuilder.append("成功");
					sBuilder.append("\",\"opType\":\"");
					sBuilder.append("5");
					sBuilder.append("\",\"vInfo\":{");

					sBuilder.append("\"vName\":\"");
					sBuilder.append(vBean.getName());
					sBuilder.append("\",\"vType\":\"");
					sBuilder.append(vBean.getType());
					sBuilder.append("\",\"vDetail\":\"");
					sBuilder.append(vBean.getDesc());
					sBuilder.append("\",\"vRemark\":\"");
					sBuilder.append("无");
					sBuilder.append("\"}}");
				}else{
					sBuilder.append("{\"ret\":\"");
					sBuilder.append("-1");
					sBuilder.append("\",\"desc\":\"");
					sBuilder.append("失败");
					sBuilder.append("\",\"opType\":\"");
					sBuilder.append("5");
					sBuilder.append("\",\"vInfo\":{");

					
					sBuilder.append("\"}}");
				}
				
				

				return sBuilder.toString();
			} else {
				System.out.println("扫描安全");
				StringBuilder sBuilder = new StringBuilder();

				sBuilder.append("{\"ret\":\"");
				sBuilder.append(0);
				sBuilder.append("\",\"desc\":\"");
				sBuilder.append("成功");
				sBuilder.append("\",\"opType\":\"");
				sBuilder.append("5");
				sBuilder.append("\",\"vInfo\":{");

				sBuilder.append("\"vName\":\"");
				sBuilder.append("");
				sBuilder.append("\",\"vType\":\"");
				sBuilder.append("-1");
				sBuilder.append("\",\"vDetail\":\"");
				sBuilder.append("");
				sBuilder.append("\",\"vRemark\":\"");
				sBuilder.append("");
				sBuilder.append("\"}}");

				return sBuilder.toString();
			}

		} catch (NameNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append("{\"ret\":\"");
		sBuilder.append("-1");
		sBuilder.append("\",\"desc\":\"");
		sBuilder.append("失败");
		sBuilder.append("\",\"opType\":\"");
		sBuilder.append("5");
		sBuilder.append("\",\"vInfo\":{");

		
		sBuilder.append("\"}}");

		return sBuilder.toString();
	}

}
