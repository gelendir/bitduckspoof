package org.bitducks.spoofing.packet;
import java.io.IOException;
import java.net.InetAddress;

import org.bitducks.spoofing.exception.UnexpectedErrorException;
import org.bitducks.spoofing.util.gateway.GatewayFinder;

import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;

/**
 * Utility class for generating packets for a specific network device.
 * Generates packets and uses the network device for setting source MAC
 * and source IP.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 * @see PacketFactory
 *
 */
public class PacketGenerator {
	
	/**
	 * Network device to generate packets for.
	 */
	private NetworkInterface device;

	/**
	 * Constructor. Creates a new PacketGenerator.
	 * 
	 * @param device Network device to generate packets for.
	 */
	public PacketGenerator( NetworkInterface device ) {
		
		this.device = device;
		
	}
	
	/**
	 * Get the device's IP Address
	 * @return IP address
	 */
	public InetAddress getDeviceAddress() {
		
		return this.device.addresses[0].address;
		
	}
	
	/**
	 * Generate an ARP request
	 * 
	 * @see PacketFactory#arpRequest(byte[], InetAddress, InetAddress)
	 * @param ipTarget destination IP Address
	 * @return ARP Packet
	 */
	public ARPPacket arpRequest( InetAddress ipTarget ) {
		
		return PacketFactory.arpRequest(
				this.device.mac_address,
				this.getDeviceAddress(),
				ipTarget );
		
	}
	
	/**
	 * Get the gateway's IP Address for the network device.
	 * @return Gateway's IP Address
	 */
	public InetAddress getGateway() {
		
		try {
			return GatewayFinder.find( this.device );
		} catch (IOException e) {
			throw new UnexpectedErrorException(e, "Error parsing routing table while looking up gateway");
		}
		
	}

}
