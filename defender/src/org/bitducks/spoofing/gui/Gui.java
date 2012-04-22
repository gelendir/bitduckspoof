package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.gui.serviceView.ARPReplyRateServiceView;
import org.bitducks.spoofing.gui.serviceView.ActiveARPProtectionServiceView;
import org.bitducks.spoofing.gui.serviceView.DNSProtectionServiceView;
import org.bitducks.spoofing.gui.serviceView.RogueDHCPDetectionServiceView;

import jpcap.NetworkInterface;

/**
 * The main Ui witch contain tab of views.
 * @author Simon Perreault
 *
 */
public class Gui extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tab = new JTabbedPane(JTabbedPane.NORTH);
	
	public Gui(NetworkInterface myInterface) throws IOException {

		if( Server.getInstance() == null ) {
			Server.createInstance(myInterface);
			Server.getInstance().start();
		}
		
		this.setUpUi();
		this.pack();
	}

	/**
	 * Setup the Tab for the main Ui
	 */
	private void setUpUi() {
		
		View view = new DNSProtectionServiceView();
		this.tab.addTab( view.getTitle() , view);
		this.add(this.tab, BorderLayout.CENTER);
		
		view = new ActiveARPProtectionServiceView();
		this.tab.addTab( view.getTitle() , view);
		this.add(this.tab, BorderLayout.CENTER);
		
		view = new ARPReplyRateServiceView();
		this.tab.addTab( view.getTitle() , view);
		this.add(this.tab, BorderLayout.CENTER);
		
		view = new RogueDHCPDetectionServiceView();
		this.tab.addTab( view.getTitle() , view);
		this.add(this.tab, BorderLayout.CENTER);
		
	}

}
