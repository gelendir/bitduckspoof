package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.services.ARPReplyRateService;

public class ARPReplyRateServiceView extends View {

	final public static String TITLE = "ARP Reply Rate";
	final public static String EXPLANATION = "ARP Reply Rate is a detector of ARP " +
			"spoofing. It calculates the rate between the number of the ARP replies " +
			"and ARP requests received. If there is more than one ARP reply for an " +
			"IP address, this means that this IP address may be spoofed.";
	
	public ARPReplyRateServiceView() {
		super( ARPReplyRateServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel(ARPReplyRateServiceView.EXPLANATION );
		
		this.servicePanel.add( descLabel, BorderLayout.CENTER );
			
	}
	
	@Override
	protected Service createService() {
		return new ARPReplyRateService(10);
	}

}
