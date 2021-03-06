package org.bitducks.spoofing.util.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.LinkedList;

import jpcap.NetworkInterface;

/**
 * Linux Gateway Parser. Uses the netstat command
 * for finding the gateway of a network device.
 * This implementation should also work for MAC OS X, but
 * hasn't been tested for that platform.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class LinuxGatewayParser extends GatewayParser {
	
	private static String command = "netstat -nr";
	private static String defaultGateway = "0.0.0.0";

	/**
	 * Parse the result of the netstat command and return the gateway's
	 * IP Address.
	 */
	@Override
	public InetAddress findAndParse(NetworkInterface device) throws IOException {
		
		Process proc = Runtime.getRuntime().exec( LinuxGatewayParser.command );
		
		BufferedReader output = new BufferedReader(
				new InputStreamReader( proc.getInputStream() ) );
		
		LinkedList<String> lines = new LinkedList<String>();
		
		//Skip first 2 lines
		output.readLine();
		output.readLine();
		
		String line = output.readLine();
		
		while( line != null ) {
			lines.add( line );
			line = output.readLine();
		}
		
		InetAddress gateway = null;
		while( gateway == null && !lines.isEmpty() ) {
			
			line = lines.pop();
			String parts[] = line.split("\\s+");
			
			String destination = parts[0];
			String ipAddress = parts[1];
			String linuxInterface = parts[ parts.length - 1 ];
			
			if( linuxInterface.equals( device.name ) && 
				destination.equals( LinuxGatewayParser.defaultGateway ) ) 
			{
				gateway = InetAddress.getByName(ipAddress);
			}
			
		}
		
		return gateway;
		
	}

}
