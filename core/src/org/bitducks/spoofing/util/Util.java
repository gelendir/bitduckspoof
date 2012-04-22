package org.bitducks.spoofing.util;

/**
 * Various utilities used throughout the project
 * 
 * @author Gregory Eric Sanderson <gzou200@gmail.com>
 *
 */
public abstract class Util {
	
	/**
	 * Return the current time as a UNIX timestamp
	 * @return UNIX timestamp
	 */
	public static int unixTimestamp() {
		return (int) (System.currentTimeMillis() / 1000L);
	}
	
	/**
	 * Return the current time as a UNIX timestamp, including milliseconds
	 * @return UNIX timestamp with milliseconds
	 */
	public static long unixTimestampMillis() {
		return System.currentTimeMillis();
	}

}
;