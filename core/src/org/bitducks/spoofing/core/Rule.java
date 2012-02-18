package org.bitducks.spoofing.core;

import jpcap.packet.Packet;

/**
 * This class is use to implement a rule for
 * receiving the wanted packet in services.
 * @author Frédérik Paradis
 */
public abstract class Rule {
	
	/**
	 * This method return true if the packet satisfy the rule;
	 * false otherwise.
	 * @param p The packet to examine
	 * @return Return true if the packet satisfy the rule;
	 * false otherwise.
	 */
	public abstract boolean checkRule(Packet p);
}
