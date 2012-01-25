package org.bitducks.spoofing.services;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.packet.DNSPacket;
import org.bitducks.spoofing.packet.PacketFactory;

public class DNSService extends Service {

	@Override
	public void run() {
		UDPPacket queryPaquet = (UDPPacket)this.getNextPacket();
		
		queryPaquet.
		
		/*DNSPacket answerPaquet = PacketFactory.dnsRequest(src_port, 
				dst_port, 
				ipSource, 
				ipTarget, 
				transactionId, 
				questions, 
				query, 
				ipFalseDNS);*/
		

	}

}
