package org.bitducks.spoofing.scan;

import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPRule;

public class ArpService extends Service {
	
	ArpCache cache;
	
	public ArpService() {
		
		this.cache = new ArpCache();
		
		Rule arpRule = new ARPRule();
		this.getPolicy().addRule(arpRule);
		
	}
	
	public void run() {
		
		boolean run = true;
		
		while( run ) {
			
			System.out.println("ARP service started");
			
			Packet packet = this.getNextPacket();
			System.out.println(packet);
			
		}
		
	}

}
