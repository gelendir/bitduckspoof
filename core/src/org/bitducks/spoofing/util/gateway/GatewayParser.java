package org.bitducks.spoofing.util.gateway;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.NetworkInterface;

/**
 * Interface for implementing OS command parsing for each
 * platform. An implementation must return a Gateway address
 * for a specific device.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public abstract class GatewayParser {
	
	public abstract InetAddress findAndParse( NetworkInterface device ) throws IOException;

}
