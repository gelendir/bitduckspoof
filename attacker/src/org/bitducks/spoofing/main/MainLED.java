package org.bitducks.spoofing.main;

import java.io.IOException;

import jpcap.JpcapCaptor;

import org.apache.log4j.BasicConfigurator;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.services.RedirectNAT;

public class MainLED {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure();
		
		Server.createInstance(JpcapCaptor.getDeviceList()[0]);
		Server.getInstance().addService(new RedirectNAT());
		
		Server.getInstance().start();
		Server.getInstance().join();
		Server.getInstance().stopServer();
		
	}

}
