package org.bitducks.spoofing.core.rules;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

/**
 * This rule check if the packet is an ARP reply.
 * @author Louis-Étienne Dorval
 */
public class ARPResponseRule extends Rule {

	/**
	 * This method check if the packet is an ARP reply.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return  p instanceof ARPPacket && 
				((ARPPacket)p).operation == ARPPacket.ARP_REPLY; 
	}

}
