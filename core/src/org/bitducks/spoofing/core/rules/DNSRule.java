package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

/**
 * This rule check if a packet is a DNS packet.
 * @author Frédérik Paradis
 */
public class DNSRule extends UDPRule {

	/**
	 * This method check if a packet is a DNS packet.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) && 
				( ((UDPPacket)p).src_port == 53 || 
				((UDPPacket)p).dst_port == 53 );
	}

}
