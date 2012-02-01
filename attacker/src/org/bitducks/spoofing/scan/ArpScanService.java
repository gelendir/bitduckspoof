package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
		NetworkInterfaceAddress deviceAddress = device.addresses[0];
		
		InetAddress start = IpUtil.network(deviceAddress);
		InetAddress end = IpUtil.lastIpInNetwork(deviceAddress);
		
		IpRange ipRange = new IpRange(start, end);
		
		PacketGenerator generator = new PacketGenerator(device);
		
		for( InetAddress address: ipRange ) {
			
			System.out.println("sending request for " + address.toString() );
			ARPPacket arpRequest = generator.arpRequest(address);	
			server.sendPacket(arpRequest);
			
		}
		
	}

}
