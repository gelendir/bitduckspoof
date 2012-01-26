package org.bitducks.spoofing.scan;

import java.util.HashMap;
import java.util.Set;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPResponseRule;
import org.bitducks.spoofing.core.rules.ARPRule;
import org.bitducks.spoofing.event.ARPEvent;
import org.bitducks.spoofing.event.EventReciever;

public class ArpService extends Service {
	
	ArpCache cache;
	
	HashMap<ARPEvent, Set<EventReciever>> eventRegistry;
	
	public ArpService() {
		
		this.cache = new ArpCache();
		
		Rule arpRule = new ARPResponseRule();
		this.getPolicy().addRule(arpRule);
		
	}
	
	public void run() {
		
		boolean run = true;
		System.out.println("ARP service started");
		
		while( run ) {
			
			ARPPacket packet = (ARPPacket)this.getNextPacket();
			
			byte[] targetMac = packet.target_hardaddr;
			byte[] targetIp = packet.target_protoaddr;
			this.cache.add(targetIp, targetMac);
			
		}
		
	}
	
	public ArpCache getCache() {
		return this.cache;
	}
	
}
