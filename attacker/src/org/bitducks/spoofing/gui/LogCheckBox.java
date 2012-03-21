package org.bitducks.spoofing.gui;

import javax.swing.JCheckBox;

import org.bitducks.spoofing.core.Service;

public class LogCheckBox extends JCheckBox {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GUILogAppender appender;
	private Service service;

	public LogCheckBox( Service service ) {
		super( service.serviceName() );
		this.service = service;
	}

	public GUILogAppender getAppender() {
		return appender;
	}
	
	public void setAppender( GUILogAppender appender ) {
		this.appender = appender;
	}

	public Service getService() {
		return service;
	}

	public void activateLogAppender(GUILogAppender appender) {
		
		this.appender = appender;
		this.service.addLogAppender( appender );
		
	}

	public void removeLogAppender() {
		
		this.service.removeLogAppender( this.appender );
		this.appender = null;
		
	}
	
}
