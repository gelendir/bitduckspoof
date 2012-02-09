package org.bitducks.spoofing.services.arp;

public class ArpCacheEntry {
	
	private byte[] macAddress;
	private long timestamp;

	public ArpCacheEntry( byte[] macAddress, long timestamp ) {
		this.macAddress = macAddress;
		this.timestamp = timestamp;
	}

	public byte[] getMacAddress() {
		return macAddress;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp( long timestamp ) {
		this.timestamp = timestamp;
	}
	
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
