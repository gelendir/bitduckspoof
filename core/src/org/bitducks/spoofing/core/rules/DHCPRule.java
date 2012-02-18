package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

/**
 * This rule filters DHCP Packets on the network.
 * Both DHCP client and DHCP server packets are
 * intercepted by this rule.
 * 
 * @see DHCPClientRule
 * @see DHCPServerRule
 * 
 * @author Frédérik Paradis
 */
public class DHCPRule extends UDPRule {

	/**
	 * This client's rule.
	 */
	private DHCPClientRule client = new DHCPClientRule();
	
	/**
	 * This server's rule.
	 */
	private DHCPServerRule server = new DHCPServerRule();
	
	/**
	 * This method check if a packet is a DHCP packet.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) && 
				(this.client.checkRule(p) || 
				this.server.checkRule(p));
	}

}
