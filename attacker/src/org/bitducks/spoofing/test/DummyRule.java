package org.bitducks.spoofing.test;

import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Rule;

public class DummyRule extends Rule {

	@Override
	public boolean checkRule(Packet p) {
		return true;
	}

}
