package org.bitducks.spoofing.services;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPRule;

public class ARPReplyRateService extends Service {

	private int interval;
	
	public ARPReplyRateService(int interval) {
		this.getPolicy().addRule(new ARPRule());
		this.interval = interval;
	}

	@Override
	public void run() {
		while(!this.isCloseRequested()) {
			try {
				Thread.sleep(this.interval * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int nbRequest = 0;
			int nbReply = 0;

			Packet p = this.getNextNonBlockingPacket();
			while(p != null && !p.equals(Packet.EOF)) {
				ARPPacket arp = (ARPPacket)p;

				if(arp.operation == 1) {
					++nbRequest;
				} else if(arp.operation == 2) {
					++nbReply;
				}

				p = this.getNextNonBlockingPacket();
			}

			if(nbReply > nbRequest) {
				this.logger.warn("There is a possibility that you are under ARP spoofing.");
			} else {
				this.logger.info("There is NO possibility that you are under ARP spoofing.");
			}
		}
	}

}
