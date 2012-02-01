package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;

import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.PacketGenerator;
import org.bitducks.spoofing.util.IpUtil;


public class ArpScanService extends Service {
	
	public ArpScanService() {
		
	}

	@Override
	public void run() {
		
		Server server = Server.getInstance();
		NetworkInterface device = server.getNetworkInterface();
		
		InetAddress start = IpUtil.network( device );
		InetAddress end = IpUtil.lastIpInNetwork( device );
		
		IpRange ipRange = new IpRange(start, end);
		
		LinkedList<InetAddress> addresses = new LinkedList<InetAddress>();
		for( InetAddress address: ipRange ) {
			addresses.add(address);
		}
		
		this.runScan(addresses);
		
	}
	
	public void runScan( Collection<InetAddress> addresses ) {
		
		Server server = Server.getInstance();
		NetworkInterface device = server.getNetworkInterface();
		
		PacketGenerator generator = new PacketGenerator(device);
		
		for( InetAddress address: addresses ) {
			
			System.out.println("sending request for " + address.toString() );
			ARPPacket arpRequest = generator.arpRequest(address);	
			server.sendPacket(arpRequest);
			
		}
		
	}

}
