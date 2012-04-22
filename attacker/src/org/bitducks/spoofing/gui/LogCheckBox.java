package org.bitducks.spoofing.gui;

import javax.swing.JCheckBox;

import org.bitducks.spoofing.core.Service;

/**
 * Custom check box used for dynamically activating and deactivating a 
 * service log listener. Used internally in the LogView
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class LogCheckBox extends JCheckBox {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GUILogAppender appender;
	private Service service;

	/**
	 * Constructor. Creates a new checkbox
	 * @param service The service to log
	 */
	public LogCheckBox( Service service ) {
		super( service.serviceName() );
		this.service = service;
	}

	/**
	 * Return the appender used by the service for
	 * logging messages
	 * 
	 * @return The log appender
	 */
	public GUILogAppender getAppender() {
		return appender;
	}
	
	/**
	 * Set the appender that will be used by the service
	 * 
	 * @param appender The log appender
	 */
	public void setAppender( GUILogAppender appender ) {
		this.appender = appender;
	}

	/**
	 * Return the service for this checkbox
	 * 
	 * @return The network service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * Activate a log appender for a service. Log messages
	 * coming from the service will be sent to this appender.
	 * 
	 * @param appender The log appender
	 */
	public void activateLogAppender(GUILogAppender appender) {
		
		this.appender = appender;
		this.service.addLogAppender( appender );
		
	}

	/**
	 * Deactive the log appender for this service. Log messages will
	 * no longer be sent to the appender.
	 */
	public void removeLogAppender() {
		
		this.service.removeLogAppender( this.appender );
		this.appender = null;
		
	}
	
}
