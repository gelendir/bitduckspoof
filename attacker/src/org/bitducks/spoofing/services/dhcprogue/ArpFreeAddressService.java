package org.bitducks.spoofing.services.dhcprogue;

import java.net.InetAddress;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.ARPTimeoutRule;
import org.bitducks.spoofing.packet.PacketFactory;

public class ArpFreeAddressService extends Service {

	private ARPTimeoutRule rule = new ARPTimeoutRule();

	public ArpFreeAddressService() {
		this.getPolicy().addRule(this.rule);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public boolean sendARP(InetAddress addr, int timeout) {
		
		//captor.setFilter("arp[6:2] == 2 && arp src " + addr.getHostAddress(), false);
		this.rule.setInetAddress(addr);
		
		//We delete all old arp packet in the queue
		while(this.getNextNonBlockingPacket() != null);

		ARPPacket arp = PacketFactory.arpRequest(Server.getInstance().getNetworkInterface().mac_address,
				Server.getInstance().getNetworkInterface().addresses[0].address, 
				addr);

		Server.getInstance().sendPacket(arp);
		Packet p = this.getNextPacket(timeout);
		
		//We set the address of the rule to null to do not get other packet.
		this.rule.setInetAddress(null);
		
		if(p != null) {
			return true;
		}
		
		return false;
	}
}
