package org.bitducks.spoofing.core;

import jpcap.packet.Packet;

public abstract class Rule {
	public abstract boolean checkRule(Packet p);
}
