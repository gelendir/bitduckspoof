package org.bitducks.spoofing.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
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
	
	private static Layout defaultLayout = new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN);
	
	private static int NB_COLS = 40;
	private static int NB_ROWS = 60;
	
	private JTextArea logView = null;
	private JPanel serviceList = null;
	
	private HashMap<JCheckBox, Service> checkboxTable = new HashMap<JCheckBox, Service>();
	
	private ArrayList<Service> checkedServices = null;
	
	public LogView() {
		super();
		
		this.checkedServices = new ArrayList<Service>();
		
		this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
		this.addServiceList();
		this.addLogView();

	}
	
	private void addServiceList() {
		this.add( this.generateServiceList() );
	}

	private void addLogView() {
		this.logView = new JTextArea();
		this.logView.setColumns( LogView.NB_COLS );
		this.logView.setRows( LogView.NB_COLS );

		DefaultCaret caret = (DefaultCaret)this.logView.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll = new JScrollPane(this.logView);
		
		this.add( scroll );
	}
	
	private JPanel generateServiceList() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		
		System.out.println( Server.getInstance().getServices() );
		
		for( Service service: Server.getInstance().getServices() ) {
			JCheckBox checkBox = this.serviceCheckBox( service );
			panel.add(checkBox);
		}
		
		return panel;
		
	}
	
	private JCheckBox serviceCheckBox( Service service ) {
			
		JCheckBox checkbox = new JCheckBox( service.serviceName() );
		checkbox.addActionListener( this );
		this.checkboxTable.put( checkbox, service );
		
		if( this.checkedServices.contains( service ) ) {
			checkbox.setSelected(true);

		}
		
		return checkbox;
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		Object source = event.getSource();
		if( source instanceof JCheckBox ) {
			
			JCheckBox checkBox = (JCheckBox)source;
			
			if( checkBox.isSelected() ) {
				
				Service service = this.checkboxTable.get( event.getSource() );
				GUILogAppender appender = new GUILogAppender( this.logView, LogView.defaultLayout );
				service.addLogAppender( appender );
			
			}
			
		}

	}
	
	

}
