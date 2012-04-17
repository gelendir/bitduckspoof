package org.bitducks.spoofing.core;

import java.net.InetAddress;
import java.util.Arrays;

public class Computer {

	private InetAddress ipAddress;
	private byte[] macAddress;
	private int portNumber;

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public Computer setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}

	public byte[] getMacAddress() {
		return macAddress;
	}

	public Computer setMacAddress(byte[] macAddress) {
		this.macAddress = macAddress;
		return this;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public Computer setPortNumber(int portNumber) {
		this.portNumber = portNumber;
		return this;
	}

	private String getMacAddressString() {
		String mac = "";
		for (byte b : getMacAddress()) {  
			String temp = Integer.toHexString(b&0xff);
			if (temp.length() == 1){
				temp = "0" + temp;
			}
			mac += temp + ":";
		}
		mac = mac.substring(0, 17);
		return mac;
	}
	
	@Override
	public String toString() {
		String toReturn = this.ipAddress.toString()
				+" ( " + getMacAddressString() + " ) : " +
				this.portNumber;
		return toReturn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + Arrays.hashCode(macAddress);
		result = prime * result + portNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (!Arrays.equals(macAddress, other.macAddress))
			return false;
		if (portNumber != other.portNumber)
			return false;
		return true;
	}

}