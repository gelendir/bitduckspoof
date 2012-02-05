package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.util.Collection;

/**
 * This interface is called after an ARP scan of
 * IP addresses is made.
 * @author Frédérik Paradis
 */
public interface ArpScanFinish {
	
	/**
	 * This method is called after an ARP scan of
	 * IP addresses is made.
	 * @param addresses The collection of addresses
	 */
	public void scanFinished( Collection<InetAddress> addresses );
	

}