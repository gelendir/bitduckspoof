package org.bitducks.spoofing.core.rules;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

public class ARPResponseRule extends Rule {

	@Override
	public boolean checkRule(Packet p) {
		return  p instanceof ARPPacket && 
				((ARPPacket)p).operation == ARPPacket.ARP_REPLY; 
	}

}
