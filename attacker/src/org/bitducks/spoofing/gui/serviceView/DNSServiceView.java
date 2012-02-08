package org.bitducks.spoofing.gui.serviceView;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.DNSService;

public class DNSServiceView extends View {
	
	JList<String> list = new JList<String>();

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
