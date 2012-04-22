package org.bitducks.spoofing.services.arp;

/**
 * ARP Entry in a ARP Cache.
 * @see ArpCache
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class ArpCacheEntry {
	
	/**
	 * Raw MAC Address
	 */
	private byte[] macAddress;
	
	/**
	 * UNIX timestamp when the entry was generated
	 */
	private long timestamp;

	/**
	 * Constructor. Creates a new ArpCacheEntry
	 * @param macAddress raw MAC Address
	 * @param timestamp UNIX timestamp when the ARP reply was received
	 */
	public ArpCacheEntry( byte[] macAddress, long timestamp ) {
		this.macAddress = macAddress;
		this.timestamp = timestamp;
	}

	/**
	 * Return the MAC Address for this entry
	 * @return raw MAC Address
	 */
	public byte[] getMacAddress() {
		return macAddress;
	}

	/**
	 * Return the timestamp when this entry was generated 
	 * @return UNIX timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Set the timestamp when this entry was generated
	 * @param timestamp UNIX timestamp
	 */
	public void setTimestamp( long timestamp ) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Textual representation of a ArpCacheEntry
	 */
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		 for (byte b : this.macAddress) {
			 buffer.append(Integer.toHexString(b&0xff));
			 buffer.append(":");
		 }
		 buffer.deleteCharAt( buffer.length() -1 );
		 
		 buffer.append(",");
		 buffer.append(this.timestamp);
		 buffer.append(")");
		 
		 return buffer.toString();
		
	}
	
}
