package org.bitducks.spoofing.util;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;

public class IllegalAddressDetection {

	public static Collection<InetAddress> getIllegalAddress(Collection<InetAddress> good, Collection<InetAddress> all) {
		Collection<InetAddress> ret = new HashSet<InetAddress>(all);
		ret.removeAll(good);
		return ret;
	}
}
