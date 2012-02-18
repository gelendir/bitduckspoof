package org.bitducks.spoofing.customrules;

import java.net.InetAddress;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.rules.ARPRule;

/**
 * This class is a rule to receive the reply from a known host.
 * @author Frédérik Paradis
 */
public class ARPReplyReceiverRule extends ARPRule {
	
	/**
	 * The IP address to filter.
	 */
	private InetAddress addr;
	
	/**
	 * This method verify if the packet passed in
	 * parameter is an ARP reply and if the sender address
	 * is from the IP address set with {@link ARPReplyReceiverRule#setInetAddress(InetAddress)}
	 */
	@Override
	public boolean checkRule(Packet p) {
		if(super.checkRule(p) && this.addr != null) {
			ARPPacket arp = (ARPPacket)p;
			if(arp.operation == 2) {
				return this.addr.equals(arp.getSenderProtocolAddress());
			}
		}
		return false;
	}
	
	/**
	 * This method modify the IP address to filter.
	 * @param addr The IP address to filter.
	 */
	public void setInetAddress(InetAddress addr) {
		this.addr = addr;
	}
}
