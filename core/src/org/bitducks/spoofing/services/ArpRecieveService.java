package org.bitducks.spoofing.services;

import java.util.HashMap;
import java.util.Set;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;
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
		
		while( !this.isCloseRequested() ) {
			
			ARPPacket packet = (ARPPacket)this.getNextBlockingPacket();
			
			if( packet == null ) {
				return;
			}
			
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
