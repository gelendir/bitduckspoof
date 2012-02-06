package org.bitducks.spoofing.core;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.Packet;

public class Server extends Thread {
	private List<Service> services = Collections.synchronizedList(new LinkedList<Service>());
	private volatile boolean active = false;
	private JpcapSender sender = null;
	
	private NetworkInterface networkInterface;
	private InterfaceInfo info;
	
	private static Server instance = null;
	
	public static void createInstance(NetworkInterface networkInterface) throws IOException {
		Server.instance = new Server(networkInterface);
	}
	
	public static Server getInstance() {
		//TODO:Throw exception if not created... not a NPE
			return Server.instance;
	}
	
	private Server(NetworkInterface networkInterface) throws IOException {
		this.networkInterface = networkInterface;
		this.sender = JpcapSender.openDevice(networkInterface);
		this.info = new InterfaceInfo(this.networkInterface);
	}

	public void addService(Service service) {
		if (this.isAlive() && !service.isAlive()) {
			service.start();
		}
		this.services.add(service);
	}

	public void removeService(Service service) {
		this.services.remove(service);
		service.closeService();
	}

	@Override
	public void start() {
		this.active = true;
		// Start the thread
		super.start();
		
		// Start all the service
		for (Service s : this.services ) {
			if (!s.isAlive()) {
				s.start();
			}
		}
	}

	public void stopServer() {
		this.active = false;

		// End all the service
		for (Service s : this.services ) {
			s.closeService();
		}
	}

	@Override
	public void run() {		
		// TODO Set the right interface
		NetworkInterface device = this.networkInterface;
		JpcapCaptor captor = null;
		
		try {
			captor = JpcapCaptor.openDevice(device,2000,true, 20);
		} catch (IOException e) {
			// TODO Error handling
			e.printStackTrace();
			return;
		}

		captor.setNonBlockingMode(false);

		// Main loop
		while (this.active) {
			this.pushPacketToService(captor);
			
		}

		captor.close();
		System.out.println("end");
	}
	
	private void pushPacketToService(JpcapCaptor captor) {
		Packet packet = captor.getPacket();
		if (packet != null) {

			// Check for each service to know if it match with the policy
			for (Service s : this.services ) {
				if (s.getPolicy().checkIfPolicyValid(packet)) {
					// It's matching, so we push it to the service's paquetQueue.
					s.pushPacket(packet);
				}
			}
		}		
	}
	
	public void sendPacket(Packet packet) {
		sender.sendPacket(packet);
	}
	
	
	public NetworkInterface getNetworkInterface() {
		return this.networkInterface;
	}
	
	public InterfaceInfo getInfo() {
		return this.info;
	}
	
}
