package org.bitducks.spoofing.core;

import java.net.InetAddress;

import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

public class Info {
	
	NetworkInterface device;
	
	public Info( NetworkInterface device ) {
		this.device = device;
	}
	
	private NetworkInterfaceAddress getDeviceAddress() {
		return this.device.addresses[0];
	}
	
	public NetworkInterface getDevice() {
		return this.device;
	}
		
	public InetAddress getAddress() {
		return this.getDeviceAddress().address;
	}
	
	public InetAddress getSubnet() {
		return this.getDeviceAddress().subnet;
	}
	
	public InetAddress getMask() {
		return this.getSubnet();
	}
	
	public byte[] getMacAddress() {
		return this.getDevice().mac_address;
	}

}
