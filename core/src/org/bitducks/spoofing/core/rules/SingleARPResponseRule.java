package org.bitducks.spoofing.core.rules;

import java.net.InetAddress;
import java.util.Arrays;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

/**
 * This rule filters to receive an ARP response
 * from a single target specify by the IP address
 * @author Louis-Etienne Dorval
 */
public class SingleARPResponseRule extends ARPResponseRule {
	
	/**
	 * The IP address of the victim 
	 */
	private byte[] address;

	/**
	 * Constructor
	 * @param address
	 */
	public SingleARPResponseRule( InetAddress address ) {
		this.address = address.getAddress();
	}
	
	/**
	 * Returns true if the packet is an ARP response
	 * and is from the right IP address
	 */
	@Override
	public boolean checkRule(Packet p) {
		if( super.checkRule(p) ) {
			ARPPacket packet = ((ARPPacket)p);
			return Arrays.equals( packet.sender_protoaddr, this.address ); 
		}
		
		return false;
	}
	
	

}
