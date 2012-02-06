package org.bitducks.spoofing.gui.serviceView;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.DNSService;

public class DNSServiceView extends View {

	public DNSServiceView() {
		super("DNS Poisoning");
		
		setUpServicePanel();
	}
	
	public void setUpServicePanel() {
		this.servicePanel.add(new JButton("LOLOLOLOLOLOLOLO"));
	}

	@Override
	protected Service createService() {
		return new DNSService();
	}
}
