package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

/**
 * This rule filters DHCP packets that are sent
 * from a server to a client.
 * @author Frédérik Paradis
 */
public class DHCPServerRule extends UDPRule {

	/**
	 * Returns true if this packet is a DHCP packet
	 * sent by a client.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) &&
				( ((UDPPacket)p).src_port == 67 && 
				((UDPPacket)p).dst_port == 68 );
	}
}
