package org.bitducks.spoofing.services;

/*import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.core.rules.ARPRule;

public class RegularArpService extends Service {

	private Timer timer = new Timer();
	private List<InetAddress> active;
	private ArpTask task;
	private RogueDHCPService dhcpService;
	
	public RegularArpService(RogueDHCPService dhcpService, List<InetAddress> active) {
		this.dhcpService = dhcpService;
		this.active = active;
		this.task = new ArpTask(this, this.active);
		
		this.getPolicy().addRule(new ARPRule());
	}
	
	@Override
	public void run() {
		this.timer.schedule(this.task, 1000 * 60 * 10);
		while(!this.isCloseRequested()) {
			Packet packet = this.getNextPacket();
			if(packet != null) {
				
			}
		}
		
		this.timer.cancel();
	}
	
	public void removeNotActive() {
		for(InetAddress ip : this.task.getNotActiveClient()) {
			this.active.remove(ip);
			this.dhcpService.addFreeAddress(ip);
		}
	}

}*/
