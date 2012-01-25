package org.bitducks.spoofing.main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;


import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.scan.ArpService;
import org.bitducks.spoofing.scan.IpRange;
import org.bitducks.spoofing.util.IpUtil;

public class Tests {
	
	final static private int NB_DEVICE = 0;
	
	public static NetworkInterface getDevice() {
		
		NetworkInterface device = JpcapCaptor.getDeviceList()[ Tests.NB_DEVICE ];
		return device;
		
	}

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws Exception {
		
		testIpUtils();
		testIpMask();
		testIpRange();
		testArpService();

	}
	
	private static void testIpUtils() {
		
		NetworkInterface device = getDevice();
		NetworkInterfaceAddress deviceAddress = device.addresses[0];
		
		InetAddress iAddress = device.addresses[0].address;
		InetAddress iMask = device.addresses[0].subnet;
		InetAddress iBroadcast = device.addresses[0].broadcast;
		
		InetAddress network = IpUtil.network(iAddress, iMask);
		InetAddress last = IpUtil.lastIpInNetwork(iBroadcast);
		
		System.out.println(network);
		System.out.println(last);
		System.out.println( IpUtil.network(deviceAddress) );
		System.out.println( IpUtil.lastIpInNetwork(deviceAddress) );
		
	}

	private static void testIpMask() {
		
		NetworkInterface device = getDevice();
		
		InetAddress iAddress = device.addresses[0].address;
		InetAddress iMask = device.addresses[0].subnet;
		InetAddress iBroadcast = device.addresses[0].broadcast;
		
		int address = ByteBuffer.wrap(iAddress.getAddress()).getInt(0);
		int mask = ByteBuffer.wrap(iMask.getAddress()).getInt(0);
		
		int network = address & mask;
		
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(network);
				
		byte[] bufNetwork = new byte[4];
		for( int i = 0; i < 4; i++ ) {
			bufNetwork[i] = buffer.get(i);
		}
		
		InetAddress iNetwork = null;
		try {
			iNetwork = InetAddress.getByAddress(bufNetwork);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(iNetwork);
		
	}

	public static void testIpRange() throws Exception {
		
		InetAddress start = InetAddress.getByName("192.168.0.1");
		InetAddress end = InetAddress.getByName("192.168.0.255");
		
		IpRange range = new IpRange(start.getAddress(), end.getAddress());
		
		for( InetAddress a: range) {
			System.out.println(a);
		}
		
	}
	
	public static void testArpService() throws Exception {
		
		ArpService arpService = new ArpService();
		
		Server server = new Server();
		server.addService(arpService);
		
		server.start();
		
	}

}
