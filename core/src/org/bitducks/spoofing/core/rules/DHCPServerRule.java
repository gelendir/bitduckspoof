package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

public class DHCPServerRule extends UDPRule {

	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) &&
				( ((UDPPacket)p).src_port == 67 && 
				((UDPPacket)p).dst_port == 68 );
	}
}
