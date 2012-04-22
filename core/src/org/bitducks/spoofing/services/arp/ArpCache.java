package org.bitducks.spoofing.services.arp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.bitducks.spoofing.util.Util;

/**
 * ARP Cache. Used for caching ARP requests
 * received by an ARP Service. Can be queried later on
 * to find the MAC Address for an IP Address.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class ArpCache {
	
	/**
	 * Internal cache for MAC Addresses
	 */
	private HashMap<InetAddress, ArpCacheEntry> addresses;
	
	/**
	 * Constructor. Create a new ArpCache
	 */
	public ArpCache() {
		this.addresses = new HashMap<InetAddress, ArpCacheEntry>();
	}
	
	/**
	 * Add a new MAC Address to the cache.
	 * 
	 * @param address IP Address
	 * @param mac MAC Address
	 */
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
	
	/**
	 * Add a new MAC Address to the cache
	 * 
	 * @param address IP Address (raw bytes)
	 * @param mac MAC Address
	 */
	public void add(byte[] address, byte[] mac) {
		
		InetAddress iAddress;
		try {
			iAddress = InetAddress.getByAddress(address);
		} catch (UnknownHostException e) {
			throw new RuntimeException("error adding byte address to arp cache");
		}
		this.add(iAddress, mac);
		
	}
	
	/**
	 * Check if a MAC Address for a certain IP Address is in the cache
	 * 
	 * @param address IP Address to look for
	 * @return True if the address is in the cache
	 */
	public boolean hasAddress( InetAddress address ) {
		return this.addresses.containsKey(address);
	}
	
	/**
	 * Check if a MAC Address for a certain IP Address is in the cache
	 * 
	 * @param address IP Address to look for
	 * @parma timeout ARP Reply timeout. If the query is older than the timeout, discard the result from the lookup.
	 * @return True if the address is in the cache
	 */
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
	
	/**
	 * Return a list of all addresses currently in the cache
	 * 
	 * @return list of IP -> MAC Addresses
	 */
	public Set<InetAddress> allAdresses() {
		return this.addresses.keySet();
	}
	
	/**
	 * Return a list of all addresses currently in the cache that
	 * are not older than a certain timeout
	 * 
	 * @param timeout number of seconds
	 * @return list of IP -> MAC Addresses
	 */
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
	
	/**
	 * Get the MAC Address corresponding to a IP address.
	 * Retruns null if the IP isn't in the cache.
	 * 
	 * @param address IP Address
	 * @return MAC Address
	 */
	public byte[] getMac(InetAddress address) {
		return this.addresses.get(address).getMacAddress();
	}
		
	/**
	 * Textual representation of the current state of the ARP cache.
	 */
	public String toString() {
		return this.addresses.toString();
	}

}
