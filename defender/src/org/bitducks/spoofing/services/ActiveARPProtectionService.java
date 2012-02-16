package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPRule;

public class ActiveARPProtectionService extends Service {

	private Set<InetAddress> blacklist = new HashSet<InetAddress>();

	public ActiveARPProtectionService() {
		this.getPolicy().addRule(new ARPRule());
	}

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

	public void setBadAddress(InetAddress addr) {
		this.logger.warn("There is a possibility of spoof of the address " + addr);
		this.blacklist.remove(addr);
	}

	public void setGoodAddress(InetAddress addr) {
		this.blacklist.remove(addr);
		this.logger.warn("There is NO possibility of spoof of the address " + addr);
	}
}
