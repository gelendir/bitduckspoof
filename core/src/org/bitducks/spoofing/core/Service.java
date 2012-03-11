package org.bitducks.spoofing.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jpcap.packet.Packet;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

/**
 * This class is used to implement a service in this program.
 * The notion of service is that a service is habitually a
 * thread that receive packet from the Server class and 
 * performs treatment on the received packet.
 * 
 * The implementation of a service must always call {@link #isCloseRequested()}
 * in a while to know if a close is requested. Also, a Packet.EOF
 * is send to unblock the waiting for a new packet.
 * 
 * @author Frédérik Paradis and Simon Perreault
 */
public abstract class Service extends Thread {
	
	/**
	 * The queue of packet of the service.
	 */
	private LinkedBlockingQueue<Packet> receivePackets = new LinkedBlockingQueue<Packet>();
	
	/**
	 * The policy of rule of the service.
	 */
	private Policy policy = new Policy();
	
	/**
	 * Indicate if a close is requested. If its value is true,
	 * the close is requested.
	 */
	private volatile boolean closeRequested = false;
	
	/**
	 * The logger of the service.
	 */
	protected Logger logger;
	
	/**
	 * This constructor initialize the logger and must
	 * be always call by the subclass by super();.
	 */
	public Service() {
		this.logger = Logger.getLogger(this.getClass());
	}
	
	/**
	 * This method block the thread until a packet is sent
	 * from the Server class. A Packet.EOF is sent if the close
	 * of this service is requested.
	 * @return Return a packet destined for this service.
	 */
	protected Packet getNextBlockingPacket() {
		try {
			return this.receivePackets.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * This method block the thread for a maximum of timeout 
	 * milliseconds or until a packet is sent from the Server 
	 * class. A Packet.EOF is sent if the close of this service
	 * is requested.
	 * @return Return a packet destined for this service.
	 */
	protected Packet getNextPacket(long timeout) {
		try {
			return this.receivePackets.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * This method return a packet if there is a packet in the 
	 * packet queue or null otherwise.
	 * @return Return a packet if there is a packet in the 
	 * packet queue or null otherwise.
	 */
	protected Packet getNextNonBlockingPacket() {
		return this.receivePackets.poll();
	}
	
	/**
	 * This method is habitually used by the Server class to
	 * add packet in the packet queue of this service.
	 * @param p Packet to add.
	 */
	public void pushPacket(Packet p) {
		this.receivePackets.add(p);
	}
	
	/**
	 * This method clear the packet queue of this service.
	 */
	public void clearQueue() {
		this.receivePackets.clear();
	}
	
	/**
	 * This method return the policy of rules of this service.
	 * @return Return the policy of rules of this service.
	 */
	public Policy getPolicy() {
		return this.policy;
	}
	
	/**
	 * This method is use to indicate to the service's thread
	 * to close the thread properly.
	 */
	public void closeService() {
		this.closeRequested = true;
		
		//We push Packet.EOF to indicate to the service thread to close the thread.
		this.pushPacket(Packet.EOF);
	}
	
	/**
	 * This method return true if the close of this service is
	 * request or false otherwise.
	 * @return Return true if the close of this service is
	 * request or false otherwise.
	 */
	public boolean isCloseRequested() {
		return this.closeRequested;
	}
	
	/**
	 * The method to implement the service.
	 */
	public abstract void run();
	
	/**
	 * This method is used to add a log appender to
	 * the logger of this object.
	 * @param appender A log appender
	 */
	public void addLogAppender( Appender appender ) {
		this.logger.addAppender( appender );
	}
	
	/**
	 * This method is used to remove a log appender to
	 * the logger of this object.
	 * @param appender A log appender
	 */
	public void removeLogAppender( Appender appender ) {
		this.logger.removeAppender( appender );
	}
	
	/**
	 * This method return the name of the class of this object.
	 * @return Return the name of the class of this object.
	 */
	public String serviceName() {
		return this.getClass().getCanonicalName();
	}



}
