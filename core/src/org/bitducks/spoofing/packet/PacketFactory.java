package org.bitducks.spoofing.packet;
import java.net.InetAddress;

import org.bitducks.spoofing.util.Constants;

import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.UDPPacket;

/**
 * Utility class for helping in building various types of packet used throughout
 * the network services.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class PacketFactory {
	
	/**
	 * Builds a raw ethernet packet to attach to all outgoing packets.
	 * 
	 * @param macSource packet's source MAC
	 * @param macDestination packet's destination MAC
	 * @return Ethernet header packet
	 */
	static public EthernetPacket ethernet( byte[] macSource, byte[] macDestination ) {
		
		EthernetPacket ether = new EthernetPacket();
		ether.frametype = EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac = macSource;
		ether.dst_mac = macDestination;
		
		return ether;
		
	}
	
	/**
	 * Builds an ARP Request packet for finding out a target's MAC Address
	 * 
	 * @param macSource sender's MAC Address
	 * @param ipSource sender's IP Address
	 * @param ipTarget IP queried for a MAC Address
	 * @return the ARP Request packet
	 */
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
	
	/**
	 * Builds a DNS packet for querying a DNS server
	 * 
	 * @param queryPacket the raw UDP packet to wrap around
	 * @param transactionId a unique transaciton ID
	 * @param questions raw DNS questions to send to the server
	 * @param query raw DNS query
	 * @param ipFalseDNS are we using a false IP ?
	 * @return the DNS packet
	 */
	static public DNSPacket dnsRequest( UDPPacket queryPacket, 
			byte[] transactionId, 
			byte[] questions, 
			byte[] query, 
			InetAddress ipFalseDNS ) {
		
		DNSPacket dnsPacket = new DNSPacket(queryPacket.src_port, queryPacket.dst_port);
		
		dnsPacket.buildIpDNSPacket(queryPacket.src_ip, queryPacket.dst_ip, dnsPacket.ident);
		
		//create an Ethernet packet (frame)
		EthernetPacket ether = new EthernetPacket();
		//set frame type as IP
		ether.frametype = EthernetPacket.ETHERTYPE_IP;
		//set source and destination MAC addresses
		//ether.src_mac = Server.getInstance().getInfo().getMacAddress();
		//ether.dst_mac =  ((EthernetPacket)queryPacket.datalink).src_mac;
		ether.src_mac = ((EthernetPacket)queryPacket.datalink).src_mac;
		ether.dst_mac =  ((EthernetPacket)queryPacket.datalink).dst_mac;
		
		
		
		//set the datalink frame of the packet p as ether
		dnsPacket.datalink = ether;
		
		dnsPacket.buildDataDNSPacket(transactionId, questions, query, ipFalseDNS);		
		
		return dnsPacket;
	}

	/**
	 * Builds an ARP reply packet.
	 * 
	 * @param macSource sender's MAC Address
	 * @param source sender's IP Address
	 * @param macVictim the MAC of the victim
	 * @param victim the IP of the victim
	 * @return the ARP Request packet
	 */
	public static ARPPacket arpReply(byte[] macSource, InetAddress source, byte[] macVictim, InetAddress victim) {
		
		ARPPacket reply = new ARPPacket();
		
		reply.hlen = (short)Constants.MAC_LEN;
		reply.plen = (short)Constants.IPV4_LEN;
		
		reply.datalink = PacketFactory.ethernet( macSource, macVictim );
		
		reply.hardtype = ARPPacket.HARDTYPE_ETHER;
		reply.prototype = ARPPacket.PROTOTYPE_IP;
		reply.operation = ARPPacket.ARP_REPLY;
		
		reply.sender_hardaddr = macSource;
		reply.sender_protoaddr = source.getAddress();
		reply.target_hardaddr = macVictim;
		reply.target_protoaddr = victim.getAddress();
		
		return reply;
	}
}
