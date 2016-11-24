package com.forsuntech.secchkphone.bean;

public class AppInfoBean {
	
//	包唯一标识包名：packageName   包版本：versionName
//	编码路径：codePath       
//	APP名字：AppName         源文件目录：sourceDir
//	图标内容：iconContent    MD5算法：MD5
	
	private String packageName,versionName,codePath,AppName,sourceDir,iconContent,MD5,permission;

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getCodePath() {
		return codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}

	public String getAppName() {
		return AppName;
	}

	public void setAppName(String appName) {
		AppName = appName;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getIconContent() {
		return iconContent;
	}

	public void setIconContent(String iconContent) {
		this.iconContent = iconContent;
	}

	public String getMD5() {
		return MD5;
	}

	public void setMD5(String mD5) {
		MD5 = mD5;
	}
	

}
