package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import jpcap.NetworkInterface;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.gui.serviceView.BroadcastARPServiceView;
import org.bitducks.spoofing.gui.serviceView.DNSServiceView;
import org.bitducks.spoofing.gui.serviceView.IPStealerServiceView;
import org.bitducks.spoofing.gui.serviceView.ARPReplySpoofingView;
import org.bitducks.spoofing.gui.serviceView.RogueDHCPServiceView;

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
		
		View view = new DNSServiceView();
		tab.addTab(view.getTitle(), view);
		
		view = new IPStealerServiceView();
		tab.addTab(view.getTitle(), view);
		
		view = new RogueDHCPServiceView();
		tab.addTab(view.getTitle(), view);
		
		view = new BroadcastARPServiceView();
		tab.addTab(view.getTitle(), view);
		
		view = new ARPReplySpoofingView();
		tab.addTab(view.getTitle(), view);
		
		LogView logView = new LogView();
		tab.addTab("Logs", logView);

		this.add(tab, BorderLayout.CENTER);
	}
}
