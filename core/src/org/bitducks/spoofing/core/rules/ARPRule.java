package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Rule;

public class ARPRule extends Rule {

	@Override
	public boolean checkRule(Packet p) {
		return p instanceof ARPPacket;
	}

}
