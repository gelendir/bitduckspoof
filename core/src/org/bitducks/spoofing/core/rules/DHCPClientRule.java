package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

/**
 * 
 * This rule filters DHCP packets that are sent
 * by a client to a server.
 * 
 * @author Frédérik Paradis
 *
 */
public class DHCPClientRule extends UDPRule {

	/**
	 * Returns true if this packet is a DHCP packet
	 * sent by a client.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) && 
				( ((UDPPacket)p).src_port == 68 &&
				((UDPPacket)p).dst_port == 67 );
	}
}
