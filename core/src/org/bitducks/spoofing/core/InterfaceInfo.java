package org.bitducks.spoofing.core;

import java.net.InetAddress;

import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

/**
 * This class is an utility for getting information
 * about a network interface. 
 * @author Frédérik Paradis
 */
public class InterfaceInfo {
	
	/**
	 * The device
	 */
	NetworkInterface device;
	
	/**
	 * The constructor initialize the object with
	 * a network interface.
	 * @param device A network interface
	 */
	public InterfaceInfo( NetworkInterface device ) {
		this.device = device;
	}
	
	/**
	 * This method return the NetworkInterfaceAddress of
	 * the network interface.
	 * @return Return the NetworkInterfaceAddress of
	 * the network interface.
	 */
	public NetworkInterfaceAddress getDeviceAddress() {
		return this.device.addresses[0];
	}
	
	/**
	 * This method return the network interface.
	 * @return Return the network interface.
	 */
	public NetworkInterface getDevice() {
		return this.device;
	}
		
	/**
	 * This method return the IP address of this interface.
	 * @return Return the IP address of this interface.
	 */
	public InetAddress getAddress() {
		return this.getDeviceAddress().address;
	}
	
	/**
	 * This method return the subnet IP address of this interface.
	 * @return Return the subnet IP address of this interface.
	 */
	public InetAddress getSubnet() {
		return this.getDeviceAddress().subnet;
	}
	
	/**
	 * This method return the mask IP address of this interface.
	 * @return Return the mask IP address of this interface.
	 */
	public InetAddress getMask() {
		return this.getSubnet();
	}
	
	/**
	 * This method return the MAC address of this interface.
	 * @return Return the MAC address of this interface.
	 */
	public byte[] getMacAddress() {
		return this.getDevice().mac_address;
	}

	/**
	 * This method return the broadcast IP address for this interface.
	 * @return Return the broadcast IP address for this interface.
	 */
	public InetAddress getBroadcast() {
		return this.getDeviceAddress().broadcast;
	}

}
