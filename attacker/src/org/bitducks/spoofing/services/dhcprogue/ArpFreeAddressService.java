package org.bitducks.spoofing.services.dhcprogue;

import java.net.InetAddress;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.ARPTimeoutService;
import org.bitducks.spoofing.packet.PacketFactory;

public class ArpFreeAddressService extends Service {

	private ARPTimeoutService rule = new ARPTimeoutService();

	public ArpFreeAddressService() {
		this.getPolicy().addRule(this.rule);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public boolean sendARP(InetAddress addr, int timeout) {

		//We delete all old arp packet in the queue
		while(this.getNextNonBlockingPacket() != null);
		
		//captor.setFilter("arp[6:2] == 2 && arp src " + addr.getHostAddress(), false);
		this.rule.setInetAddress(addr);

		ARPPacket arp = PacketFactory.arpRequest(Server.getInstance().getNetworkInterface().mac_address,
				Server.getInstance().getNetworkInterface().addresses[0].address, 
				addr);

		Server.getInstance().sendPacket(arp);
		Packet p = this.getNextPacket(timeout);
		if(p != null) {
			return true;
		}

		return false;
	}
}
