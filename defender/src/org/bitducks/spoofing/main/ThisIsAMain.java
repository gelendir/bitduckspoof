package org.bitducks.spoofing.main;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import jpcap.JpcapCaptor;

import org.apache.log4j.BasicConfigurator;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.services.DNSProtection;
import org.bitducks.spoofing.services.RogueDHCPDetectionService;

public class ThisIsAMain {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure();
		
		Server.createInstance(JpcapCaptor.getDeviceList()[0]);
		
		/*Server.getInstance().start();
		Thread.sleep(500);
		
		ArrayList<InetAddress> addr = new ArrayList<InetAddress>();
		//addr.add(InetAddress.getByName("10.17.65.2"));
		
		RogueDHCPDetectionService service = new RogueDHCPDetectionService(addr);
		Server.getInstance().addService(service);
		
		Server.getInstance().join();*/
		
		Server.getInstance().start();
		
		Server.getInstance().addService(new DNSProtection());
		
		Server.getInstance().join();
		
	}

}
