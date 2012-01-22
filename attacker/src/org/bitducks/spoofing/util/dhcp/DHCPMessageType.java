package org.bitducks.spoofing.util.dhcp;

public enum DHCPMessageType {
	BOOTREQUEST((byte)1),
	BOOTREPLY((byte)2);
	
	private byte opcode;
	
	private DHCPMessageType(byte opcode) {
		this.opcode = opcode;
	}

	public byte getOpcode() {
		return opcode;
	}
}
