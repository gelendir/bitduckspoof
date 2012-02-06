package org.bitducks.spoofing.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.exception.UnexpectedErrorException;

import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

public abstract class IpUtil {
	
	public static InetAddress network( NetworkInterfaceAddress address ) {
		return IpUtil.network( address.address, address.subnet );
	}
	
	public static InetAddress network( NetworkInterface device ) {
		InterfaceInfo info = new InterfaceInfo( device );
		return IpUtil.network( info.getAddress(), info.getSubnet() );
	}
	
	public static InetAddress network( InetAddress address, InetAddress subnet ) {
		
		byte[] network = address.getAddress();
		byte[] mask = subnet.getAddress();
		
		for( int i = 0; i < network.length; ++i ) {
			network[i] = (byte) (network[i] & mask[i]);
		}
		
		return IpUtil.bytesToInet(network);
		
	}
	
	public static InetAddress lastIpInNetwork( NetworkInterfaceAddress address ) {
		return IpUtil.lastIpInNetwork( address.broadcast );
	}
	
	public static InetAddress lastIpInNetwork( NetworkInterface device  ) {
		InterfaceInfo info = new InterfaceInfo( device );
		return IpUtil.lastIpInNetwork( info.getBroadcast() );
	}
	
	public static InetAddress lastIpInNetwork( InetAddress broadcast ) {
		
		int rawBroadcast = ByteBuffer.wrap(broadcast.getAddress()).getInt();
		rawBroadcast--;
		
		ByteBuffer buffer = ByteBuffer.allocate( Constants.IPV4_LEN );
		buffer.putInt(rawBroadcast);
		
		return IpUtil.bytesToInet(buffer.array());
		
	}
	
	public static InetAddress lastIpInNetwork2( NetworkInterfaceAddress address ) {
		return IpUtil.lastIpInNetwork2( address.address, address.subnet );
	}
	
	public static InetAddress lastIpInNetwork2( InetAddress address, InetAddress subnet ) {
		byte[] network = address.getAddress();
		byte[] mask = subnet.getAddress();
		
		for( int i = 0; i < network.length; ++i ) {
			network[i] = (byte) ((network[i] & mask[i]) | (mask[i] ^ (byte)0xff));
		}
		
		return IpUtil.lastIpInNetwork(IpUtil.bytesToInet(network));
	}
	
	public static InetAddress bytesToInet( byte[] address ) {
		
		try{
			return InetAddress.getByAddress( address );
		} catch( UnknownHostException e) {
			throw new UnexpectedErrorException(e, "Error converting ip to InetAddress");
		}
	}
	
	public static String prettyPrintMac( byte[] mac ) {
			
		char[] adr=new char[17];
		
			for(int i=0;i<5;i++){
				adr[i*3] = IpUtil.hexUpperChar(mac[i]);
				adr[i*3+1] = IpUtil.hexLowerChar(mac[i]);
				adr[i*3+2]=':';
			}
		
	    adr[15]=hexUpperChar(mac[5]);
	    adr[16]=hexLowerChar(mac[5]);
	
	    return new String(adr);
		
	}
	
    public static char hexUpperChar(byte b){
        b=(byte)((b>>4)&0xf);
        if(b==0) return '0';
        else if(b<10) return (char)('0'+b);
        else return (char)('a'+b-10);
    }

    public static char hexLowerChar(byte b){
        b=(byte)(b&0xf);
        if(b==0) return '0';
        else if(b<10) return (char)('0'+b);
        else return (char)('a'+b-10);
    }


}
