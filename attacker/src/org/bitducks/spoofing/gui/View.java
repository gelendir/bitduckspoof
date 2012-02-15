package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;

public abstract class View extends JPanel implements ActionListener {
	
	private static final String STOP = "Stop";
	private static final String START = "Start";
	private static final String STARTED = "Started";
	
	private String title = "";
	protected Service service = null;
	protected JPanel servicePanel = new JPanel();
	private JPanel startStopbuttonPanel = new JPanel();
	private JTextArea terminal = new JTextArea();
	
	private JButton start = new JButton();
	private JButton stop = new JButton();
	
	public View(String title) {
		this.title = title;
		
		setUpUI();
	}
	
	public String getTitle() {
		return this.title;
	}
	
	private void setUpUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		
		this.add(this.servicePanel, BorderLayout.CENTER);
		
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		this.setUpStartStopButton();
		this.add(this.startStopbuttonPanel);
		
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		
		this.setUpTerminal();
		this.add(this.terminal);
		
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		
	}
	
	private void setUpStartStopButton() {
		this.startStopbuttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		this.start.setText(View.START);
		this.start.addActionListener(this);
		this.start.setActionCommand(View.START);
		this.startStopbuttonPanel.add(this.start);
		
		this.stop.setText(View.STOP);
		this.stop.addActionListener(this);
		this.stop.setActionCommand(View.STOP);
		this.stop.setEnabled(false);
		this.startStopbuttonPanel.add(this.stop);
		
	}
	
	private void setUpTerminal() {
		this.terminal.setEditable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		switch (e.getActionCommand()) {
		
		case View.START:
			this.start.setEnabled(false);
			this.stop.setEnabled(true);
			// Create the Service instance
			this.service = this.createService();
			this.start.setText(View.STARTED);
			Server.getInstance().addService(this.service);
			this.service.addLogAppender(new GUILogAppender(this.terminal));
			break;
			
		case View.STOP:
			Server.getInstance().removeService(this.service);
			try {
				service.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			
			service = null;
			this.start.setEnabled(true);
			this.stop.setEnabled(false);
			this.start.setText(View.START);
			break;
		}
	}
	
	/**
	 * Create a new service from the specific ServiceView
	 * @return Service
	 */
	protected abstract Service createService();

}
