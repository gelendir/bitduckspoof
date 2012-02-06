package org.bitducks.spoofing.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.bitducks.spoofing.core.Service;

public class View extends JPanel implements ActionListener {
	
	private static final String STOP = "Stop";
	private static final String START = "Start";
	
	private String title = "";
	protected Service service = null;
	protected JPanel servicePanel = new JPanel();
	private JPanel startStopbuttonPanel = new JPanel();
	private JTextArea terminal = new JTextArea();
	
	
	public View(String title, Service service) {
		this.title = title;
		this.service = service;
		
		setUpUI();
	}
	
	public String getTitle() {
		return this.title;
	}
	
	private void setUpUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.add(this.servicePanel);
		
		this.setUpStartStopButton();
		this.add(this.startStopbuttonPanel);
		
		this.setUpTerminal();
		this.add(this.terminal);
		
	}
	
	private void setUpStartStopButton() {
		this.startStopbuttonPanel.setLayout(new BoxLayout(this.startStopbuttonPanel, BoxLayout.X_AXIS));
		
		JButton button = new JButton("Start");
		button.addActionListener(this);
		button.setActionCommand(View.START);
		this.startStopbuttonPanel.add(button);
		
		button = new JButton("Stop");
		button.addActionListener(this);
		button.setActionCommand(View.STOP);
		this.startStopbuttonPanel.add(button);
		
	}
	
	private void setUpTerminal() {
		this.terminal.setMinimumSize(new Dimension(600, 300));
		//this.terminal.setSize(600, 300);
		this.terminal.setEditable(false);
		//this.terminal.setRows(5);
		//this.terminal.setColumns(40);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		switch (e.getActionCommand()) {
		
		case View.START:
			break;
		case View.STOP:
			break;
		}
	}

}
