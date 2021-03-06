package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.Arrays;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.ARPReplyReceiverRule;
import org.bitducks.spoofing.packet.PacketGenerator;

/**
 * This class is used by ActiveARPProtectionService class as a worker to 
 * verify if an ARP reply is legitimate or not. The main task of this class
 * is to send an ARP query, to get the reply and verify the mac address. 
 * @author Frédérik Paradis
 */
public class ARPQueryService extends Service {

	/**
	 * A rule to receive the reply from a known host.
	 */
	private ARPReplyReceiverRule rule = new ARPReplyReceiverRule();
	
	/**
	 * PacketGenerator to generate the ARP requests.
	 */
	private PacketGenerator arpGen = new PacketGenerator(Server.getInstance().getInfo().getDevice());
	
	/**
	 * The ActiveARPProtectionService to warn when the analysis of 
	 * the ARP replies is finished.
	 */
	private ActiveARPProtectionService callback;
	
	/**
	 * The IP address that we need to verify.
	 */
	private InetAddress next;
	
	/**
	 * The known MAC address.
	 */
	private byte[] mac;

	/**
	 * Constructor. initializes the ARPQueryService with the IP address and 
	 * the MAC address taken from an ARP reply and a callback instance. The callback
	 * represents the service that needs to be notified once the MAC address has been verified. 
	 * @param next The IP address taken from an ARP reply
	 * @param mac The MAC address taken from an ARP reply
	 * @param callback A callback instance
	 */
	public ARPQueryService(InetAddress next, byte[] mac, ActiveARPProtectionService callback) {
		this.rule.setInetAddress(next);
		this.getPolicy().addRule(this.rule);
		this.callback = callback;
		this.next = next;
		this.mac = mac;
	}

	/**
	 * Main task. The method sends an 
	 * ARP request to the given IP address in the constructor, 
	 * verifies if the reply matches with the MAC address also given
	 * in the constructor and calls the callback object once finished.
	 */
	@Override
	public void run() {
		try {
			//Sending the ARP request.
			Server.getInstance().sendPacket(this.arpGen.arpRequest(this.next));
			
			//Waiting for five second to receive all the reply
			Thread.sleep(5000);
			
			//We get all packet
			Packet p = this.getNextNonBlockingPacket();
			while(p != null && !p.equals(Packet.EOF)) {
				ARPPacket arp = (ARPPacket)p;
				
				//We compare the MAC address of the ARP reply.
				if(arp.getSenderProtocolAddress().equals(this.next) &&
						!Arrays.equals(this.mac, arp.sender_hardaddr)) {
					this.callback.setBadAddress(this.next);
					return;
				}

				p = this.getNextNonBlockingPacket();
			}

			this.callback.setGoodAddress(this.next);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Server.getInstance().removeService(this);
		}
	}
}

