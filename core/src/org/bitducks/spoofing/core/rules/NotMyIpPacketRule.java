package org.bitducks.spoofing.core.rules;

import java.net.InetAddress;

import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

public class NotMyIpPacketRule extends Rule {

	InetAddress myIp;
	
	public NotMyIpPacketRule(InetAddress myIp) {
		this.myIp = myIp;
	}
	@Override
	public boolean checkRule(Packet p) {
		if (p instanceof IPPacket)
		{
			return !((IPPacket)p).src_ip.equals(myIp);
		}
		return false;
	}

}
