package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

public class DHCPRule extends UDPRule {

	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) && 
				(
						( ((UDPPacket)p).src_port == 67 && 
						((UDPPacket)p).dst_port == 68 ) || 
						( ((UDPPacket)p).src_port == 68 &&
						((UDPPacket)p).dst_port == 67 )
						);
	}

}
