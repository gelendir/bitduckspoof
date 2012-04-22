package org.bitducks.spoofing.util;

/**
 * Various Constants used throughout the project.
 *
 */
public class Constants {

	/**
	 * Blank MAC Address (00:00:00:00:00:00)
	 */
	public static byte[] BLANK = new byte[]{(byte)0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0};
	
	/**
	 * Broadcast MAC Address
	 */
	public static byte[] BROADCAST = new byte[]{(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
	
	/**
	 * Broadcast IP Address
	 */
	public static byte[] BROADCAST_IP = new byte[]{(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
	
	/**
	 * Blank IP Address (0.0.0.0)
	 */
	public static byte[] NO_IP = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
	
	/**
	 * Number of bytes in an IPv4 Address
	 */
	public static int IPV4_LEN = 4;
	
	/**
	 * Number of bytes in a MAC Address
	 */
	public static int MAC_LEN = 6;
	
}
