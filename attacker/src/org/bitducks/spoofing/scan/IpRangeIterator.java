package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.bitducks.spoofing.util.Constants;

public class IpRangeIterator implements Iterator<InetAddress> {
	
	int start;
	int end;
	int current;
	
	public IpRangeIterator( byte[] ipStart, byte[] ipEnd ) {
		this.start = ByteBuffer.wrap(ipStart).getInt(0);
		this.end = ByteBuffer.wrap(ipEnd).getInt(0);
		this.current = this.start;
		
	}
	

	public IpRangeIterator(InetAddress start, InetAddress end) {
		this(start.getAddress(), end.getAddress());
	}

	@Override
	public boolean hasNext() {
		return this.current <= this.end;
	}

	@Override
	public InetAddress next() {

		ByteBuffer buffer = ByteBuffer.allocate( Constants.IPV4_LEN );
		buffer.putInt(this.current);
		this.current++;
		
		try {
			return InetAddress.getByAddress(buffer.array());
		} catch (UnknownHostException e) {
			return null;
		}
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub	
	}

	public void reset() {
		this.current = this.start;
	}	

}
