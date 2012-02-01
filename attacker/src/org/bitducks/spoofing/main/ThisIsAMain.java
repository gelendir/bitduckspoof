package org.bitducks.spoofing.main;
import java.io.IOException;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.services.RogueDHCPService;

public class ThisIsAMain {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Server.createInstance(JpcapCaptor.getDeviceList()[ 0 ]);
		Service service = new RogueDHCPService();
		
		Server.getInstance().addService(service);
		
		Server.getInstance().start();
		
		// NEVER END !!
		Server.getInstance().join();
	}

}


