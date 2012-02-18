package org.bitducks.spoofing.services;

import java.net.InetAddress;

import jpcap.packet.ARPPacket;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.SingleARPResponseRule;
import org.bitducks.spoofing.exception.UnexpectedErrorException;
import org.bitducks.spoofing.packet.PacketGenerator;

public class MacFindService extends Service {
	
	private InetAddress ipAddress = null;
	private byte[] macAddress = null;
	
	public MacFindService( InetAddress ipAddress ) {
		
		this.ipAddress = ipAddress;
		
		this.getPolicy().addRule(
				new SingleARPResponseRule( this.ipAddress ) );
	}
	
	@Override
	public void run() {
		
		Server server = Server.getInstance();
		
		PacketGenerator generator = new PacketGenerator( Server.getInstance().getInfo().getDevice() );
		ARPPacket request = generator.arpRequest( this.ipAddress );
		
		server.sendPacket( request );
		
		ARPPacket response = (ARPPacket)this.getNextBlockingPacket();
		
		this.macAddress = response.sender_hardaddr;
		
		synchronized( this ) {
			this.notify();
		}
		
	}
	
	public byte[] getMacAddress() {
		
		synchronized( this ) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				throw new UnexpectedErrorException(e, "error waiting for gateway mac address");
			}
		}
		
		return this.macAddress;
	}

}
