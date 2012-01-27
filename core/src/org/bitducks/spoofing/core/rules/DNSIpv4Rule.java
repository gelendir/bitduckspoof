package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

public class DNSIpv4Rule extends DNSQueryrule {

	@Override
	public boolean checkRule(Packet p) {
		return (super.checkRule(p) &&
				((p.data[p.data.length - 3] & (byte) 0x1C) == (byte)0x00)); // Ipv4 DNS Query
	}

}
