package org.bitducks.spoofing.core.rules;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

/**
 * This rule filters ARP Response Packets. ARP responses
 * after sending an ARP request to retrieve an IP's MAC address
 * on the network.
 * 
 * @author Louis-Étienne Dorval
 */
public class ARPResponseRule extends Rule {

	/**
	 * Returns true if the packet is an ARP response.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return  p instanceof ARPPacket && 
				((ARPPacket)p).operation == ARPPacket.ARP_REPLY; 
	}

}
