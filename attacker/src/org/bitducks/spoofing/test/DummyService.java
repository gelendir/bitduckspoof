package org.bitducks.spoofing.test;

import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Service;

public class DummyService extends Service {
	
	public DummyService() {
		
		this.getPolicy().addRule(new DummyRule());
	}

	@Override
	public void run() {
		
		System.out.println("Dummy service started");
		
		while( true ) {
			System.out.println("waiting for packet");
			Packet packet = this.getNextBlockingPacket();
			System.out.println("DUMMY " + packet.toString());
		}
		
	}

}
