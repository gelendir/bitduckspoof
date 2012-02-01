package org.bitducks.spoofing.customrules;

import java.net.InetAddress;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.rules.ARPRule;

public class ARPTimeoutService extends ARPRule {
	private InetAddress addr;
	
	@Override
	public boolean checkRule(Packet p) {
		if(super.checkRule(p)) {
			ARPPacket arp = (ARPPacket)p;
			if(arp.operation == 2) {
				byte[] bAddr = this.addr.getAddress();
				for(int i = 0; i < bAddr.length; ++i) {
					if(bAddr[i] != arp.sender_protoaddr[i]) {
						return false;
					}
				}
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public void setInetAddress(InetAddress addr) {
		this.addr = addr;
	}
}
