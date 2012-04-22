package org.bitducks.spoofing.customrules;

import java.util.Arrays;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

/**
 * This rule filters to receive only IP packets
 * for a specified MAC address
 * @author Louis-Étienne Dorval
 */
public class IpAndMacFilterRule extends Rule {
	
	/**
	 * The MAC address to use with the filter
	 */
	private byte[] macToFilter;

	/**
	 * Constructor
	 * @param macToFilter
	 */
	public IpAndMacFilterRule(byte[] macToFilter) {
		this.macToFilter = macToFilter;
	}

	/**
	 * Check if the packet fit in the rule
	 * @return True if the packet is for the right MAC address.
	 * False otherwise.
	 */
	@Override
	public boolean checkRule(Packet p) {
		if(p instanceof IPPacket) {
			if ( Arrays.equals( ((EthernetPacket)p.datalink).dst_mac, macToFilter) ) {
					System.out.println("Check rule true -- > Packet to redirect received");
					return true;
			}
		}
		return false;
	}
}
