package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import jpcap.NetworkInterface;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.gui.serviceView.DNSServiceView;

public class Gui extends JFrame {
	
	private JTabbedPane tab = new JTabbedPane(JTabbedPane.NORTH);

	public Gui(NetworkInterface myInterface) throws IOException {

		if( Server.getInstance() == null ) {
			Server.createInstance(myInterface);
			Server.getInstance().start();
		}
		
		this.setUpUi();
		//this.setSize(500, 500);
		//this.repaint();
		//this.revalidate();
		this.pack();
	}
	
	private void setUpUi() {
		
		DNSServiceView view = new DNSServiceView();
		tab.addTab(view.getTitle(), view);
		
		view = new DNSServiceView();
		tab.addTab(view.getTitle(), view);
		
		view = new DNSServiceView();
		tab.addTab(view.getTitle(), view);
		
		LogView logView = new LogView();
		tab.addTab("Logs", logView);

		this.add(tab, BorderLayout.CENTER);
	}
}
