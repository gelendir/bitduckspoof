package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.exception.UnexpectedErrorException;
import org.bitducks.spoofing.services.arp.ArpCache;

public class ARPProtectionService extends Service {

	/**
	 * The ArpScanService to scan all network before the start of the service.
	 */
	private ArpScanService arpScan = new ArpScanService();

	/**
	 * The ArpReceiveService to receive the ARP request sent by the ArpScanService.
	 */
	private ArpRecieveService receiver = new ArpRecieveService();
	
	private ArpCache cache;

	public ARPProtectionService() {
		Server.getInstance().addService(this.receiver);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Server.getInstance().addService(this.arpScan);
	}
	
	@Override
	public void run() {
		this.scanNetwork();
		
		while(!this.isCloseRequested()) {
			
		}
	}
	
	private void scanNetwork() {
		this.arpScan.runNetworkScan();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new UnexpectedErrorException( e, "error while sleeping in ArpScanTimer" );
		}
		Collection<InetAddress> allAdresses = this.receiver.getCache().allAdresses();
		this.cache = new ArpCache();
		for(InetAddress addr : allAdresses) {
			this.cache.add(addr, this.receiver.getCache().getMac(addr));
		}
		
		Server.getInstance().removeService(this.arpScan);
		Server.getInstance().removeService(this.receiver);
		
		System.out.println(this.cache);
	}

}
