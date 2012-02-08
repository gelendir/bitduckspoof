package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.DNSRule;

public class DNSProtection extends Service {

	public DNSProtection() {
		super();
		this.getPolicy().addRule(new DNSRule());
	}

	@Override
	public void run() {
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

}
