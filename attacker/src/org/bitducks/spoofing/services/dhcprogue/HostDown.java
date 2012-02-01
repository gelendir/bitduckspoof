package org.bitducks.spoofing.services.dhcprogue;
/*
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TimerTask;

import jpcap.JpcapCaptor;
import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.InterfaceInfo;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.packet.PacketFactory;

public class HostDown extends TimerTask {
	private Collection<InetAddress> hosts;
	private RogueDHCPService rogue;
	
	public HostDown(RogueDHCPService rogue, Collection<InetAddress> givenAddress) {
		this.rogue = rogue;
		this.hosts = new ArrayList<InetAddress>(givenAddress);
	}
	
	@Override
	public void run() {
		try {
			JpcapCaptor captor = JpcapCaptor.openDevice(Server.getInstance().getNetworkInterface(), 65000, true, 3000);
			
			LinkedList<InetAddress> toDelete = new LinkedList<InetAddress>();
			for(InetAddress addr : hosts) {
				captor.setFilter("arp[6:2] == 2 && arp src " + addr.getHostAddress(), false);
				
				InterfaceInfo info = Server.getInstance().getInfo();
				
				ARPPacket arp = PacketFactory.arpRequest( info.getMacAddress(),
						info.getAddress(),
						addr);
				
				Server.getInstance().sendPacket(arp);
				Packet p = captor.getPacket();
				if(p == null) {
					toDelete.add(addr);
				}
			}
			
			this.rogue.addFreeAddress(toDelete);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
*/