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
import org.bitducks.spoofing.event.Registry;

public class ArpRecieveService extends Service {
	
	ArpCache cache;
	
	Registry registry;
		
	public ArpRecieveService() {
		
		this.cache = new ArpCache();
		this.registry = new Registry();
		
		Rule arpRule = new ARPResponseRule();
		this.getPolicy().addRule(arpRule);
		
	}
	
	public void run() {
		
		boolean run = true;
		System.out.println("ARP recieving service started");
		
		while( run ) {
			
			ARPPacket packet = (ARPPacket)this.getNextPacket();
			
			if( packet == null ) {
				run = false;
				return;
			}
			
			byte[] senderMac = packet.sender_hardaddr;
			byte[] senderIp = packet.sender_protoaddr;
			this.cache.add(senderMac, senderIp);
			System.out.println(this.cache);
	
		}
		
	}
	
	public ArpCache getCache() {
		return this.cache;
	}
	
	public void registerForEvent( EventReciever reciever, ARPEvent event ) {
		
		this.registry.addReciever( event, reciever );
		
	}
	
}
