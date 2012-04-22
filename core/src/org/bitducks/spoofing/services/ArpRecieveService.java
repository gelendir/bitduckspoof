package org.bitducks.spoofing.services;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Rule;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPResponseRule;
import org.bitducks.spoofing.services.arp.ArpCache;

/**
 * Service for listening on a network connection and
 * accumulating ARP Replies. IP and MAC addresses received
 * through the replies are added to a ARP cache for later
 * retreival.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class ArpRecieveService extends Service {
	
	/**
	 * The internal ARP cache for storing addresses
	 */
	ArpCache cache;
	
	/**
	 * Constructor. Creates a new ArpReceiveService
	 */
	public ArpRecieveService() {
		
		this.cache = new ArpCache();
		
		Rule arpRule = new ARPResponseRule();
		this.getPolicy().addRule(arpRule);
		
	}
	
	/**
	 * Main service loop. Listens for ARP Replies and
	 * caches the IP and MAC addresses.
	 * 
	 */
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

	/**
	 * Returns the ARP Cache containing all the IP and MAC
	 * addresses received.
	 * 
	 * @return The ARP Cache.
	 */
	public ArpCache getCache() {
		return this.cache;
	}
	
}
