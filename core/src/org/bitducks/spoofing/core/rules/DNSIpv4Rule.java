package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;

/**
 * Check if the DNS Query is an Ipv4 query
 * @author Simon Perreault
 *
 */
public class DNSIpv4Rule extends DNSQueryrule {

	/**
	 * Return true if the dns query is an ipv4 query, otherwise false;
	 */
	@Override
	public boolean checkRule(Packet p) {
		return (super.checkRule(p) &&
				((p.data[p.data.length - 3] & (byte) 0x1C) == (byte)0x00)); // Ipv4 DNS Query
	}

}
