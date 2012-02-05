package org.bitducks.spoofing.services.dhcprogue;

import java.net.InetAddress;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.ARPTimeoutRule;
import org.bitducks.spoofing.packet.PacketFactory;

/**
 * This service aims to send ARP packet and
 * receive response with a timeout.
 * @author Frédérik Paradis
 */
public class ArpFreeAddressService extends Service {

	/**
	 * The rule use to specify the ARP source IP to
	 * receive.
	 */
	private ARPTimeoutRule rule = new ARPTimeoutRule();

	/**
	 * This constructor initialize the service. 
	 */
	public ArpFreeAddressService() {
		this.getPolicy().addRule(this.rule);
	}
	
	/**
	 * This method does actually nothing.
	 */
	@Override
	public void run() {
		//This method has nothing to do.
	}

	/**
	 * This method send an ARP Packet and receive the answer
	 * if available.
	 * @param addr The IP address to send ARP
	 * @param timeout The receive timeout
	 * @return Return true if the IP answer to the request; in another
	 * case, false.
	 */
	public boolean sendARP(InetAddress addr, int timeout) {
		
		//captor.setFilter("arp[6:2] == 2 && arp src " + addr.getHostAddress(), false);
		this.rule.setInetAddress(addr);
		
		//We delete all old arp packet in the queue
		while(this.getNextNonBlockingPacket() != null);

		ARPPacket arp = PacketFactory.arpRequest(Server.getInstance().getInfo().getMacAddress(),
				Server.getInstance().getInfo().getAddress(), 
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
