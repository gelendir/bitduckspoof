package org.bitducks.spoofing.util.dhcp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;

public class DHCP extends UDPPacket {
	
	private static final long serialVersionUID = 1L;
	
	private ByteBuffer b;
	private static final int HEADER_LENGTH = 236;

	private DHCPMessageType opcode = DHCPMessageType.BOOTREQUEST;
	private byte htype = 1;
	private byte hlen = 6;
	private byte hops = 0;
	private int xid = 0;
	private short secs = 0;
	private short flags = 0;
	private byte[] ciaddr = new byte[4];
	private byte[] yiaddr = new byte[4];
	private byte[] siaddr = new byte[4];
	private byte[] giaddr = new byte[4];
	private byte[] chiaddr = new byte[16];
	private byte[] sname = new byte[64];
	private byte[] file = new byte[128];

	private ArrayList<DHCPOption> options = new ArrayList<DHCPOption>();

	public DHCP() {
		super(67, 68);
	}

	public Packet forgePacket() {

		int opLength = this.getOptionsLength();
		byte[] options = this.getOptionsData(opLength);

		this.b = ByteBuffer.allocate(DHCP.HEADER_LENGTH + opLength);

		this.b.put(this.opcode.getOpcode());
		this.b.put(this.htype);
		this.b.put(this.hlen);
		this.b.put(this.hops);
		this.b.putInt(this.xid);
		this.b.putShort(this.secs);
		this.b.putShort(this.flags);
		this.b.put(this.ciaddr);
		this.b.put(this.yiaddr);
		this.b.put(this.siaddr);
		this.b.put(this.giaddr);
		this.b.put(this.chiaddr);
		this.b.put(this.sname);
		this.b.put(this.file);

		this.b.put(options);

		this.data = b.array();
		
		return this;
	}

	public DHCPMessageType getOpcode() {
		return this.opcode;
	}

	public void setOpcode(DHCPMessageType opcode) {
		this.opcode = opcode;
	}

	public int getXid() {
		return this.xid;
	}

	public void setXid(int xid) {
		this.xid = xid;
	}

	public short getSecs() {
		return this.secs;
	}

	public void setSecs(short secs) {
		this.secs = secs;
	}

	public String getSname() {
		return new String(this.sname);
	}

	public void setSname(String sname) {
		this.sname = Arrays.copyOf(sname.getBytes(), 64);
		this.sname[63] = 0;
	}

	public byte[] getCiaddr() {
		return this.ciaddr;
	}

	public void setCiaddr(byte[] ciaddr) {
		this.ciaddr = DHCP.getIPAddress(ciaddr);
	}

	public byte[] getYiaddr() {
		return this.yiaddr;
	}

	public void setYiaddr(byte[] yiaddr) {
		this.yiaddr = DHCP.getIPAddress(yiaddr);
	}

	public byte[] getSiaddr() {
		return this.siaddr;
	}

	public void setSiaddr(byte[] siaddr) {
		this.siaddr = DHCP.getIPAddress(siaddr);
	}

	public byte[] getGiaddr() {
		return this.giaddr;
	}

	public void setGiaddr(byte[] giaddr) {
		this.giaddr = DHCP.getIPAddress(giaddr);
	}

	private static byte[] getIPAddress(byte[] addr) {
		return Arrays.copyOf(addr, 4);
	}

	private int getOptionsLength() {
		int length = 0;
		for (DHCPOption option : this.options) {
			if (option.getDataLength() == 0) {
				length += 1;
			} else {
				length += 2 + option.getDataLength();
			}
		}

		return length;
	}

	private byte[] getOptionsData(int length) {
		ByteBuffer buf = ByteBuffer.allocate(length);

		for (DHCPOption option : this.options) {
			buf.put(option.getCode());
			if (option.getDataLength() != 0) {
				buf.put(option.getDataLength());
				buf.put(option.getDataBytes());
			}
		}

		return buf.array();
	}
}
