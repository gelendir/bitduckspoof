package org.bitducks.spoofing.core.rules;

import java.net.InetAddress;
import java.util.Arrays;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

public class SingleARPResponseRule extends ARPResponseRule {
	
	private byte[] address;
	
	public SingleARPResponseRule( InetAddress address ) {
		this.address = address.getAddress();
	}

	@Override
	public boolean checkRule(Packet p) {
		if( super.checkRule(p) ) {
			ARPPacket packet = ((ARPPacket)p);
			return Arrays.equals( packet.sender_protoaddr, this.address ); 
		}
		
		return false;
	}
	
	

}
