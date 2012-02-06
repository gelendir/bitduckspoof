package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.bitducks.spoofing.gui.serviceView.DNSServiceView;

public class Gui extends JFrame {
	
	private JTabbedPane tab = new JTabbedPane(JTabbedPane.NORTH);

	public Gui() {

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

		this.add(tab, BorderLayout.CENTER);
	}
}
