package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;

import org.bitducks.spoofing.util.Util;

public class ArpCache {
	
	HashMap<InetAddress, ArpCacheEntry> addresses;
	
	public ArpCache() {
		this.addresses = new HashMap<InetAddress, ArpCacheEntry>();
	}
	
	public void add(InetAddress address, byte[] mac) {
		
		ArpCacheEntry entry = this.addresses.get(address);
		
		if( entry == null ) {
			
			entry = new ArpCacheEntry(
					mac,
					Util.unixTimestamp() );
			
			this.addresses.put(address, entry);
			
		} else {
			
			entry.setTimestamp( Util.unixTimestamp() );
			
		}
				
	}
	
	public void add(byte[] address, byte[] mac) {
		
		InetAddress iAddress;
		try {
			iAddress = InetAddress.getByAddress(address);
		} catch (UnknownHostException e) {
			throw new RuntimeException("error adding byte address to arp cache");
		}
		this.add(iAddress, mac);
		
	}
	
	public boolean hasAddress( InetAddress address ) {
		return this.addresses.containsKey(address);
	}
	
	public boolean hasAddress( InetAddress address, int timeout ) {
		
		ArpCacheEntry entry = this.addresses.get(address);
		if( entry == null ) {
			return false;
		}
		
		int minimum = Util.unixTimestamp() - timeout;
		if( entry.getTimestamp() > minimum ) {
			return true;
		}
		
		return false;
		
	}
	
	public byte[] getMac(InetAddress address) {
		return this.addresses.get(address).getMacAddress();
	}
		
	public String toString() {
		return this.addresses.toString();
	}

}
