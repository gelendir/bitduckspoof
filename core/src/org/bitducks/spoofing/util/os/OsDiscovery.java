package org.bitducks.spoofing.util.os;

public class OsDiscovery {
	
	public static Os discover() {
		
		String identifier = System.getProperty("os.name").toLowerCase();
				
		for( Os testOs: Os.values() ) {
			if( identifier.startsWith( testOs.getIdentifier() ) ) {
				return testOs;
			}
		}
		
		return Os.UNKNOWN;
		
	}

}
