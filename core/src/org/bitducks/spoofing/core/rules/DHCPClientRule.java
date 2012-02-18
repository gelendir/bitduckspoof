package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

/**
 * This rule check if a packet is a DHCP packet
 * comes from a DHCP client.
 * @author Frédérik Paradis
 */
public class DHCPClientRule extends UDPRule {

	/**
	 * This method check if a packet is a DHCP packet
	 * comes from a DHCP client.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) && 
				( ((UDPPacket)p).src_port == 68 &&
				((UDPPacket)p).dst_port == 67 );
	}
}
