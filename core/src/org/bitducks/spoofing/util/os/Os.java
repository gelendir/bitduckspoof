package org.bitducks.spoofing.util.os;

/**
 * Identifier for detecting an OS. Used in conjunction with
 * OsDiscovery.
 *  
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public enum Os {
	
	WINDOWS("windows"),
	LINUX("linux"),
	MAC("mac"),
	UNKNOWN("");
	
	/**
	 * String for identifying the OS
	 * 
	 */
	private String identifier;
	
	private Os(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}

}
