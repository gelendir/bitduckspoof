package org.bitducks.spoofing.main;
import java.io.IOException;

import jpcap.JpcapCaptor;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.services.BroadcastARPService;

public class ThisIsAMain {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		Server.createInstance(JpcapCaptor.getDeviceList()[0]);
		Server.getInstance().addService(new BroadcastARPService());
		Thread.sleep(10000);
		Server.getInstance().stopServer();
	}

}


