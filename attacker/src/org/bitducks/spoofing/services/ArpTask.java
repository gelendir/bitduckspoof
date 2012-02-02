package org.bitducks.spoofing.services;

/*import java.net.InetAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

public class ArpTask extends TimerTask {
	
	private List<InetAddress> active;
	private Set<InetAddress> notActive;
	private RegularArpService service;
	
	public ArpTask(RegularArpService service, List<InetAddress> active) {
		this.service = service;
		this.active = active;
		this.notActive = Collections.synchronizedSet(new HashSet<InetAddress>(this.active));
	}
	
	@Override
	public void run() {
		this.service.removeNotActive();
		
		//
	}
	
	public void addActiveClient(InetAddress addr) {
		this.notActive.remove(addr);
	}
	
	public List<InetAddress> getNotActiveClient() {
		List<InetAddress> ret = new LinkedList<InetAddress>();
		for(InetAddress addr : this.notActive) {
			ret.add(addr);
		}
		
		return ret;
	}
}*/
