package org.bitducks.spoofing.customrules;

import java.net.InetAddress;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.rules.ARPRule;

public class ARPTimeoutRule extends ARPRule {
	private InetAddress addr;
	
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
	
	public void setInetAddress(InetAddress addr) {
		this.addr = addr;
	}
}
