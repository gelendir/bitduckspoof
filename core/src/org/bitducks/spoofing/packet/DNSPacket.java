package org.bitducks.spoofing.packet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.bitducks.spoofing.util.Constants;

import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

public class DNSPacket extends UDPPacket {

	public DNSPacket(int src_port, int dst_port) {
		super(dst_port, src_port);
	}
	
	public DNSPacket(UDPPacket packet) {
		super(packet.dst_port, packet.src_port);
		this.data = Arrays.copyOf( packet.data, packet.data.length );
		this.datalink = packet.datalink;
	}
	
	/**
	 * Build the ip header that will be used in a DNS packet.
	 * 
	 * @param ipSource Source IP of the packet
	 * @param ipTarget Target IP of the packet
	 * @param identifier DNS identifier for the packet
	 */
	public void buildIpDNSPacket(InetAddress ipSource, InetAddress ipTarget, int identifier) {

				this.setIPv4Parameter(0, 
						false, 
						false, 
						false, 
						0, 
						false, 
						false, 
						false, 
						0, 
						identifier,	// Identifier
						64, 		// TTL
						17,   		// Protocol
						ipTarget, 
						ipSource);
	}	
	
	
	/** 
	 * Will fill the data part of the UDP packet
	 * @param ipFalseDNS
	 */
	public void buildDataDNSPacket(byte[] transactionId, byte[] questions, byte[] query, InetAddress ipFalseDNS) {
		ByteBuffer buffer = ByteBuffer.allocate(28 + query.length);
		
		
		buffer.put(transactionId);	// Transaction idByteBuffer.allocate(1)
		buffer.put(new byte[]{ (byte) 0x81, (byte)0x80 });	// Flags (query response)
		buffer.put(questions);		// Question
		buffer.put(new byte[]{ (byte) 0x00, (byte)0x01 });	// Answer
		buffer.put(new byte[]{ (byte) 0x00, (byte)0x00 });	// Authority RRS
		buffer.put(new byte[]{ (byte) 0x00, (byte)0x00 });	// Additional RRS
		
		// Query
		buffer.put(query);		// Query
		
		// Answer
		buffer.put(new byte[]{ (byte) 0xc0, (byte)0x0c });	// Name
		int queryLenght = query.length;
		buffer.put(new byte[]{ query[queryLenght - 4], query[queryLenght - 3] }); // Type
		buffer.put(new byte[]{ query[queryLenght - 2], query[queryLenght - 1] }); // Class
		buffer.put(new byte[]{ (byte) 0x00, (byte)0x00, (byte) 0x3F, (byte) 0x93  });	// TTL
		buffer.put(new byte[]{ (byte) 0x00, (byte)0x04 }); // Data Length
		buffer.put(ipFalseDNS.getAddress()); // Address
		
		this.data = buffer.array();
		
	}
	
	public String getDomainName() {
		
		
		ArrayList<Byte> query = new ArrayList<Byte>();
		
		//skip the packet header (first 12 bytes)
		int pos = 13;
		byte block = this.data[pos];
		
		while( block != 0x00 && pos < this.data.length ) {
			
			//If byte is over 0x21, then it's an ASCII character and
			//we can add the byte to the string as-is
			if (block >= 0x21) { 
				query.add(block);		
			} else {
				//Otherwise add the ASCII dot character
				query.add((byte) 0x2e);
			}
			
			++pos;
			block = this.data[pos];
		}
		
		
		//Convert ArrayList query to string
		byte[] buffer = new byte[query.size()];
		for( int i = 0; i < buffer.length; ++i ) {
			buffer[i] = query.get(i);
		}

		return new String(buffer);
	}
	

}
