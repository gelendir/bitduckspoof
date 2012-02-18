package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Rule;

/**
 * This rule check if the packet is an ARP packet.
 * @author Frédérik Paradis
 */
public class ARPRule extends Rule {

	/**
	 * This method check if the packet is an ARP packet.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return p instanceof ARPPacket;
	}

}
