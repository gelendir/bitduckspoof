package org.bitducks.spoofing.util.os;

/**
 * Service for detecting what Operating System the current
 * program is running on. Used by the Gateway Finder.
 * 
 * @see GatewayFinder
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class OsDiscovery {
	
	/**
	 * Try to detect what Operating System is currently running.
	 * 
	 * @return OS detected.
	 */
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
