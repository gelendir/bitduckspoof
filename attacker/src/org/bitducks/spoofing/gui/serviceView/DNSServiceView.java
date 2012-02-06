package org.bitducks.spoofing.gui.serviceView;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.DNSService;

public class DNSServiceView extends View {

	public DNSServiceView() {
		super("DNS Poisoning", new DNSService());
		
		setUpServicePanel();
	}
	
	public void setUpServicePanel() {
		this.servicePanel.add(new JButton("LOLOLOLOLOLOLOLO"));
	}
}
