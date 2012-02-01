package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.util.Collection;

public interface ArpScanFinish {
	
	public void scanFinished( Collection<InetAddress> addresses );
	

}
