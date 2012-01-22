package org.bitducks.spoofing.util.dhcp;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class DHCPMainTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, IOException {
		//if(args.length<2){
		//	System.out.println("Usage: java Traceroute <device index (e.g., 0, 1..)> <target host address>");
		//	System.exit(0);
		//}
		
		//initialize Jpcap
		NetworkInterface device=JpcapCaptor.getDeviceList()[0];
		JpcapCaptor captor=JpcapCaptor.openDevice(device,2000,false,5000);
		InetAddress thisIP=null;
		for(NetworkInterfaceAddress addr:device.addresses)
			if(addr.address instanceof Inet4Address){
				thisIP=addr.address;
				break;
			}
		
		//obtain MAC address of the default gateway
		InetAddress pingAddr=InetAddress.getByName("www.microsoft.com");
		captor.setFilter("tcp and dst host "+pingAddr.getHostAddress(),true);
		byte[] gwmac=null;
		while(true){
			new URL("http://www.microsoft.com").openStream().close();
			Packet ping=captor.getPacket();
			if(ping==null){
				System.out.println("cannot obtain MAC address of default gateway.");
				System.exit(-1);
			}else if(Arrays.equals(((EthernetPacket)ping.datalink).dst_mac,device.mac_address))
					continue;
			gwmac=((EthernetPacket)ping.datalink).dst_mac;
			break;
		}
		
		DHCP dhcp = new DHCP();
		dhcp.setIPv4Parameter(0, false, false, false, 0, false, false, false, 0, 0, 64, IPPacket.IPPROTO_ICMP,
				thisIP, InetAddress.getByName("192.168.1.1"));
		
		EthernetPacket ether=new EthernetPacket();
		ether.frametype=EthernetPacket.ETHERTYPE_IP;
		ether.src_mac=device.mac_address;
		ether.dst_mac=gwmac;
		dhcp.datalink=ether;
		
		captor.setFilter("icmp and dst host "+thisIP.getHostAddress(),true);
		JpcapSender sender=captor.getJpcapSenderInstance();
		//JpcapSender sender=JpcapSender.openDevice(device);
		
		sender.sendPacket(dhcp.forgePacket());
	}

}
