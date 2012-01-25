package org.bitducks.spoofing.customrules;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

public class DestinationIpRule extends Rule {

	private InetAddress toFilter;
	
	public DestinationIpRule(String ipToFilter) {
		try {
			toFilter = InetAddress.getByName(ipToFilter);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean checkRule(Packet p) {
		if(p instanceof IPPacket && ((IPPacket)p).src_ip == toFilter) {
			return true;
		}
		return false;
	}

}
