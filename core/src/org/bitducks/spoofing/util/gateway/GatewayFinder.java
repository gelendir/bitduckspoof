package org.bitducks.spoofing.util.gateway;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.NetworkInterface;

import org.bitducks.spoofing.exception.UnexpectedErrorException;
import org.bitducks.spoofing.util.os.Os;
import org.bitducks.spoofing.util.os.OsDiscovery;

/**
 * Utility for finding out the gateway of a network device by using
 * OS utilities. Commands are sent to the underlying OS and the result
 * is parsed. Parsing is platform-dependent and must be implemented for every
 * OS used.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public abstract class GatewayFinder {
	
	/**
	 * Find the gateway IP adddress for a network device.
	 * 
	 * @param device The network device
	 * @return Gateway's IP Address
	 * @throws IOException Exception if there was an error sending commands to the OS.
	 */
	static public InetAddress find( NetworkInterface device ) throws IOException {
		
		Os os = OsDiscovery.discover();
		
		if( os == Os.UNKNOWN ) {
			throw new UnexpectedErrorException("Could not detect OS.");
		}
		
		GatewayParser parser = null;
		
		if( os == Os.LINUX ) { 
			parser = new LinuxGatewayParser();
		} else if ( os == Os.WINDOWS ) {
			parser = new WindowsGatewayParser();
		}
		
		InetAddress address = parser.findAndParse(device);
		if( address == null ) {
			throw new UnexpectedErrorException("Could not parse routing table when looking up gateway");
		}
		
		return address;

	}

}
