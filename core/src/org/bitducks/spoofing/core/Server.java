package org.bitducks.spoofing.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.Packet;

/**
 * This class manage all the services by sending packet to the 
 * services and sending packet to the network. It is possible to
 * add service anytime in the Server's life. This class is habitually
 * a separated thread.
 * @author Frédérik Paradis
 */
public class Server extends Thread {
	/**
	 * The list of the services
	 */
	private List<Service> services = Collections.synchronizedList(new LinkedList<Service>());
	
	/**
	 * Indicates to the thread of Server to stop if its
	 * value is false.
	 */
	private volatile boolean active = false;
	
	/**
	 * Used to send packet to the network.
	 */
	private JpcapSender sender = null;
	
	
	/**
	 * Information about the chosen interface.
	 */
	private InterfaceInfo info;

	/**
	 * Singleton instance of this class.
	 */
	private static Server instance = null;

	/**
	 * Initialize the Singleton and open the network interface.
	 * @param networkInterface The interface chosen by the user 
	 * @throws IOException
	 */
	public static void createInstance(NetworkInterface networkInterface) throws IOException {
		Server.instance = new Server(networkInterface);
	}

	/**
	 * This static method return the singleton instance of 
	 * this class. If the singleton have not been created, 
	 * null is returned.
	 * @return Return the singleton instance of 
	 * this class. If the singleton have not been created, 
	 * null is returned.
	 */
	public static Server getInstance() {
		//TODO:Throw exception if not created... not a NPE
		return Server.instance;
	}

	/**
	 * The constructor initialize the object and open the network interface.
	 * @param networkInterface The interface chosen by the user 
	 * @throws IOException
	 */
	private Server(NetworkInterface networkInterface) throws IOException {
		this.sender = JpcapSender.openDevice(networkInterface);
		this.info = new InterfaceInfo(networkInterface);
	}

	/**
	 * This method add a service to the server and start it if
	 * the server is already started.
	 * @param service The service to add.
	 */
	public void addService(Service service) {
		if (this.isAlive() && !service.isAlive()) {
			service.start();
		}
		this.services.add(service);
	}

	/**
	 * This method remove a service to this server and
	 * send a close request to this service.
	 * @param service The service to remove.
	 */
	public void removeService(Service service) {
		this.services.remove(service);
		service.closeService();
	}

	/**
	 * This method start the thread of this server then
	 * start the thread of the services.
	 */
	@Override
	public void start() {
		this.active = true;
		// Start the thread
		super.start();

		synchronized(this.services) {
			// Start all the service
			for (Service s : this.services ) {
				if (!s.isAlive()) {
					s.start();
				}
			}
		}
	}

	/**
	 * This method send a signal to server thread to
	 * close then send a signal to the service thread
	 * to close.
	 */
	public void stopServer() {
		this.active = false;

		synchronized(this.services) {
			// End all the service
			for (Service s : this.services ) {
				s.closeService();
			}
		}
	}

	/**
	 * This method is the main function of the thread.
	 * It get packet from JPcap library and send it to
	 * the services by checking their policy.
	 */
	@Override
	public void run() {
		JpcapCaptor captor = null;

		try {
			captor = JpcapCaptor.openDevice(this.info.getDevice(),2000,true, 20);
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

	/**
	 * This method get a packet from a JpcapCaptor and 
	 * send it to the services by checking their policy.
	 * @param captor The JpcapCaptor
	 */
	private void pushPacketToService(JpcapCaptor captor) {
		Packet packet = captor.getPacket();
		if (packet != null) {

			synchronized(this.services) {
				// Check for each service to know if it match with the policy
				for (Service s : this.services ) {
					if (s.getPolicy().checkIfPolicyValid(packet)) {
						// It's matching, so we push it to the service's paquetQueue.
						s.pushPacket(packet);
					}
				}
			}
		}		
	}

	/**
	 * This method send a packet to the network.
	 * @param packet The packet to send.
	 */
	public void sendPacket(Packet packet) {
		this.sender.sendPacket(packet);
	}

	/**
	 * This method return an instance of InterfaceInfo which
	 * have many information about the interface chosen by the 
	 * user.
	 * @return Return an instance of InterfaceInfo
	 */
	public InterfaceInfo getInfo() {
		return this.info;
	}

	/**
	 * This method return all services of this server.
	 * @return Return all services of this server.
	 */
	public ArrayList<Service> getServices() {
		ArrayList<Service> services = new ArrayList<Service>();
		synchronized(this.services) {
			for( Service service: this.services ) {
				services.add( service );
			}
		}
		return services;

	}

}
