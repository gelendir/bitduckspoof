package org.bitducks.spoofing.util;

import java.net.InetAddress;
import java.util.Iterator;

/**
 * Iterator for generating IP Addresses in a certain range.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class IpRange implements Iterable<InetAddress> {
	
	/**
	 * Start IP Address
	 */
	byte[] start;
	
	/**
	 * End IP Address
	 */
	byte[] end;
	
	/**
	 * Constructor. Creates a new IP Range Iterator
	 * @param start Raw IP Address to start at
	 * @param end Raw IP Address to stop at
	 */
	public IpRange(byte[] start, byte[] end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Constructor. Creates a new IP Range Iterator
	 * @param start IP Address to start at
	 * @param end IP Address to stop at
	 */
	public IpRange(InetAddress start, InetAddress end) {
		this.start = start.getAddress();
		this.end = end.getAddress();
	}

	@Override
	public Iterator<InetAddress> iterator() {
		IpRangeIterator iterator = new IpRangeIterator(this.start, this.end);
		return iterator;
	}

}
