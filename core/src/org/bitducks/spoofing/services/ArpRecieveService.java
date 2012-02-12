package org.bitducks.spoofing.services;

import java.util.HashMap;
import java.util.Set;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Rule;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPResponseRule;
import org.bitducks.spoofing.core.rules.ARPRule;
import org.bitducks.spoofing.services.arp.ArpCache;

public class ArpRecieveService extends Service {
	
	ArpCache cache;
	
	public ArpRecieveService() {
		
		this.cache = new ArpCache();
		
		Rule arpRule = new ARPResponseRule();
		this.getPolicy().addRule(arpRule);
		
	}
	
	public void run() {
		
		this.logger.info("ARP recieving service started");
		
		InterfaceInfo info = Server.getInstance().getInfo();
		this.cache.add( info.getAddress().getAddress(), info.getMacAddress() );
		
		while( !this.isCloseRequested() ) {
			Packet p = this.getNextBlockingPacket();
			if( p == null || p.equals(Packet.EOF) ) {
				return;
			}
			
			ARPPacket packet = (ARPPacket)p;
			
			byte[] senderMac = packet.sender_hardaddr;
			byte[] senderIp = packet.sender_protoaddr;
			this.cache.add(senderIp, senderMac);
			
			this.logger.debug(this.cache.toString());
		}
		
	}

	public ArpCache getCache() {
		return this.cache;
	}
	
}
