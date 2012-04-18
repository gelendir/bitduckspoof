package org.bitducks.spoofing.gui.serviceView;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.BroadcastARPService;

public class BroadcastARPServiceView extends View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static private String TITLE = "ARP Broadcast Service";
	final static private String EXPLANATION = "This service will arp poison the cache of all the user on the network to make them think you're the gateway.";

	public BroadcastARPServiceView() {
		super( BroadcastARPServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel( BroadcastARPServiceView.EXPLANATION );
		
		this.servicePanel.add( descLabel, BorderLayout.CENTER );
			
	}

	
	@Override
	protected Service createService() {
		return new BroadcastARPService();
	}

}
