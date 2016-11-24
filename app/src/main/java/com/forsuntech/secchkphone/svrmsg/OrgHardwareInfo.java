package com.forsuntech.secchkphone.svrmsg;

import java.nio.Buffer;

import com.forsuntech.secchkphone.bean.HardwareBean;
import com.forsuntech.secchkphone.util.MyApplication;
import com.forsuntech.secchkphone.util.hardwareutil.HardwareInfoUtil;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class OrgHardwareInfo {
	private HardwareBean hardwareBean;
	private HardwareInfoUtil hardwareUtil;
	private Build bd ;
	private TelephonyManager tm;
	private Context context;
	public OrgHardwareInfo(){
		context=MyApplication.getContextObject();
		hardwareBean=new HardwareBean();
		hardwareUtil=new HardwareInfoUtil();
		bd= new Build();
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
	}
	
	public String getHardwareInfo(){
		hardwareBean.setCpuType(Build.CPU_ABI);//CPU型号
		hardwareBean.setHardwareSerialNumber(tm.getDeviceId());
		hardwareBean.setICCID(tm.getSimSerialNumber());
		hardwareBean.setImei(tm.getDeviceId());
		hardwareBean.setImsi(tm.getSubscriberId());
		hardwareBean.setInternalStorageAvailableSize(hardwareUtil.getTotalInternalMemorySize());
		hardwareBean.setInternalStorageTotalSize(hardwareUtil.getLocalBlueMacAddress(context));
		hardwareBean.setMacAdress(hardwareUtil.getLocalWifiMacAddress(context));
		hardwareBean.setMoType(bd.MODEL);
		hardwareBean.setOS("Android");
		hardwareBean.setOSVerson(Build.VERSION.RELEASE);
		hardwareBean.setRoot(hardwareUtil.getIsRoot());
		hardwareBean.setSDKVersion(Build.VERSION.SDK);
		hardwareBean.setVerdor(Build.MANUFACTURER);
		
		
		StringBuilder sBuilder=new StringBuilder();
		
		sBuilder.append("{\"ret\":\"");
		sBuilder.append(0);
		sBuilder.append("\",\"desc\":\"");
		sBuilder.append("成功");
		sBuilder.append("\",\"opType\":\"");
		sBuilder.append("6");
		sBuilder.append("\",\"hardwareInfo\":{");
		
		
		sBuilder.append("\"moType\":\"");
		sBuilder.append(hardwareBean.getMoType());
		sBuilder.append("\",\"verdor\":\"");
		sBuilder.append(hardwareBean.getVerdor());
		sBuilder.append("\",\"OS\":\"");
		sBuilder.append(hardwareBean.getOS());
		sBuilder.append("\",\"OSVerson\":\"");
		sBuilder.append(hardwareBean.getOSVerson());
		sBuilder.append("\",\"SDKVersion\":\"");
		sBuilder.append(hardwareBean.getSDKVersion());
		sBuilder.append("\",\"imei\":\"");
		sBuilder.append(hardwareBean.getImei());
		sBuilder.append("\",\"imsi\":\"");
		sBuilder.append(hardwareBean.getImsi());
		sBuilder.append("\",\"ICCID\":\"");
		sBuilder.append(hardwareBean.getICCID());
		sBuilder.append("\",\"internalStorageTotalSize\":\"");
		sBuilder.append(hardwareBean.getInternalStorageTotalSize());
		sBuilder.append("\",\"internalStorageAvailableSize\":\"");
		sBuilder.append(hardwareBean.getInternalStorageAvailableSize());
		sBuilder.append("\",\"cpuType\":\"");
		sBuilder.append(hardwareBean.getCpuType());
		sBuilder.append("\",\"macAdress\":\"");
		sBuilder.append(hardwareBean.getMacAdress());
		sBuilder.append("\",\"hardwareSerialNumber\":\"");
		sBuilder.append(hardwareBean.getHardwareSerialNumber());
		sBuilder.append("\",\"root\":\"");
		sBuilder.append(hardwareBean.getRoot());
		sBuilder.append("\"}}");
		return sBuilder.toString();
	}
	
	
	
	
}
