package org.bitducks.spoofing.gui.serviceView;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.RogueDHCPService;

public class RogueDHCPServiceView extends View {

	final static private String TITLE = "Rogue DHCP";
	final static private String EXPLANATION = "This service will launch a Rogue DHCP on the network.";

	public RogueDHCPServiceView() {
		super( RogueDHCPServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel( RogueDHCPServiceView.EXPLANATION );
		
		this.servicePanel.add( descLabel, BorderLayout.CENTER );
			
	}

	@Override
	protected Service createService() {
		return new RogueDHCPService();
	}
}
