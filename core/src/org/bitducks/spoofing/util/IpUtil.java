package org.bitducks.spoofing.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.bitducks.spoofing.exception.UnexpectedErrorException;

import jpcap.NetworkInterfaceAddress;

public abstract class IpUtil {
	
	public static InetAddress network (NetworkInterfaceAddress address ) {
		return IpUtil.network( address.address, address.subnet );
	}
	
	public static InetAddress lastIpInNetwork( NetworkInterfaceAddress address ) {
		return IpUtil.lastIpInNetwork( address.broadcast );
	}
	
	public static InetAddress network( InetAddress address, InetAddress subnet ) {
		
		byte[] network = address.getAddress();
		byte[] mask = subnet.getAddress();
		
		for( int i = 0; i < network.length; ++i ) {
			network[i] = (byte) (network[i] & mask[i]);
		}
		
		return IpUtil.bytesToInet(network);
		
	}
	
	public static InetAddress lastIpInNetwork( InetAddress broadcast ) {
		
		int rawBroadcast = ByteBuffer.wrap(broadcast.getAddress()).getInt();
		rawBroadcast--;
		
		ByteBuffer buffer = ByteBuffer.allocate( Constants.IPV4_LEN );
		buffer.putInt(rawBroadcast);
		
		return IpUtil.bytesToInet(buffer.array());
		
	}
	
	public static InetAddress bytesToInet( byte[] address ) {
		
		try{
			return InetAddress.getByAddress( address );
		} catch( UnknownHostException e) {
			throw new UnexpectedErrorException(e, "Error converting ip to InetAddress");
		}
	}

}
