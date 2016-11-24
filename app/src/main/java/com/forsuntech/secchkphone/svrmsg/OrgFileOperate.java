package com.forsuntech.secchkphone.svrmsg;

import android.util.Log;

import java.io.File;

/**
 * Created by vicken on 2016/11/11.
 */

public class OrgFileOperate {

    private static final String TAG = OrgFileOperate.class.getName();
    private StringBuffer stbFile=new StringBuffer();


    public String getVideoFileName(String fileAbsolutePath){
        stbFile.append("{\"ret\":\"0\",\"desc\":\"成功\",\"opType\":\"1\",\"fileList\":[");

        getFileName(fileAbsolutePath);
//        Log.i(TAG,stbFile.length()>0?stbFile.substring(0,stbFile.lastIndexOf(",")).concat("]}"):"{\"ret\":\"-1\",\"desc\":\"服务器异常\"}");

        return stbFile.length()>0?stbFile.substring(0,stbFile.lastIndexOf(",")).concat("]}"):"{\"ret\":\"-1\",\"desc\":\"服务器异常\",\"opType\":\"1\"}";

    }
    // 获取当前目录下所有的mp4文件
    public  void getFileName(String fileAbsolutePath) {

        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        if (subFile != null) {
            if (subFile.length > 0) {

                for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                    // 判断是否为文件夹
                    if (!subFile[iFileLength].isDirectory()) {
                        String filename = subFile[iFileLength].getName();
                        // 判断是否为MP4结尾
//                if (filename.trim().toLowerCase().endsWith(".mp4")) {

                        Log.i(TAG, "文件路径：" + fileAbsolutePath +"/"+ filename);
                        stbFile.append("{\"fileName\":\"");
                        stbFile.append(fileAbsolutePath+"/"+ filename);
                        stbFile.append("\"},");
//                        stbFile.append(fileAbsolutePath).append(filename);
//                        Message msg=new Message();
//                        Bundle bundle=new Bundle();
//                        bundle.putString("file",fileAbsolutePath + filename);
//                        msg.setData(bundle);
//                        if (mHandler!=null){
//                            mHandler.handleMessage(msg);
//                        }
//                }
                    } else {
                        getFileName(subFile[iFileLength].getPath());
                    }
                }
            }
        }

    }
}
