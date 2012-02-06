package org.bitducks.spoofing.gui;

import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class GUILogAppender extends AppenderSkeleton {
	
	private JTextArea logView;
	boolean active = true;

	public GUILogAppender( JTextArea logView, Layout layout ) {
		this.logView = logView;
		this.layout = layout;
	}

	@Override
	public void close() {
		this.active  = false;
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	protected void append(LoggingEvent event) {
		if( this.active ) {
			String message = this.getLayout().format( event );
			this.logView.append( message );
		}
	}

}
