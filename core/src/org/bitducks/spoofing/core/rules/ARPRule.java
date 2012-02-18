package org.bitducks.spoofing.core.rules;

import jpcap.packet.Packet;
import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Rule;

/**
 * 
 * This rule filters ARP packets. It does not
 * differentiate between an ARP request or ARP reply.
 * Please refer to the rest of the documentation for more
 * specialized ARP filtering rules.
 * 
 * @see ARPResponseRule
 * @see SingleARPResponseRule
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class ARPRule extends Rule {

	/**
	 * Returns true if the packet is an ARP packet.
	 */
	@Override
	public boolean checkRule(Packet p) {
		return p instanceof ARPPacket;
	}

}
