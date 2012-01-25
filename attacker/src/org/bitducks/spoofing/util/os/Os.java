package org.bitducks.spoofing.util.os;

public enum Os {
	
	WINDOWS("windows"),
	LINUX("linux"),
	MAC("mac"),
	UNKNOWN("");
	
	private String identifier;
	
	private Os(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}

}
