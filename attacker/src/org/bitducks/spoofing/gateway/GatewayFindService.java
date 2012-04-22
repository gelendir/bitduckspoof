package org.bitducks.spoofing.gateway;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.SingleARPResponseRule;
import org.bitducks.spoofing.exception.UnexpectedErrorException;
import org.bitducks.spoofing.packet.PacketGenerator;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

/**
 * Utility service to find the MAC address of the gateway being used
 * by the current Network device. Reuses the Service architecture
 * to check if the MAC has already been cached by the ARP Service.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class GatewayFindService extends Service {
	
	private InetAddress ipAddress = null;
	private byte[] macAddress = null;
	
	/**
	 * Constructor. Creates a new service
	 */
	public GatewayFindService() {
		
		this.ipAddress = this.findGatewayAddress();
		
		this.getPolicy().addRule(
				new SingleARPResponseRule( this.ipAddress ) );
	}

	/**
	 * Find the network device's gateway IP Address using the
	 * OS network utilities.
	 * 
	 * @return the network's gateway IP Address
	 */
	private InetAddress findGatewayAddress() {
		
		InetAddress gateway = null;
		
		try {
			gateway = GatewayFinder.find(
					Server.getInstance().getInfo().getDevice());
		} catch (IOException e) {
			throw new UnexpectedErrorException( e, "error querying OS for gateway");
		}
		
		return gateway;
		
	}

	/**
	 * Main service loop. Send an ARP request to retrieve the gateway's MAC
	 * Address and wait for the response.
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
	 * Send an ARP request to find out the Gateway's MAC Address.
	 * This method will block until an ARP Response has been received.
	 * 
	 * @return the gateway's MAC Address
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
