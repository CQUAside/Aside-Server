package com.round.aside.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *	MD5摘要
 * @author A Shuai
 *
 */
public class MD5Utils {
	
	/**
	 *	使用MD5得到报文信息摘要
	 * @param text
	 * @return
	 */
	public static String encryptionInfoByMd5(String text) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(text.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 *	将得到的报文摘要转换为16进制数字
	 * @param b
	 * @return
	 */
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);		//无符号右移，高位补0
			sb.append(HEX_DIGITS[b[i] & 0x0f]);  
		}
		return sb.toString();
	}

}