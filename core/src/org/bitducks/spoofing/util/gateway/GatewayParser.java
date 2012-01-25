package org.bitducks.spoofing.util.gateway;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.NetworkInterface;

public abstract class GatewayParser {
	
	public abstract InetAddress findAndParse( NetworkInterface device ) throws IOException;

}
