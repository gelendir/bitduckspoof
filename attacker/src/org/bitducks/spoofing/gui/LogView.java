package org.bitducks.spoofing.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;

/**
 * 
 * LogView is a GUI View used to show log messages
 * coming from one or more servics. Log messages can be activated
 * using a checkbox for each service that is running. Log messages
 * can only be activated once a service has started.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class LogView extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int NB_COLS = 40;
	
	/**
	 * All log messages will appeear in this textarea
	 */
	private JTextArea logView = null;
	/**
	 * Main container for the view
	 */
	private JPanel serviceList = null;
	/**
	 * button for refreshing the list of services that are running.
	 */
	private JButton refresh = null;
	
	private ArrayList<LogCheckBox> checkboxes = null;
	
	/**
	 * Constructor. Create a new LogView 
	 */
	public LogView() {
		super();
		
		this.checkboxes = new ArrayList<LogCheckBox>();
		
		this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
		
		this.serviceList = this.generateServiceList();
		this.logView = this.generateLogView();
		this.refresh = this.generateRefreshButton();
		
		this.refreshPanel();
		
	}
	
	/**
	 * Refresh the whole panel, updating the list
	 * of services that are running.
	 */
	public void refreshPanel() {
		
		this.removeAll();
		this.add( this.serviceList );
		this.add( this.refresh );
		
		JScrollPane scroll = new JScrollPane(this.logView);
		this.add( scroll );
		
		this.revalidate();
		
	}

	/**
	 * Utility method for generating the textarea
	 * that will be showing the log messages from the services.
	 * 
	 * @return textarea with log messages
	 */
	private JTextArea generateLogView() {
		
		JTextArea textArea = new JTextArea();
		
		textArea = new JTextArea();
		textArea.setColumns( LogView.NB_COLS );
		textArea.setRows( LogView.NB_COLS );

		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		return textArea;
		
	}
	
	/**
	 * Utility method for generating the button to
	 * refresh the service list
	 * 
	 * @return the refresh button
	 */
	private JButton generateRefreshButton() {
		
		JButton button = new JButton("Refresh service list");
		button.addActionListener( this );
		return button;
		
	}
	
	/**
	 * Utility method for generating a panel with a list
	 * of active services and checkboxes for activating the log of a service.
	 * 
	 * @return the service list in a panel
	 */
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
	
	/**
	 * Utility method for generating a checkbox for a service.
	 * The checkbox is responsible of activating the logger
	 * for a service when clicked.
	 * 
	 * @param service The service to monitor
	 * @return the checkbox for activating a logger
	 */
	private LogCheckBox serviceCheckBox( Service service ) {
			
		LogCheckBox checkbox = new LogCheckBox( service );
		checkbox.addActionListener( this );
		
		this.checkboxes.add(checkbox);
		
		return checkbox;
		
	}

	/**
	 * Activates or deactivates a logger if a log checkbox has been checked by the user
	 */
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
