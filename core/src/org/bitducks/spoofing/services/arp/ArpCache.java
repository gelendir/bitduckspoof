package org.bitducks.spoofing.services.arp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.bitducks.spoofing.util.Util;

public class ArpCache {
	
	private HashMap<InetAddress, ArpCacheEntry> addresses;
	
	public ArpCache() {
		this.addresses = new HashMap<InetAddress, ArpCacheEntry>();
	}
	
	public void add(InetAddress address, byte[] mac) {
		
		ArpCacheEntry entry = this.addresses.get(address);
		
		if( entry == null ) {
			
			entry = new ArpCacheEntry(
					mac,
					Util.unixTimestampMillis() );
			
			this.addresses.put(address, entry);
			
		} else {
			
			entry.setTimestamp( Util.unixTimestampMillis() );
			
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
		
		long minimum = Util.unixTimestampMillis() - (long)timeout;
		if( entry.getTimestamp() > minimum ) {
			return true;
		}
		
		return false;
		
	}
	
	public Set<InetAddress> allAdresses() {
		return this.addresses.keySet();
	}
	
	public Set<InetAddress> allAddresses( int timeout ) {
		
		long minimum = Util.unixTimestampMillis() - (long)timeout;
		
		Set<InetAddress> alive = new HashSet<InetAddress>();
		for( Entry<InetAddress, ArpCacheEntry> entry: this.addresses.entrySet() ) {
			
			if( entry.getValue().getTimestamp() > minimum ) {
				alive.add( entry.getKey() );
			}
			
		}
		
		return alive;
		
	}
	
	public byte[] getMac(InetAddress address) {
		return this.addresses.get(address).getMacAddress();
	}
		
	public String toString() {
		return this.addresses.toString();
	}

}
