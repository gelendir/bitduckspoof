package org.bitducks.spoofing.scan;

import java.net.InetAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TimerTask;

import org.bitducks.spoofing.exception.UnexpectedErrorException;
import org.bitducks.spoofing.services.ArpRecieveService;
import org.bitducks.spoofing.services.ArpScanService;
import org.bitducks.spoofing.services.RogueDHCPService;
import org.bitducks.spoofing.services.arp.ArpScanFinish;

/**
 * This class aims to scan a collection of InetAddress
 * with ARP request. This class is habitually used by a
 * timer.
 * @author Frédérik Paradis
 *
 */
public class ArpScanTimer extends TimerTask {
	
	/**
	 * The collection of IP address to scan.
	 */
	private Collection<InetAddress> addresses;
	
	/**
	 * The ArpScanService used by this class.
	 */
	private ArpScanService arpScan;
	
	/**
	 * The ArpRecieveService used by this class.
	 */
	private ArpRecieveService reciever;
	
	/**
	 * The object to which we send the result of the scan.
	 */
	private ArpScanFinish finish;

	/**
	 * This constructor initialize the object with
	 * the parameters. 
	 * @param addresses The collection of address to scan
	 * @param arpScan The ArpScanService used by this class.
	 * @param reciever The ArpRecieveService used by this class.
	 * @param finish The object to which we send the result of the scan.
	 */
	public ArpScanTimer( Collection<InetAddress> addresses, 
			ArpScanService arpScan, 
			ArpRecieveService reciever,
			ArpScanFinish finish ) {
		this.addresses = addresses;
		this.arpScan = arpScan;
		this.reciever = reciever;
		this.finish = finish;
		
	}

	/**
	 * This method is called by the timer. It scan the IP addresses given
	 * in the constructor and send to the ArpScanFinish given the IP
	 * addresses who has not answered. The call of this method may take 
	 * few seconds.
	 */
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
		
		this.finish.scanFinished( noResponse );
		
	}

}
