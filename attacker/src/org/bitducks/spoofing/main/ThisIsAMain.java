package org.bitducks.spoofing.main;
import java.io.IOException;

import jpcap.JpcapCaptor;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.services.RogueDHCPService;

public class ThisIsAMain {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		Server.createInstance(JpcapCaptor.getDeviceList()[1]);
		
		Server.getInstance().start();
		Server.getInstance().addService(new RogueDHCPService());
		
		Server.getInstance().join();
	}

}


