package org.bitducks.spoofing.services;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.IpAndMacFilterRule;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

public class RedirectMITM extends Service {

	private InterfaceInfo serverInfo;
	private HashMap<InetAddress, byte[]> ipToMac;
	private InetAddress gatewayIP;
	private byte[] gatewayMAC;

	public RedirectMITM() {
		serverInfo = Server.getInstance().getInfo();
		System.out.println(serverInfo.getAddress());
		this.getPolicy().addRule(new IpAndMacFilterRule(serverInfo.getAddress(), serverInfo.getMacAddress()));
		try {
			gatewayIP = GatewayFinder.find(serverInfo.getDevice());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		IPPacket victmIP = (IPPacket)p;
		EthernetPacket victimEthernet = (EthernetPacket)p.datalink;
		
		if (!isPacketResponse(p)) {				
			if (ipToMac.get(victmIP) == null || Arrays.equals(ipToMac.get(victmIP), victimEthernet.src_mac)) {
				ipToMac.put(victmIP.src_ip, victimEthernet.src_mac);
			}
			else {
				System.out.println("Invalid IP<-->MAC correspondence detected!");
				ipToMac.put(victmIP.src_ip, victimEthernet.src_mac);
			}
			victimEthernet.src_mac = serverInfo.getMacAddress();
			//TODO: victimEthernet.dst_mac = ADRESSE MAC DU GATEWAY
			p.datalink = victimEthernet;
			Server.getInstance().sendPacket(p);
		}
		else { 										//Response from the gateway
			if (ipToMac.get(victmIP) != null) {
				victimEthernet.src_mac = serverInfo.getMacAddress();
				victimEthernet.dst_mac = ipToMac.get(victmIP);
				p.datalink = victimEthernet;
				Server.getInstance().sendPacket(p);
			}
			else {
				System.out.println("No MAC correspondence found for the packet");
			}
		}

	}

	public boolean isPacketResponse(Packet p) {
		if ( Arrays.equals(gatewayMAC, ((EthernetPacket)p.datalink).src_mac) &&
			 ((IPPacket)p).src_ip == gatewayIP) {
			return true;
		}
		return false;
	}

}
