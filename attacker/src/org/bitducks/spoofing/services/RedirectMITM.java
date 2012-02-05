package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.HashMap;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.IpAndMacFilterRule;

public class RedirectMITM extends Service {

	private InterfaceInfo infoInterface;
	private HashMap<InetAddress, byte[]> ipToMac;

	public RedirectMITM() {
		infoInterface = Server.getInstance().getInfo();
		System.out.println(infoInterface.getAddress());
		this.getPolicy().addRule(new IpAndMacFilterRule(infoInterface.getAddress(), infoInterface.getMacAddress()));
	}
	
	@Override
	public void run() {
		System.out.println("Redirect MITM started");
		while(! this.isCloseRequested()){
			Packet toTransfer = this.getNextBlockingPacket();
			redirectPacket(toTransfer);
		}
	}

	private void redirectPacket(Packet p) {
		//Get the current IP and Ethernet packet
		IPPacket genuineIP = (IPPacket)p;
		EthernetPacket genuineEthernet = (EthernetPacket)p.datalink;
		
		//First we keep the correspondence IP<-->MAC
		if (ipToMac.get(genuineIP) == null || ipToMac.get(genuineIP) != genuineEthernet.src_mac) {
			ipToMac.put(genuineIP.src_ip, genuineEthernet.src_mac);
		}
		//If it's already
		
		
	}
	
	

}
