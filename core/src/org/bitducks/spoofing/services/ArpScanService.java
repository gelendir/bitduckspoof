package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.Collection;

import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.PacketGenerator;
import org.bitducks.spoofing.util.IpRange;
import org.bitducks.spoofing.util.IpUtil;

/**
 * Sends ARP Requests for every IP address on a network.
 * Can be used for scanning a network and discovering what devices
 * are connected.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class ArpScanService extends Service {
	
	/**
	 * Constructor. Creates a new ArpScanService.
	 */
	public ArpScanService() {
		
	}

	@Override
	public void run() {
		
	}
	
	/**
	 * Sends ARP Requests for every IP address in a list.
	 * 
	 * @param addresses the addresses to send ARP Requests to.
	 */
	public void runScan( Collection<InetAddress> addresses ) {
		
		this.logger.info("Arp scan started");

		Server server = Server.getInstance();
		NetworkInterface device = server.getInfo().getDevice();
		
		PacketGenerator generator = new PacketGenerator(device);
		
		for( InetAddress address: addresses ) {
			
			this.logger.info("sending request for " + address.toString() );
			ARPPacket arpRequest = generator.arpRequest(address);	
			server.sendPacket(arpRequest);
			
		}
		
	}
	
	/**
	 * Sends ARP Requests for every IP address in the network.
	 */
	public void runNetworkScan() {
		
		this.logger.info("Arp network scan started");
		
		Server server = Server.getInstance();
		NetworkInterface device = server.getInfo().getDevice();
		
		PacketGenerator generator = new PacketGenerator(device);
		
		InetAddress start = IpUtil.network( device );
		InetAddress end = IpUtil.lastIpInNetwork2( server.getInfo().getDeviceAddress() );
		
		IpRange ipRange = new IpRange(start, end);
		
		for( InetAddress address: ipRange ) {
			this.logger.info("sending request for " + address.toString() );
			ARPPacket arpRequest = generator.arpRequest(address);	
			server.sendPacket(arpRequest);
		}
			
	}

}
