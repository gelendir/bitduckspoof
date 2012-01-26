package org.bitducks.spoofing.packet;
import java.net.InetAddress;

import org.bitducks.spoofing.util.Constants;

import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.UDPPacket;

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
	
	static public DNSPacket dnsRequest( UDPPacket queryPacket, 
			byte[] transactionId, 
			byte[] questions, 
			byte[] query, 
			InetAddress ipFalseDNS ) {
		
		DNSPacket dnsPacket = new DNSPacket(queryPacket.src_port, queryPacket.dst_port);
		
		dnsPacket.buildIpDNSPacket(queryPacket.src_ip, queryPacket.dst_ip);
		
		//create an Ethernet packet (frame)
		EthernetPacket ether = new EthernetPacket();
		//set frame type as IP
		ether.frametype = EthernetPacket.ETHERTYPE_IP;
		//set source and destination MAC addresses
		ether.src_mac = new byte[]{(byte)0x00,(byte)0x1c,(byte)0x10,(byte)0x41,(byte)0x92,(byte)0x0e};
		ether.dst_mac =  new byte[]{(byte)0x08,(byte)0x00,(byte)0x27,(byte)0x4c,(byte)0x0d,(byte)0x1c};
		
		//set the datalink frame of the packet p as ether
		dnsPacket.datalink = ether;
		
		dnsPacket.buildDataDNSPacket(transactionId, questions, query, ipFalseDNS);		
		
		return dnsPacket;
	}

}
