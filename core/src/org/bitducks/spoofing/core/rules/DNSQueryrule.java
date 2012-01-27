package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

public class DNSQueryrule extends DNSRule {

	@Override
	public boolean checkRule(Packet p) {
		
		/*System.out.println("Checking Rule " + (p instanceof UDPPacket &&
				(p.data[2] & 0xF0) == (byte) 0x00 ));
		System.out.println(p);*/
		
		return (super.checkRule(p) &&
				(p.data[2] & 0x80) == (byte) 0x00); // First bit == 0
	}

}
