package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

public class DNSRule extends Rule {

	@Override
	public boolean checkRule(Packet p) {
		return (p instanceof UDPPacket) && 
				( ((UDPPacket)p).src_port == 53 || 
				((UDPPacket)p).dst_port == 53 );
	}

}
