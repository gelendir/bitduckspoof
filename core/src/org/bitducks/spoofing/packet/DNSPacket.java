package org.bitducks.spoofing.packet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.bitducks.spoofing.util.Constants;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

import jpcap.packet.UDPPacket;

public class DNSPacket extends UDPPacket {

	public DNSPacket(int src_port, int dst_port) {
		super(dst_port, src_port);
	}
	
	/**
	 * Build the ip header
	 * @param macSource
	 * @param macTarget
	 * @param ipSource
	 * @param ipTarget
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
	

}
