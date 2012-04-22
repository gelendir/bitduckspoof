package org.bitducks.spoofing.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.exception.UnexpectedErrorException;

import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

/**
 * Utilities related to managing IP Addresses
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public abstract class IpUtil {
	
	/**
	 * Return the IP Address of a network device address
	 * 
	 * @param address network device address
	 * @return IP Address
	 */
	public static InetAddress network( NetworkInterfaceAddress address ) {
		return IpUtil.network( address.address, address.subnet );
	}
	
	/**
	 * Return the IP Address of a network device
	 * 
	 * @param address network device
	 * @return IP Address
	 */
	public static InetAddress network( NetworkInterface device ) {
		InterfaceInfo info = new InterfaceInfo( device );
		return IpUtil.network( info.getAddress(), info.getSubnet() );
	}
	
	/**
	 * Return the network address, a.k.a first IP of a subnet.
	 * @param address The device's IP Address
	 * @param subnet The network's subnet
	 * @return The network address
	 */
	public static InetAddress network( InetAddress address, InetAddress subnet ) {
		
		byte[] network = address.getAddress();
		byte[] mask = subnet.getAddress();
		
		for( int i = 0; i < network.length; ++i ) {
			network[i] = (byte) (network[i] & mask[i]);
		}
		
		return IpUtil.bytesToInet(network);
		
	}
	
	/**
	 * Return the last IP in a network, a.k.a the IP just before network broadcast.
	 * @param address The network device address
	 * @return Last IP Address
	 */
	public static InetAddress lastIpInNetwork( NetworkInterfaceAddress address ) {
		return IpUtil.lastIpInNetwork( address.broadcast );
	}
	
	/**
	 * Return the last IP in a network, a.k.a the IP just before network broadcast.
	 * @param address The network device
	 * @return Last IP Address
	 */
	public static InetAddress lastIpInNetwork( NetworkInterface device  ) {
		InterfaceInfo info = new InterfaceInfo( device );
		return IpUtil.lastIpInNetwork( info.getBroadcast() );
	}
	
	/**
	 * Return the last IP in a network, a.k.a the IP just before network broadcast.
	 * @param address The broadcast address
	 * @return Last IP Address
	 */
	public static InetAddress lastIpInNetwork( InetAddress broadcast ) {
		
		int rawBroadcast = ByteBuffer.wrap(broadcast.getAddress()).getInt();
		rawBroadcast--;
		
		ByteBuffer buffer = ByteBuffer.allocate( Constants.IPV4_LEN );
		buffer.putInt(rawBroadcast);
		
		return IpUtil.bytesToInet(buffer.array());
		
	}
	
	/**
	 * Return the last IP in a network, a.k.a the IP just before network broadcast.
	 * @param address The network device address
	 * @return Last IP Address
	 */
	public static InetAddress lastIpInNetwork2( NetworkInterfaceAddress address ) {
		return IpUtil.lastIpInNetwork2( address.address, address.subnet );
	}
	
	/**
	 * Return the last IP in a network, a.k.a the IP just before network broadcast.
	 * @param address The network device's address
	 * @param subnet network's subnet
	 * @return Last IP Address
	 */
	public static InetAddress lastIpInNetwork2( InetAddress address, InetAddress subnet ) {
		byte[] network = address.getAddress();
		byte[] mask = subnet.getAddress();
		
		for( int i = 0; i < network.length; ++i ) {
			network[i] = (byte) ((network[i] & mask[i]) | (mask[i] ^ (byte)0xff));
		}
		
		return IpUtil.lastIpInNetwork(IpUtil.bytesToInet(network));
	}
	
	/**
	 * Transform a raw array of bytes into an IP Address.
	 * @param address raw bytes representing the IP Address
	 * @return IP Address
	 */
	public static InetAddress bytesToInet( byte[] address ) {
		
		try{
			return InetAddress.getByAddress( address );
		} catch( UnknownHostException e) {
			throw new UnexpectedErrorException(e, "Error converting ip to InetAddress");
		}
	}
	
	/**
	 * Return a string formatting for more easily reading a MAC Address
	 * @param mac raw bytes of the MAC Address
	 * @return string representing the MAC Address (00:11:22:33:44:55)
	 */
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
	
	/**
	 * Convert a HEX byte to upper case char
	 * @param b hex char
	 * @return upper-cased hex char
	 */
    public static char hexUpperChar(byte b){
        b=(byte)((b>>4)&0xf);
        if(b==0) return '0';
        else if(b<10) return (char)('0'+b);
        else return (char)('a'+b-10);
    }

	/**
	 * Convert a HEX byte to lower case char
	 * @param b hex char
	 * @return lower-cased hex char
	 */
    public static char hexLowerChar(byte b){
        b=(byte)(b&0xf);
        if(b==0) return '0';
        else if(b<10) return (char)('0'+b);
        else return (char)('a'+b-10);
    }


}
