package org.bitducks.spoofing.gui.serviceView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.IpStealer;

public class IPStealerServiceView extends View {
	
	final static private String TITLE = "IP Stealer";
	final static private String EXPLANATION = "This service will launch a DHCP starvation attack in the network.";

	public IPStealerServiceView() {
		super( IPStealerServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel(IPStealerServiceView.EXPLANATION );
		
		this.servicePanel.add( descLabel, BorderLayout.CENTER );
			
	}

	@Override
	protected Service createService() {
		return new IpStealer();
	}
	

}