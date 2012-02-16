package org.bitducks.spoofing.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jpcap.packet.Packet;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public abstract class Service extends Thread {
	private LinkedBlockingQueue<Packet> receivePackets = new LinkedBlockingQueue<Packet>();
	private Policy policy = new Policy();
	private volatile boolean closeRequested = false;
	private volatile boolean isStarted = false;
	protected Logger logger;
	
	public Service() {
		this.logger = Logger.getLogger(this.getClass());
	}
	
	protected Packet getNextBlockingPacket() {
		try {
			return this.receivePackets.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected Packet getNextPacket(long timeout) {
		try {
			return this.receivePackets.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected Packet getNextNonBlockingPacket() {
		return this.receivePackets.poll();
	}
	
	public void pushPacket(Packet p) {
		this.receivePackets.add(p);
	}
	
	public void clearQueue() {
		this.receivePackets.clear();
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
	
	public void addLogAppender( Appender appender ) {
		this.logger.addAppender( appender );
	}
	
	public void removeLogAppender( Appender appender ) {
		this.logger.removeAppender( appender );
	}
	
	public String serviceName() {
		return this.getClass().getCanonicalName();
	}



}
