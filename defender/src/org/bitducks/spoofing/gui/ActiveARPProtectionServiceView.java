package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.services.ActiveARPProtectionService;

public class ActiveARPProtectionServiceView extends View {

	final public static String TITLE = "Active ARP Protection";
	final public static String EXPLANATION = "Scans the network for ARP reply and when when the " +
			"service receives an ARP reply, it will send a request to verify if the reply was " +
			"authentic or not.";
	
	public ActiveARPProtectionServiceView() {
		super( ActiveARPProtectionServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel(ActiveARPProtectionServiceView.EXPLANATION);
		
		this.servicePanel.add(descLabel, BorderLayout.CENTER);
		
	}
	
	@Override
	protected Service createService() {
		return new ActiveARPProtectionService();
	}

}
