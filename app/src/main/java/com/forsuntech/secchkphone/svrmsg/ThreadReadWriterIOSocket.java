package com.forsuntech.secchkphone.svrmsg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.forsuntech.secchkphone.MainActivity;
import com.forsuntech.secchkphone.R;
import com.forsuntech.secchkphone.service.ControlIOService;
import com.forsuntech.secchkphone.util.LogWriterUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ThreadReadWriterIOSocket implements Runnable {
    private Socket client;
    private Context context;
    private byte[] buffer;
    private LogWriterUtils logWriterUtils=null;

    private static final String TAG = ThreadReadWriterIOSocket.class.getName();

    public ThreadReadWriterIOSocket(Context context, Socket client) {
        this.client = client;
        this.context = context;
        logWriterUtils=new LogWriterUtils("ThreadReadWriterIOSocket.log");
        logWriterUtils.log("-----------------------------网络连接初始化------------------------\n");
    }

    @Override
    public void run() {
        Log.d("chl", "a client has connected to server!");
        BufferedOutputStream out;
        BufferedInputStream in;
        try {
            /* PC端发来的数据msg */
            String currCMD = "";
            out = new BufferedOutputStream(client.getOutputStream());
            in = new BufferedInputStream(client.getInputStream());
            DataOutputStream outData = new DataOutputStream(client.getOutputStream());
            ControlIOService.ioThreadFlag = true;
            while (ControlIOService.ioThreadFlag) {
                try {
                    if (!client.isConnected()) {
                        break;
                    }
					/* 接收PC发来的数据 */
                    Log.v(ControlIOService.TAG, Thread.currentThread().getName() + "---->" + "will read......");
					/* 读操作命令 */
                    currCMD = readCMDFromSocket(in);
                    Log.v(ControlIOService.TAG,
                            Thread.currentThread().getName() + "---->" + "**currCMD ==== " + currCMD);

                    String[] currCMDList=currCMD.split("#");

                    for (String cmd: currCMDList) {
                        String[] cmdList = cmd.replace("|", ",").split(",");
                        if (!TextUtils.equals(cmdList[0].toString(), "@")
                                && !TextUtils.equals(cmdList[cmdList.length - 1].toString(), "#")) {
                            out.write("命令有误".getBytes());
                            out.flush();
                            continue;
                        }
                        Log.v(ControlIOService.TAG,
                                Thread.currentThread().getName() + "---->" + "**currCMD ==== " + cmd);

                        Message message = new Message();
					/* 根据命令分别处理数据 */
                        int opType = Integer.valueOf(cmdList[2].toString().trim());
                        switch (opType) {

                            case 1:
                                Log.i(TAG, "获取SD卡文件。。。");
                                logWriterUtils.log("获取SD卡文件\n");
                                String filePath = cmdList[4].toString().trim();
                                OrgFileOperate fileOperate=new OrgFileOperate();
                                out.write(getRetResult(fileOperate.getVideoFileName(filePath)).getBytes());
                                out.flush();
//                            StringBuffer retFileName = new StringBuffer(fileOperate.getVideoFileName(filePath));
//                            StringBuffer retFileNameLength = new StringBuffer(String.valueOf(retFileName.toString().getBytes().length));
//                            Log.i("ThreadReadWriterIOSocket", String.valueOf(retFileNameLength));
//                            while (retFileNameLength.length() < 10) {
//                                retFileNameLength.insert(0, "0");
//                            }
//                            retFileName.insert(0, retFileNameLength)
                                break;
                            case 2:
                                Log.i(TAG,"获取短信内容");
                                logWriterUtils.log("获取短信内容\n");
                                OrgMessage orgMessage=new OrgMessage();
                                out.write(getRetResult(orgMessage.getSmsInPhone()).getBytes());
                                out.flush();
                                break;

                            case 3:
                                Log.i(TAG,"获取通讯录");
                                logWriterUtils.log("获取通讯录\n");
                                OrgContacts contacts=new OrgContacts();
                                out.write(getRetResult(contacts.getAllContacts()).getBytes());
                                out.flush();
                                break;
                            case 9:
                                Log.i(TAG,"获取通话记录");
                                logWriterUtils.log("获取通话记录\n");
                                OrgCallHistory callHistory=new OrgCallHistory();
                                out.write(getRetResult(callHistory.getCallHistory()).getBytes());
                                out.flush();
                                break;
                            case 4:
                                Log.i(TAG, "权限监测");
                                logWriterUtils.log("权限监测\n");
                                // 权限检测
                                OrgAppList info = new OrgAppList();
                                StringBuffer retAppList = new StringBuffer(info.getAppList());
                                StringBuffer retAppListlength = new StringBuffer(String.valueOf(retAppList.toString().getBytes().length));
                                Log.i("ThreadReadWriterIOSocket", String.valueOf(retAppList.toString().getBytes().length));
                                while (retAppListlength.length() < 10) {
                                    retAppListlength.insert(0, "0");
                                }
                                retAppList.insert(0, retAppListlength);
                                out.write(retAppList.toString().getBytes());
                                out.flush();
                                message.what = 0;
                                Bundle d = new Bundle();
                                d.putString("key", retAppList.toString());
                                message.setData(d);
                                MainActivity.handler.sendMessage(message);

                                retAppList.setLength(0);
                                retAppListlength.setLength(0);
                                break;
                            case 8:
                                Log.i(TAG, "图标获取");
                                logWriterUtils.log("图标获取\n");
                                String pkgName = cmdList[4].toString().trim();
                                OrgAppList appIcon = new OrgAppList();
                                Bitmap bitmap = appIcon.getAppIconByPkg(pkgName);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                Log.i("ThreadReadWriterIOSocket", "图片大小" + baos.size());
                                logWriterUtils.log("图标获取：图片大小:"+baos.size()+"\n");
                                StringBuffer bmLeng = new StringBuffer(String.valueOf(baos.size()));
                                while (bmLeng.length() < 10) {
                                    bmLeng.insert(0, "0");
                                }
                                out.write(bmLeng.toString().getBytes());
                                InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                                byte[] buf = new byte[1024];
                                int len = 0;
                                //2.往输出流里面投放数据
                                while ((len = isBm.read(buf)) > 0) {
                                    System.out.println(len);
                                    out.write(buf, 0, len);
                                }
                                out.flush();
                                logWriterUtils.log("图标获取:传输完成\n");
                                break;
                            case 6:
                                // 手机具体参数
                                Log.i(TAG, "手机具体参数");
                                logWriterUtils.log("手机具体参数\n");
                                OrgHardwareInfo hardinfo = new OrgHardwareInfo();
                                StringBuffer ret = new StringBuffer(hardinfo.getHardwareInfo());
                                StringBuffer retLenth = new StringBuffer(String.valueOf(ret.toString().getBytes().length));

                                while (retLenth.length() < 10) {
                                    retLenth.insert(0, "0");
                                }
                                ret.insert(0, retLenth);
                                out.write(ret.toString().getBytes());
                                out.flush();

                                message.what = 0;
                                Bundle b = new Bundle();
                                b.putString("key", ret.toString());
                                message.setData(b);
                                MainActivity.handler.sendMessage(message);

                                //清空字符串缓存
                                ret.setLength(0);
                                retLenth.setLength(0);
                                break;
                            case 7:
                                Log.i(TAG, "手机网络检测");
                                logWriterUtils.log("手机网络检测\n");
                                out.write("hello".getBytes());
                                out.flush();
                                message.what = 0;
                                Bundle c = new Bundle();
                                c.putString("key", cmd);
                                message.setData(c);
                                MainActivity.handler.sendMessage(message);
                                break;
                            case 5:
                                Log.i(TAG, "手机病毒检测");
                                logWriterUtils.log("手机病毒检测\n");
                                String pkgNameAnti = cmdList[4].toString().trim();
                                OrgAntiVirus orgAnti = new OrgAntiVirus();
                                StringBuffer retAnti = new StringBuffer(orgAnti.checkApp(pkgNameAnti));
                                StringBuffer retAntilength = new StringBuffer(String.valueOf(retAnti.toString().getBytes().length));

                                while (retAntilength.length() < 10) {
                                    retAntilength.insert(0, "0");
                                }
                                retAnti.insert(0, retAntilength);


                                out.write(retAnti.toString().getBytes());
                                out.flush();

                                message.what = 0;
                                Bundle f = new Bundle();
                                f.putString("key", cmd);
                                message.setData(f);
                                MainActivity.handler.sendMessage(message);
                                break;
                            default:
                                out.write("命令有误".getBytes());
                                out.flush();
                                break;
                        }
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    Log.v(ControlIOService.TAG, Thread.currentThread().getName() + "---->" + "client.close()");
                    client.close();
                }
            } catch (IOException e) {
                Log.e(ControlIOService.TAG, Thread.currentThread().getName() + "---->" + "read write error333333");
                e.printStackTrace();
            }
        }
    }


    private String getRetResult(String inputResult){
        StringBuffer outputResult = new StringBuffer(inputResult);
        StringBuffer outputResultLength = new StringBuffer(String.valueOf(outputResult.toString().getBytes().length));
        Log.i("ThreadReadWriterIOSocket", String.valueOf(outputResultLength));
        while (outputResultLength.length() < 10) {
            outputResultLength.insert(0, "0");
        }
      return  outputResult.insert(0, outputResultLength).toString();
    }
    /**
     * 功能：从socket流中读取完整文件数据
     * <p>
     * InputStream in：socket输入流
     * <p>
     * byte[] filelength: 流的前4个字节存储要转送的文件的字节数
     * <p>
     * byte[] fileformat：流的前5-8字节存储要转送的文件的格式（如.apk）
     */
    // public static byte[] receiveFileFromSocket(InputStream in, OutputStream
    // out, byte[] filelength, byte[] fileformat) {
    // byte[] filebytes = null;// 文件数据
    // try {
    // in.read(filelength);// 读文件长度
    // int filelen = MyUtil.bytesToInt(filelength);// 文件长度从4字节byte[]转成Int
    // String strtmp = "read file length ok:" + filelen;
    // out.write(strtmp.getBytes("utf-8"));
    // out.flush();
    //
    // filebytes = new byte[filelen];
    // int pos = 0;
    // int rcvLen = 0;
    // while ((rcvLen = in.read(filebytes, pos, filelen - pos)) > 0) {
    // pos += rcvLen;
    // }
    // Log.v(ControlIOService.TAG,
    // Thread.currentThread().getName() + "---->" + "read file OK:file size=" +
    // filebytes.length);
    // out.write("read file ok".getBytes("utf-8"));
    // out.flush();
    // } catch (Exception e) {
    // Log.v(ControlIOService.TAG, Thread.currentThread().getName() + "---->" +
    // "receiveFileFromSocket error");
    // e.printStackTrace();
    // }
    // return filebytes;
    // }

	/* 读取命令 */
    public String readCMDFromSocket(InputStream in) {
        int MAX_BUFFER_BYTES = 2048000;
        String msg = "";
        byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
        try {
            int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
            msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");
            tempbuffer = null;
        } catch (Exception e) {
            Log.v(ControlIOService.TAG, Thread.currentThread().getName() + "---->" + "readFromSocket error");
            e.printStackTrace();
        }
        tempbuffer = null;
        // Log.v(Service139.TAG, "msg=" + msg);
        return msg;
    }


    private String getBitmap(Socket socket) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//	        imageView02.setImageBitmap(bitmap);  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //读取图片到ByteArrayOutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }


        return "";
    }

    private String getPhoneType() {
        // String str="" ;
        // for(int i=0;i<1000;i++){
        // str+=android.os.Build.MODEL;
        // }
        return android.os.Build.MODEL;

    }


//	public boolean sendData(String data) {
//		byte[] temp = null;
//		MsgHeader header = new MsgHeader();
//		header.setLength(data.length());
//		header.setCmd(11);
//		this.buffer = new byte[data.length() + 20];
//
//		// add cmd
//		temp = Common.toLH(Protocol.PORTAL_DBC_INSTALLSYSTEM);
//		System.arraycopy(temp, 0, buffer, 0, 4);
//
//		// add lenght
//		temp = Common.toLH(header.getLength());
//		System.arraycopy(temp, 0, buffer, 4, 4);
//
//		// add para1
//		temp = Common.toLH(header.getPara1());
//		System.arraycopy(temp, 0, buffer, 8, 4);
//
//		// add para2
//		temp = Common.toLH(header.getPara2());
//		System.arraycopy(temp, 0, buffer, 12, 4);
//
//		// add para3
//		temp = Common.toLH(header.getPara3());
//		System.arraycopy(temp, 0, buffer, 16, 4);
//
//		System.arraycopy(data.getBytes(), 0, buffer, 20, data.length());
//
//		try {
//			client.getOutputStream().write(this.buffer);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return true;
//	}
}