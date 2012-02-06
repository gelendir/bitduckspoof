package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

public class DHCPRule extends UDPRule {

	private DHCPClientRule client = new DHCPClientRule();
	private DHCPServerRule server = new DHCPServerRule();
	
	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) && 
				(this.client.checkRule(p) || 
				this.server.checkRule(p));
	}

}
