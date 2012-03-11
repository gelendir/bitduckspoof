package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.DHCPServerRule;
import org.bitducks.spoofing.util.Constants;
import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPPacket;

/**
 * This class is used to detect rogue DHCP servers by sending
 * a DHCP discover and comparing the offers with the available
 * trusted DHCP servers.
 * @author Frédérik Paradis
 */
public class RogueDHCPDetectionService extends Service {

	/**
	 * The information about our network interface.
	 */
	private InterfaceInfo info = Server.getInstance().getInfo();
	
	/**
	 * All available DHCP servers who have replied to a DHCP discovery.
	 */
	private ArrayList<InetAddress> availableDHCPServer =  new ArrayList<InetAddress>();
	
	/**
	 * The DHCP servers who have replied to the DHCP discovery and are not
	 * part of the trusted DHCP servers.
	 */
	private Collection<InetAddress> illegalServer = null;
	
	/**
	 * The available trusted DHCP servers.
	 */
	private Collection<InetAddress> supposedServer;

	/**
	 * The constructor. Initializes the service with a collection
	 * of trusted DHCP servers.
	 * @param supposedServer Trusted DHCP servers 
	 */
	public RogueDHCPDetectionService(Collection<InetAddress> supposedServer) {
		//TODO: greg: supposedServer sont les serveurs DHCP valides
		super();
		this.getPolicy().addRule(new DHCPServerRule());
		this.supposedServer = supposedServer;
	}

	/**
	 * This method sends a DHCP discover and verifies if all the offers match with
	 * the trusted DHCP servers given.
	 */
	@Override
	public void run() {
		this.logger.info("Discovery in progress...");
		this.doDiscover();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UDPPacket p = (UDPPacket)this.getNextNonBlockingPacket();
		while(p != null) {
			this.availableDHCPServer.add(p.src_ip);
			p = (UDPPacket)this.getNextNonBlockingPacket();
		}

		this.illegalServer = new HashSet<InetAddress>(this.availableDHCPServer);
		this.illegalServer.removeAll(this.supposedServer);
		
		if(!this.illegalServer.isEmpty()) {
			this.logger.warn("There is " + this.illegalServer.size() + " illegal DHCP server on the network: " +
					this.illegalServer.toString());
		} else {
			this.logger.info("There is no illegal DHCP Server on the network.");
		}
	}

	/**
	 * This method returns all the DHCP server who have replied
	 * to a DHCP discover.
	 * @return Return all DHCP server who have replied.
	 */
	public Collection<InetAddress> getAvailableDHCPServer() {
		return this.availableDHCPServer;
	}

	/**
	 * This method returns a collection of a IP addresses
	 * who are rogue DHCP servers.
	 * @return Return a collection of all IP address
	 * who are rogue DHCP servers.
	 */
	public Collection<InetAddress> getIllegalServer() {
		return this.illegalServer;
	}

	/**
	 * This method sends a DHCP discover packet on the network.
	 */
	private void doDiscover() {
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
		discover.setChaddr(this.info.getMacAddress());
		discover.setXid( (new Random()).nextInt() );

		udpDiscover.data = discover.serialize();

		Server.getInstance().sendPacket(udpDiscover);
	}

	/**
	 * This method sets the IP header and the Ethernet header in
	 * the UDPPacket for a DHCP discover or request. 
	 * @param packet The UDPPacket to set the header.
	 * @param source The source IP address
	 * @param destination The destination IP address.
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

		ether.src_mac = this.info.getMacAddress();
		ether.dst_mac =  Constants.BROADCAST;

		//set the datalink frame of the packet p as ether
		packet.datalink = ether;
	}
}
