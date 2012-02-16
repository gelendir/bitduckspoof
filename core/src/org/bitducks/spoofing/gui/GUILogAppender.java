package org.bitducks.spoofing.gui;

import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class GUILogAppender extends AppenderSkeleton {
	
	private static Layout defaultLayout = new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN);
	private static Level defaultLevel = Level.INFO;
	
	private JTextArea logView;
	boolean active = true;

	private Level level;

	public GUILogAppender( JTextArea logView, Layout layout, Level level ) {
		this.logView = logView;
		this.layout = layout;
		this.level = level;
	}
	
	public GUILogAppender( JTextArea logView ) {
		this( logView, GUILogAppender.defaultLayout, GUILogAppender.defaultLevel );
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
		if( this.active && event.getLevel().isGreaterOrEqual( this.level ) ) {
			String message = this.getLayout().format( event );
			this.logView.append( message );
		}
	}

}
