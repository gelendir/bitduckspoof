package org.bitducks.spoofing.services;

import java.net.InetAddress;

import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.SingleARPResponseRule;
import org.bitducks.spoofing.exception.UnexpectedErrorException;
import org.bitducks.spoofing.packet.PacketGenerator;

/**
 * Utility service to find the MAC address of a device on the network. 
 * Reuses the Service architecture to check if the MAC has already been 
 * cached by the ARP Service.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class MacFindService extends Service {
	
	/**
	 * IP Address to search MAC for
	 */
	private InetAddress ipAddress = null;
	
	/**
	 * MAC Address for the IP Address we are searching
	 */
	private byte[] macAddress = null;
	
	/**
	 * Constructor. Creates a new MacFindService.
	 * 
	 * @param ipAddress the IP Address for which we need to find the MAC
	 */
	public MacFindService( InetAddress ipAddress ) {
		
		this.ipAddress = ipAddress;
		
		this.getPolicy().addRule(
				new SingleARPResponseRule( this.ipAddress ) );
	}
	
	/**
	 * Main service loop. Send an ARP Request to find a MAC Address
	 * and wait for the response.
	 * 
	 */
	@Override
	public void run() {
		
		Server server = Server.getInstance();
		
		PacketGenerator generator = new PacketGenerator( Server.getInstance().getInfo().getDevice() );
		ARPPacket request = generator.arpRequest( this.ipAddress );
		
		server.sendPacket( request );
		
		ARPPacket response = (ARPPacket)this.getNextBlockingPacket();
		
		this.macAddress = response.sender_hardaddr;
		
		synchronized( this ) {
			this.notify();
		}
		
	}
	
	/**
	 * Send an ARP request to find out the IP's MAC Address.
	 * This method will block until an ARP Response has been received.
	 * 
	 * @return the MAC Address
	 */
	public byte[] getMacAddress() {
		
		synchronized( this ) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				throw new UnexpectedErrorException(e, "error waiting for gateway mac address");
			}
		}
		
		return this.macAddress;
	}

}
