package com.forsuntech.secchkphone.svrmsg;

import java.net.*;

/**
 *
 * 字节转换，参考网络文章
 */
public class Packet {
	private byte[] buf = null;

	/**
	 * 将int转为低字节在前，高字节在后的byte数组
	 */
	private static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * 将float转为低字节在前，高字节在后的byte数组
	 */
	private static byte[] toLH(float f) {
		return toLH(Float.floatToRawIntBits(f));
	}

	/**
	 * 构造并转换
	 */
	public Packet(int packetID, int packetLen, String packetBody) {
		byte[] temp = null;
		buf = new byte[packetBody.getBytes().length + 8];
		temp = toLH(packetID);
		System.arraycopy(temp, 0, buf, 0, temp.length);
		temp = toLH(packetLen);
		System.arraycopy(temp, 0, buf, 4, temp.length);
		System.arraycopy(packetBody.getBytes(), 0, buf, 8, packetBody.length());
	}

	/**
	 * 返回要发送的数组
	 */
	public byte[] getBuf() {
		return buf;
	}

//	/**
//	* 发送测试
//	*/
//	public static void main(String[] args) {
//	try {
//	String tmp = "“test string!";
//	Socket sock = new Socket("127.0.0.1", 8888);
//	sock.getOutputStream().write(
//	new Packet(123, tmp.length(), tmp).getBuf());
//	sock.close();
//	} catch (Exception e) {
//	e.printStackTrace();
//	}
//	}
//}

}