package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Rule;

/**
 * This rule check if a packet is a UDP packet.
 * @author Frédérik Paradis
 */
public class UDPRule extends Rule {

	/**
	 * This method check if a packet is a UDP packet.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return p instanceof UDPPacket;
	}

}
