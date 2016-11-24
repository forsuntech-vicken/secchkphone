package com.forsuntech.secchkphone.util.hardwareutil;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import com.forsuntech.secchkphone.util.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class HardwareInfoUtil {
	private void getInfo() {    
        TelephonyManager mTm = (TelephonyManager)MyApplication.getContextObject().getSystemService(Context.TELEPHONY_SERVICE);    
        String imei = mTm.getDeviceId();    
        String imsi = mTm.getSubscriberId();    
        String mtype = android.os.Build.MODEL; // 手机型号    
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得    
    }  
	
	
	
	
	
	
	
	
	
	private static BroadcastReceiver mIntentReceiver;
	public  static String getIsRoot(){
		if (is_root()) {
			return "是";
			
		}else{
			return "否";
			
		}
	}
	// 判断是否具有ROOT权限
			private  static boolean is_root(){
			    boolean res = false;
			    try{ 
			        if ((!new File("/system/bin/su").exists()) && 
			            (!new File("/system/xbin/su").exists())){
			        res = false;
			        }else{
			        res = true;
			        };
			    }  catch (Exception e) {  
			    	e.printStackTrace();
			    } 
			    return res;
			}
			public static String getAvailableInternalMemorySize() {//availRom
				File path = Environment.getDataDirectory(); // 获取数据目录
				StatFs stat = new StatFs(path.getPath());
				BigDecimal blockSize = new BigDecimal(stat.getBlockSize());
				BigDecimal availableBlocks = new BigDecimal(stat.getAvailableBlocks());
				BigDecimal div = new BigDecimal(1024);
				double d = ((((blockSize.multiply(availableBlocks)).divide(div)).divide(div)).divide(div)).doubleValue();
				DecimalFormat df = new DecimalFormat("#.00");
				// return
				// Formatter.formatFileSize(activity,stat.getBlockSize()*stat.getAvailableBlocks());
				return df.format(d);
			}
			public static String getTotalInternalMemorySize() {//TotalRom
				File path = Environment.getDataDirectory();
				StatFs stat = new StatFs(path.getPath());
				BigDecimal blockSize = new BigDecimal(stat.getBlockSize());
				BigDecimal totalBlocks = new BigDecimal(stat.getBlockCount());
				BigDecimal div = new BigDecimal(1024);
				double d = ((((blockSize.multiply(totalBlocks)).divide(div)).divide(div)).divide(div)).doubleValue();

				DecimalFormat df = new DecimalFormat("#.00");
				Log.i("TAG", "原来" + df.format(d));
				// Log.i("TAG", "现在"+Formatter.formatFileSize(activity,
				// stat.getBlockSize()*stat.getBlockCount()));
				return df.format(d);
			}
			public static String getAvailMemory(Context context){
		        // 获取android当前可用内存大小 
		        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		        MemoryInfo mi = new MemoryInfo();
		        am.getMemoryInfo(mi);
		        //mi.availMem; 当前系统的可用内存
		        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化 
		        //String str=Formatter.formatFileSize(this, mi.availMem);
		        /*System.out.println("可用内存---->>>"+mi.availMem/(1024*1024));
		        System.out.println("可用内存---->>>"+str);*/		        
		        return Formatter.formatFileSize(context, mi.availMem);
		    }
			//获取总运存大小
			public static String getTotalMemory(Context context){
			        String str1 = "/proc/meminfo";// 系统内存信息文件 
			        String str2;
			        String[] arrayOfString;
			        long initial_memory = 0; 
			        try 
			        {
			            FileReader localFileReader = new FileReader(str1);
			            BufferedReader localBufferedReader = new BufferedReader(
			            localFileReader, 8192);
			            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			            arrayOfString = str2.split("\\s+");
			            for (String num : arrayOfString) {
			                Log.i(str2, num + "\t");
			            }
			            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte 
			            localBufferedReader.close();
			        } catch (IOException e) {
			        }
			       /* System.out.println("总运存--->>>"+Formatter.formatFileSize(this,initial_memory));// Byte转换为KB或者MB，内存大小规格化 
			        System.out.println("总运存--->>>"+initial_memory/(1024*1024));*/
			        return Formatter.formatFileSize(context,initial_memory);
			    }
			public static String getAvailableExternalMemorySize() {
				if (externalMemoryAvailable()) {
					//得到SD卡的路径
					File path = Environment.getExternalStorageDirectory();
					//创建StatFs对象，用来获取文件系统的状态
					StatFs stat = new StatFs(path.getPath());
					
					BigDecimal blockSize=new BigDecimal(stat.getBlockSize());
					BigDecimal availableBlocks = new BigDecimal(stat.getAvailableBlocks());
					BigDecimal div = new BigDecimal(1024);
					double d = ((((blockSize.multiply(availableBlocks)).divide(div))
							.divide(div)).divide(div)).doubleValue();
					DecimalFormat df = new DecimalFormat("#.00");
					
					//String totalSize = Formatter.formatFileSize(getApplicationContext(), stat.getAvailableBlocks()*stat.getBlockSize());//格式化获得SD卡总容量
					return df.format(d);
					//return totalSize;
				} else {
					return null;
				}
			}
			public static boolean externalMemoryAvailable() {
				return Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED);
			}
			public static String getTotalExternalMemorySize() {
				if (externalMemoryAvailable()) {
					File path = Environment.getExternalStorageDirectory();
					StatFs stat = new StatFs(path.getPath());
					BigDecimal blockSize =new BigDecimal(stat.getBlockSize());
					BigDecimal totalBlocks =new BigDecimal(stat.getBlockCount());
					BigDecimal div = new BigDecimal(1024);
					double d = ((((blockSize.multiply(totalBlocks)).divide(div))
							.divide(div)).divide(div)).doubleValue();
					DecimalFormat df = new DecimalFormat("#.00");
					//return Formatter.formatFileSize(getApplicationContext(), stat.getBlockSize()*stat.getBlockCount());
					return df.format(d);
				} else {
					return null;
				}
			}
			
			public static String getLocalBlueMacAddress(Context context) {

				if(Integer.valueOf(Build.VERSION.SDK)>18){
					BluetoothManager blue = (BluetoothManager) context.getSystemService((context).BLUETOOTH_SERVICE);
					BluetoothAdapter adapter = blue.getAdapter();
					if(adapter!=null){
						return adapter.getAddress();
					}else{
						return "不支持蓝牙";
					}
				}else{
					return "";
				}

			}
			public static String getLocalWifiMacAddress(Context context) {
				WifiManager wifi = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
				WifiInfo info = wifi.getConnectionInfo();
				return info.getMacAddress();

			}
			
			
			
			public static String getOpenTime(Context context){
				/*TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);		
				String operator = telManager.getSimOperator();*/
			        long time=SystemClock.elapsedRealtime();
			        long h=time/3600000;
			        long m=time%3600000/60000;
				String openTime = h+"时"+m+"分";
				return openTime;
			}
			public static String getYis(Context context){
				TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);		
				String operator = telManager.getSimOperator();
				String yis=null;
				if (operator != null) {
					if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {				
						yis= "运营商：中国移动";
					} else if (operator.equals("46001")) {			
						yis= "运营商：中国联通";
					} else if (operator.equals("46003")) {		
						yis= "运营商：中国电信";
					} else {
						yis= "运营商：未知";
					}
				}
				return yis;
			}
			public static String getFlowInfo(Context context){
				//1.获取一个包管理器。  
				PackageManager pm = context.getPackageManager();  
				//2.遍历手机操作系统 获取所有的应用程序的uid  
				List<ApplicationInfo> appliactaionInfos = pm.getInstalledApplications(0);  
				for(ApplicationInfo applicationInfo : appliactaionInfos){  
				    int uid = applicationInfo.uid;    // 获得软件uid  
				    //proc/uid_stat/10086  
				    long tx = TrafficStats.getUidTxBytes(uid);//发送的 上传的流量byte  
				    long rx = TrafficStats.getUidRxBytes(uid);//下载的流量 byte  
				    //方法返回值 -1 代表的是应用程序没有产生流量 或者操作系统不支持流量统计  
				}  
//				TrafficStats.getMobileTxBytes();//获取手机3g/2g网络上传的总流量  
//				TrafficStats.getMobileRxBytes();//手机2g/3g下载的总流量  
				  
				
				//Log.i("DateInfo", "上传"+TrafficStats.getTotalTxBytes()/1024/1024+"MB");
				//Log.i("DateInfo", "下载"+TrafficStats.getTotalRxBytes()/1024/1024+"MB");
				long x=TrafficStats.getTotalTxBytes()/1024/1024;//手机全部网络接口 包括wifi，3g、2g上传的总流量  
				long y=TrafficStats.getTotalRxBytes()/1024/1024;//手机全部网络接口 包括wifi，3g、2g下载的总流量  
				return (x+y)+"";
			}
	
}
