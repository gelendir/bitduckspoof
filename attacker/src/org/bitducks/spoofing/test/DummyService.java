package org.bitducks.spoofing.test;

import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Service;

public class DummyService extends Service {
	
	public DummyService() {
		super();
		this.getPolicy().addRule(new DummyRule());
	}

	@Override
	public void run() {
		
		this.logger.info("Dummy service started");
		
		while( true ) {
			this.logger.info("waiting for packet");
			Packet packet = this.getNextBlockingPacket();
			this.logger.info("DUMMY " + packet.toString());
		}
		
	}

}
