package org.bitducks.spoofing.core;

import java.util.concurrent.LinkedBlockingQueue;

import jpcap.packet.Packet;

public class PacketQueue {
	private LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue<Packet>();
	
	public PacketQueue() {
		
	}
	
	public void push(Packet p) {
		try {
			this.packets.put(p);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Packet pop() {
		return this.packets.remove();
	}
	
	
}