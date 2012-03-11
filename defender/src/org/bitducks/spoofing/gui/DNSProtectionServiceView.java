package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.services.DNSProtectionService;

public class DNSProtectionServiceView extends View {
	
	final public static String TITLE = "DNS spoof detection";
	final public static String EXPLANATION = "Scans the network for DNS spoofing, adds the spoofers to the log.";

	public DNSProtectionServiceView() {
		super( DNSProtectionServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel(DNSProtectionServiceView.EXPLANATION );
		
		this.servicePanel.add( descLabel, BorderLayout.CENTER );
			
	}

	@Override
	protected Service createService() {
		return new DNSProtectionService();
	}

}