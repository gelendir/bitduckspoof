package org.bitducks.spoofing.util;

public class Constants {

	public static byte[] BLANK = new byte[]{(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0};
	
	public static byte[] BROADCAST = new byte[]{(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
	
	public static byte[] BROADCAST_IP = new byte[]{(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
	
	public static byte[] NO_IP = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
	
	public static int IPV4_LEN = 4;
	
	public static int MAC_LEN = 6;
	
}
