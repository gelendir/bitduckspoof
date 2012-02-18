package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPRule;

/**
 * This class is an active ARP protection service. The 'active' qualify 
 * means that when the service receive a ARP reply, it send a request to
 * verify if the reply was good or not. 
 * @author Frédérik Paradis
 */
public class ActiveARPProtectionService extends Service {

	/**
	 * A blacklist of IP address who are currently being scanned.
	 */
	private Set<InetAddress> blacklist = new HashSet<InetAddress>();

	/**
	 * The constructor initialize the ActiveARPProtectionService.
	 */
	public ActiveARPProtectionService() {
		this.getPolicy().addRule(new ARPRule());
	}

	/**
	 * This method wait for ARP reply and send ARP request to
	 * verify if the reply was legitimate.
	 */
	@Override
	public void run() {
		while(!this.isCloseRequested()) {
			Packet p = this.getNextBlockingPacket();
			if(p!= null && !p.equals(Packet.EOF)) {
				ARPPacket arp = (ARPPacket)p;
				if(arp.operation == 2) {
					if(arp.getSenderProtocolAddress() instanceof InetAddress &&
							!this.blacklist.contains(arp.getSenderProtocolAddress())) {
						this.blacklist.add((InetAddress) arp.getSenderProtocolAddress());
						ARPQueryService worker = new ARPQueryService((InetAddress) arp.getSenderProtocolAddress(), arp.sender_hardaddr, this);
						Server.getInstance().addService(worker);
					}
				}
			}
		}

	}

	/**
	 * This method is used to indicate that an IP address is spoofed.
	 * @param addr The IP address
	 */
	/* package visibility */ void setBadAddress(InetAddress addr) {
		this.logger.warn("There is a possibility of spoof of the address " + addr);
		this.blacklist.remove(addr);
	}

	/**
	 * This method is used to indicate that an IP address is not spoofed.
	 * @param addr The IP address
	 */
	/* package visibility */ void setGoodAddress(InetAddress addr) {
		this.blacklist.remove(addr);
		this.logger.warn("There is NO possibility of spoof of the address " + addr);
	}
}
