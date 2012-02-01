package org.bitducks.spoofing.customrules;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

public class IpFilterRule extends Rule {

	private InetAddress toFilter;
	
	public IpFilterRule(InetAddress ipToFilter) {
		toFilter = ipToFilter;
	}
	
	@Override
	public boolean checkRule(Packet p) {
		//TODO: Change the rule to redirect packet with my MAC but not my real IP
		if( ((IPPacket)p).dst_ip != toFilter) {
			return true;
		}
		return false;
	}

}
