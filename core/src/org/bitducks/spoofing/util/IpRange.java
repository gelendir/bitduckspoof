package org.bitducks.spoofing.util;

import java.net.InetAddress;
import java.util.Iterator;

public class IpRange implements Iterable<InetAddress> {
	
	byte[] start;
	byte[] end;
	
	public IpRange(byte[] start, byte[] end) {
		this.start = start;
		this.end = end;
	}
	
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
