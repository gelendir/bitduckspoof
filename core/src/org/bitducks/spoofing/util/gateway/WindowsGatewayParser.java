package org.bitducks.spoofing.util.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.LinkedList;

import org.bitducks.spoofing.core.InterfaceInfo;

import jpcap.NetworkInterface;

/**
 * Windows Gateway Parser. Uses the netstat command
 * for finding the gateway of a network device.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class WindowsGatewayParser extends GatewayParser {
	
	private static String command = "netstat -nr";
	private static String defaultGateway = "0.0.0.0";
	
	private static String ipRouteTableHeader = "IPv4 Route Table";
	private static String ipRouteTableBanner = "====";

	/**
	 * Parse the result of the netstat command and return the gateway's
	 * IP Address.
	 */
	@Override
	public InetAddress findAndParse(NetworkInterface device) throws IOException {
		
		InterfaceInfo info = new InterfaceInfo( device );
		String address = info.getAddress().toString().substring(1);
		
		Process proc = Runtime.getRuntime().exec( WindowsGatewayParser.command );
		
		BufferedReader output = new BufferedReader(
				new InputStreamReader( proc.getInputStream() ) );
		
		LinkedList<String> lines = new LinkedList<String>();
		
		//Skip lines until we meet the IPv4 routing table
		String line = output.readLine();
		while( line != null && !line.equals( WindowsGatewayParser.ipRouteTableHeader ) ) {
			line = output.readLine();
		}
		
		if( line == null ) {
			throw new IOException("could not find IPv4 routing table in netstat output");
		}
		
		//skip route table banner
		output.readLine();
		output.readLine();
		output.readLine();
		
		line = output.readLine();
		while( line != null && !line.startsWith( WindowsGatewayParser.ipRouteTableBanner ) ) {
			lines.add(line);
			line = output.readLine();
		}
		
		if( line == null ) {
			throw new IOException("could not find IPv4 routing table in netstat output");
		}
		
		InetAddress gateway = null;
		while( gateway == null && !lines.isEmpty() ) {
			
			line = lines.pop();
			String parts[] = line.split("\\s+");
			
			String destination = parts[1];
			String ipAddress = parts[3];
			String windowsInterface = parts[4];
			
			if( windowsInterface.equals( address ) && 
				destination.equals( WindowsGatewayParser.defaultGateway ) ) 
			{
				gateway = InetAddress.getByName(ipAddress);
			}
			
		}
		
		return gateway;
		
	}
	
	

}
