package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class ArpCache {
	
	HashMap<InetAddress, byte[]> addresses;
	
	public ArpCache() {
		this.addresses = new HashMap<InetAddress, byte[]>();
	}
	
	public void add(InetAddress address, byte[] mac) {
		if( !this.hasAddress(address)) {
			this.addresses.put(address, mac);
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
	
	public byte[] getMac(InetAddress address) {
		return this.addresses.get(address);
	}
	
	public String toString() {
		return this.addresses.toString();
	}

}
