package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.services.RogueDHCPDetectionService;

public class RogueDHCPDetectionServiceView extends View {

	final public static String TITLE = "Rogue DHCP Detection";
	final public static String EXPLANATION = "Rogue DHCP Detection is used to detect rogue DHCP servers " +
			"by sending a DHCP discover and comparing the offers with the available " +
			"trusted DHCP servers in the list bellow.";
	
	public RogueDHCPDetectionServiceView() {
		super( RogueDHCPDetectionServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel(RogueDHCPDetectionServiceView.EXPLANATION );
		
		this.servicePanel.add( descLabel, BorderLayout.CENTER );
			
	}
	
	@Override
	protected Service createService() {
		return new RogueDHCPDetectionService(null);
	}

}
