package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.DNSRule;
import org.bitducks.spoofing.packet.DNSPacket;
import org.bitducks.spoofing.util.IpUtil;

/**
 * This class is used to detect the presence of a rogue DNS on the 
 * network. This service works in the following manner: it waits for a
 * client to send a DNS request and repeats the same DNS request but 
 * with a non existing DNS server. If there is a reply from this 
 * server, this means that there is a rogue DNS on the network.
 * 
 * @author Frédérik Paradis
 */
public class DNSProtectionService extends Service {
	
	private static InetAddress fakeDNS = IpUtil.bytesToInet(new byte[] {(byte)240,1,2,3} );

	/**
	 * This constructor. Initializes the DNSProtectionService.
	 */
	public DNSProtectionService() {
		super();
		this.getPolicy().addRule(new DNSRule());
	}

	/**
	 * This method waits for a DNS request from a Client and re-sends 
	 * the same DNS request but to a non existing DNS 
	 * server. If there is a reply from this server, this means that 
	 * there is a rogue DNS on the network.
	 */
	@Override
	public void run() {
		
		this.logger.info("DNS protection service started.");
		
		InetAddress fakeDNSIP = null;
		try {
			fakeDNSIP = InetAddress.getByAddress(new byte[] {(byte)240, 1, 2, 3});
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(!this.isCloseRequested()) {
			Packet p = this.getNextBlockingPacket();

			if(p != null && !p.equals(Packet.EOF)) {
				UDPPacket dns = (UDPPacket)p;
				
				this.logIfNeeded(dns);
				
				if(dns.src_port == 53) {
					if(dns.src_ip.equals(fakeDNSIP)) {
						this.logger.warn("DNS spoofing detected!");
					}
				} else if(dns.dst_port == 53 && !dns.dst_ip.equals(fakeDNSIP)) {
					dns.dst_ip = fakeDNSIP;
					Server.getInstance().sendPacket(dns);
				}
			}
		}

	}
	
	/**
	 * Log a message to the logger only if needed. Used mainly as a helper for the GUI interface.
	 * 
	 * @param packet Packet received by the service.
	 */
	private void logIfNeeded(UDPPacket packet) {
		
		InetAddress source = packet.src_ip;
		InetAddress destination = packet.dst_ip;
		if( !destination.equals( fakeDNS ) ) {
			
			this.logger.info(
					"source: " + source.toString() + 
					" destination: " + destination.toString() +
					" query: " + new DNSPacket(packet).getDomainName() );
			
		}
		
	}

}
