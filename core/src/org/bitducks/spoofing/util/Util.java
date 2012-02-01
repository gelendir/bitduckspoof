package org.bitducks.spoofing.util;

public abstract class Util {
	
	public static int unixTimestamp() {
		return (int) (System.currentTimeMillis() / 1000L);
	}
	
	public static long unixTimestampMillis() {
		return System.currentTimeMillis();
	}

}
;