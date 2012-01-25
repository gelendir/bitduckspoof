package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

public class UDPRule extends Rule {

	@Override
	public boolean checkRule(Packet p) {
		return p instanceof UDPPacket;
	}

}
