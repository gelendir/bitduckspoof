package org.bitducks.spoofing.main;

import java.io.IOException;

import jpcap.JpcapCaptor;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.services.DNSService;

public class testSimon {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		Server.createInstance(JpcapCaptor.getDeviceList()[ 0 ]);
		Service service = new DNSService();
		
		Server.getInstance().addService(service);
		
		Server.getInstance().start();
		
		// NEVER END !!
		Server.getInstance().join();
	}

}
