package org.bitducks.spoofing.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;

public class LogView extends JPanel implements ActionListener {
	
	private static int NB_COLS = 40;
	private static int NB_ROWS = 60;
	
	private JTextArea logView = null;
	private JPanel serviceList = null;
	private JButton refresh = null;
	
	private ArrayList<LogCheckBox> checkboxes = null;
	
	public LogView() {
		super();
		
		this.checkboxes = new ArrayList<LogCheckBox>();
		
		this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
		
		this.serviceList = this.generateServiceList();
		this.logView = this.generateLogView();
		this.refresh = this.generateRefreshButton();
		
		this.refreshPanel();
		
	}
	
	public void refreshPanel() {
		
		this.removeAll();
		this.add( this.serviceList );
		this.add( this.refresh );
		
		JScrollPane scroll = new JScrollPane(this.logView);
		this.add( scroll );
		
		this.revalidate();
		
	}

	private JTextArea generateLogView() {
		
		JTextArea textArea = new JTextArea();
		
		textArea = new JTextArea();
		textArea.setColumns( LogView.NB_COLS );
		textArea.setRows( LogView.NB_COLS );

		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		return textArea;
		
	}
	
	private JButton generateRefreshButton() {
		
		JButton button = new JButton("Refresh service list");
		button.addActionListener( this );
		return button;
		
	}
	
	private JPanel generateServiceList() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		
		HashSet<Service> activeServices = new HashSet<Service>();
		
		for( LogCheckBox checkbox: this.checkboxes ) {
			panel.add( checkbox );
			activeServices.add( checkbox.getService() );
		}
		
		for( Service service: Server.getInstance().getServices() ) {
			if( !activeServices.contains( service ) ) {
				LogCheckBox checkBox = this.serviceCheckBox( service );
				panel.add(checkBox);
			}
		}
		
		return panel;
		
	}
	
	private LogCheckBox serviceCheckBox( Service service ) {
			
		LogCheckBox checkbox = new LogCheckBox( service );
		checkbox.addActionListener( this );
		
		this.checkboxes.add(checkbox);
		
		return checkbox;
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		Object source = event.getSource();
		if( source instanceof LogCheckBox ) {
			
			LogCheckBox checkBox = (LogCheckBox)source;
			
			if( checkBox.isSelected() ) {
				
				GUILogAppender appender = new GUILogAppender( this.logView );
				checkBox.activateLogAppender( appender );
			
			} else {
				
				checkBox.removeLogAppender();
				
			}
			
		} else if ( source instanceof JButton && source == this.refresh ) {
						
			this.remove( this.serviceList );
			this.serviceList = this.generateServiceList();
			this.refreshPanel();
			
		}

	}
	
	

}
