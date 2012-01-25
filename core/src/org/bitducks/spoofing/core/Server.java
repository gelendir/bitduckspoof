package org.bitducks.spoofing.core;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;

public class Server extends Thread {
	private List<Service> services = Collections.synchronizedList(new LinkedList<Service>());
	private List<Packet> sendingQueue = Collections.synchronizedList(new LinkedList<Packet>());
	private volatile boolean active = false;

	public void addService(Service service) {
		service.start();
		this.services.add(service);
	}

	public void removeService(Service service) {
		this.services.remove(service);
		service.closeService();
	}

	@Override
	public void start() {
		// Start all the service
		for (Service s : this.services ) {
			s.start();
		}

		this.active = true;
		// Start the thread
		super.start();
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
		NetworkInterface device = JpcapCaptor.getDeviceList()[0];
		JpcapCaptor captor = null;
		JpcapSender sender = null;
		try {
			captor = JpcapCaptor.openDevice(device,2000,true, 20);
			sender = JpcapSender.openDevice(device);
		} catch (IOException e) {
			// TODO Error handling
			e.printStackTrace();
			return;
		}

		captor.setNonBlockingMode(false);

		// Main loop
		while (this.active) {
			// Should we do 2 thread for these call ??
			// What if the loop is stuck into read and never get to read
			// What if we pass thing we should read
			this.pushPacketToService(captor);
			this.readPacketFromService(sender);
			
		}

		captor.close();
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
	
	private void readPacketFromService(JpcapSender sender) {
		// We send all the packet in the sending queue
		for (Packet p : this.sendingQueue) {
			sender.sendPacket(p);
			this.sendingQueue.remove(p);
		}
	}
	
	public void sendPacket(Packet p) {
		this.sendingQueue.add(p);
	}
}
