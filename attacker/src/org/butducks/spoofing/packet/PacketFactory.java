package org.butducks.spoofing.packet;
import java.net.InetAddress;

import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class PacketFactory {
	
	static public EthernetPacket ethernet( byte[] macSource, byte[] macDestination ) {
		
		EthernetPacket ether = new EthernetPacket();
		ether.frametype = EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac = macSource;
		ether.dst_mac = macDestination;
		
		return ether;
		
	}
	
	static public ARPPacket arpRequest( byte[] macSource, InetAddress ipSource, InetAddress ipTarget ) {
		
		ARPPacket request = new ARPPacket();
		
		request.hlen = (short)Constants.MAC_LEN;
		request.plen = (short)Constants.IPV4_LEN;
		
		request.datalink = PacketFactory.ethernet( macSource, Constants.BROADCAST );
		
		request.hardtype = ARPPacket.HARDTYPE_ETHER;
		request.prototype = ARPPacket.PROTOTYPE_IP;
		request.operation = ARPPacket.ARP_REQUEST;
		
		request.sender_hardaddr = macSource;
		request.sender_protoaddr = ipSource.getAddress();
		request.target_hardaddr = Constants.BROADCAST;
		request.target_protoaddr = ipTarget.getAddress();
		
		return request;
		
	}

}
