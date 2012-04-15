package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPRule;

/**
 * This class is a detector of ARP spoofing. It calculates the
 * rate between the number of the ARP replies and ARP requests
 * received. If there is more than one ARP reply for an
 * IP address, this means that this IP address may be spoofed.
 * 
 * @author Frédérik Paradis
 */
public class ARPReplyRateService extends Service {

	/**
	 * The interval between each calculation of the rate in seconds.
	 */
	private volatile int interval;

	/**
	 * This constructor. Initializes the service with the 
	 * interval between each calculation of the rate.
	 * 
	 * @param interval The interval between each calculation
	 * of the rate in seconds.
	 */
	public ARPReplyRateService(int interval) {
		this.getPolicy().addRule(new ARPRule());
		this.interval = interval;
	}

	/**
	 * This method waits for an interval and calculates the rate between
	 * the number of ARP requests and ARP responses received. 
	 * If there is more than one ARP reply for an IP address, this 
	 * means that this IP address may be spoofed.
	 */
	@Override
	public void run() {
		while(!this.isCloseRequested()) {
			try {
				synchronized (this) {
					this.wait(this.interval * 1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Map<InetAddress, Integer> c = new HashMap<InetAddress, Integer>();

			Packet p = this.getNextNonBlockingPacket();
			while(p != null && !p.equals(Packet.EOF)) {
				ARPPacket arp = (ARPPacket)p;
				InetAddress addr = null;
				int bound = 0;

				if(arp.operation == 1) {
					addr = (InetAddress) arp.getTargetProtocolAddress();
					bound = -1;
				} else if(arp.operation == 2) {
					addr = (InetAddress) arp.getSenderProtocolAddress();
					bound = 1;
				}

				if(arp.operation == 1 || arp.operation == 2) {
					if(c.containsKey(addr)) {
						c.put(addr, c.get(addr) + bound);
					} else {
						c.put(addr, bound);
					}	
				}

				p = this.getNextNonBlockingPacket();
			}

			boolean possible = false;
			for(Entry<InetAddress, Integer> entry : c.entrySet()) {
				if(entry.getValue() > 0) {
					this.logger.warn("There is a possibility of spoof of the address " + entry.getKey().getHostAddress());
					possible = true;
				}
			}

			if(!possible) {
				this.logger.info("There is NO possibility that you are under ARP spoofing.");
			}
		}
	}
	
	/**
	 * This method set the interval used between each calculation of the rate.
	 * @param interval The interval used between each calculation of the rate.
	 */
	public void setInterval(int interval)
	{
		this.interval = interval;
	}
	
	/**
	 * This method overwrite closeService to notify the wait call
	 * in the service thread.
	 * @see Service#closeService()
	 */
	@Override
	public synchronized void closeService() {
		super.closeService();
		this.notify();
	}
}
