package org.bitducks.spoofing.util.os;

public class OsDiscovery {
	
	public static Os discover() {
		
		String identifier = System.getProperty("os.name").toLowerCase();
				
		for( Os testOs: Os.values() ) {
			if( testOs.getIdentifier().equals( identifier ) ) {
				return testOs;
			}
		}
		
		return Os.UNKNOWN;
		
	}

}
