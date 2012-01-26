package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

public class DNSRule extends UDPRule {

	@Override
	public boolean checkRule(Packet p) {
		return super.checkRule(p) && 
				( ((UDPPacket)p).src_port == 53 || 
				((UDPPacket)p).dst_port == 53 );
	}

}