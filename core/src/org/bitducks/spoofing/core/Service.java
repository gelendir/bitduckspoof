package org.bitducks.spoofing.core;

import java.util.concurrent.LinkedBlockingQueue;

import jpcap.JpcapSender;
import jpcap.packet.Packet;

public abstract class Service extends Thread {
	private LinkedBlockingQueue<Packet> receivePackets = new LinkedBlockingQueue<Packet>();
	private Policy policy = new Policy();
	private volatile boolean closeRequested = false;
	private volatile boolean isStarted = false;
	
	protected Packet getNextPacket() {
		try {
			return this.receivePackets.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void pushPacket(Packet p) {
		this.receivePackets.add(p);
	}
	
	public Policy getPolicy() {
		return this.policy;
	}
	
	@Override
	public void start() {
		this.isStarted = true;
		super.start();
	}
	
	protected void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public boolean isStarted() {
		return this.isStarted;
	}
	
	public void closeService() {
		this.closeRequested = true;
		//We push null object to indicate to the service thread to close the thread.
		this.pushPacket(Packet.EOF);
	}
	
	public boolean isCloseRequested() {
		return this.closeRequested;
	}
	
	public abstract void run();
}
