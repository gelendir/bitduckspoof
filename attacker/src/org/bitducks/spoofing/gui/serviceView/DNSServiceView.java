package org.bitducks.spoofing.gui.serviceView;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.bitducks.spoofing.gui.View;

public class DNSServiceView extends View {

	public DNSServiceView() {
		this.title = "DNS Poisoning";
		
		JLabel button = new JLabel( "Button" );
		//button.setSize(100, 100);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(button);
	}
}
