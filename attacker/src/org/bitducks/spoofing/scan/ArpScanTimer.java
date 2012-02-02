package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TimerTask;

import org.bitducks.spoofing.exception.UnexpectedErrorException;
import org.bitducks.spoofing.services.dhcprogue.RogueDHCPService;

public class ArpScanTimer extends TimerTask {
	
	private Collection<InetAddress> addresses;
	private ArpScanService arpScan;
	private ArpRecieveService reciever;
	private ArpScanFinish finish;

	public ArpScanTimer( Collection<InetAddress> addresses, 
			ArpScanService arpScan, 
			ArpRecieveService reciever,
			ArpScanFinish finish ) {
		this.addresses = addresses;
		this.arpScan = arpScan;
		this.reciever = reciever;
		this.finish = finish;
		
	}

	@Override
	public void run() {
		
		this.arpScan.runScan( this.addresses );
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new UnexpectedErrorException( e, "error while sleeping in ArpScanTimer" );
		}
		
		LinkedList<InetAddress> noResponse = new LinkedList<InetAddress>();
		
		for( InetAddress address: this.addresses ) {
			if( !this.reciever.getCache().hasAddress( address, RogueDHCPService.TIME_TO_CHECK_IP ) ) {
				noResponse.add( address );
			}
		}
		
		finish.scanFinished( noResponse );
		
	}

}
