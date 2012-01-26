package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

public class DNSQueryrule extends Rule {

	@Override
	public boolean checkRule(Packet p) {
		
		System.out.println("Checking Rule " + (p instanceof UDPPacket &&
				(p.data[2] & 0xF0) == (byte) 0xF0 ));
		
		return (p instanceof UDPPacket &&
				(p.data[2] & 0xF0) == (byte) 0xF0 );
	}

}
