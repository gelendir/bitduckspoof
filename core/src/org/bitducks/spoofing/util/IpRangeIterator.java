package org.bitducks.spoofing.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.bitducks.spoofing.util.Constants;

/**
 * Iterator for generating IP Addresses in a certain range.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class IpRangeIterator implements Iterator<InetAddress> {
	
	/**
	 * Start IP Address (raw)
	 */
	int start;
	
	/**
	 * End IP Address (raw)
	 */
	int end;
	
	/**
	 * Current IP Address (raw)
	 */
	int current;
	
	/**
	 * Constructor. Creates a new IP Range Iterator
	 * @param ipStart Raw IP Address to start at
	 * @param ipEnd Raw IP Address to stop at
	 */
	public IpRangeIterator( byte[] ipStart, byte[] ipEnd ) {
		this.start = ByteBuffer.wrap(ipStart).getInt(0);
		this.end = ByteBuffer.wrap(ipEnd).getInt(0);
		this.current = this.start;
		
	}

	/**
	 * Constructor. Creates a new IP Range Iterator
	 * @param ipStart IP Address to start at
	 * @param ipEnd IP Address to stop at
	 */
	public IpRangeIterator(InetAddress start, InetAddress end) {
		this(start.getAddress(), end.getAddress());
	}

	/**
	 * Are there any IP Addresses left in the iterator ?
	 * @return True if there are still IP Addresses left
	 */
	@Override
	public boolean hasNext() {
		return this.current <= this.end;
	}

	/**
	 * Returns the next IP Address in the Iterator
	 */
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

	/**
	 * Reset the iterator to the first IP Address (start)
	 */
	public void reset() {
		this.current = this.start;
	}	

}
