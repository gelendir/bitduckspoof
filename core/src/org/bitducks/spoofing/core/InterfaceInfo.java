package org.bitducks.spoofing.core;

import java.net.Inet4Address;
import java.net.InetAddress;

import org.bitducks.spoofing.exception.UnexpectedErrorException;

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
	 * IPv4 Address used by the device
	 */
	NetworkInterfaceAddress deviceAddress = null;
	
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
		
		if( this.deviceAddress != null) {
			return this.deviceAddress;
		}
		
		for ( NetworkInterfaceAddress interfaceAddress: this.device.addresses ) {
			if( interfaceAddress.address instanceof Inet4Address ) {
				this.deviceAddress = interfaceAddress;
				return interfaceAddress;
			}
		}
		
		throw new UnexpectedErrorException("did not find any IPv4 address");
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
