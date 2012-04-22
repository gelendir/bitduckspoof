package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;

/**
 * Rules that check if the Dns packet is a query.
 * @author Simon Perreault
 *
 */
public class DNSQueryrule extends DNSRule {

	/**
	 * Return true if the Dns packet is a query, otherwise false.
	 */
	@Override
	public boolean checkRule(Packet p) {
		
		return (super.checkRule(p) &&
				(p.data[2] & 0x80) == (byte) 0x00); // First bit == 0
	}

}
