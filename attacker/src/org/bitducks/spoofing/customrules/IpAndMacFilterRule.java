package org.bitducks.spoofing.customrules;

import java.net.InetAddress;
import java.util.Arrays;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

public class IpAndMacFilterRule extends Rule {

	private InetAddress ipToFilter;
	private byte[] macToFilter;

	public IpAndMacFilterRule(InetAddress ipToFilter, byte[] macToFilter) {
		this.ipToFilter = ipToFilter;
		this.macToFilter = macToFilter;
	}

	@Override
	public boolean checkRule(Packet p) {
		if(p instanceof IPPacket) {
			if ( Arrays.equals( ((EthernetPacket)p.datalink).dst_mac, macToFilter) ) {
				if( ((IPPacket)p).dst_ip.equals(ipToFilter)) {
					System.out.println("Check rule true -- > Packet to redirect received");
					return true;
				}
			}

		}
		return false;
	}

}
