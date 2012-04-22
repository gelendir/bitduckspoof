package org.bitducks.spoofing.gui;

import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Implementation of Log4j's logging interface. Used for logging
 * service messages to a GUI panel.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class GUILogAppender extends AppenderSkeleton {
	
	/**
	 * Default formatting used for log messages
	 */
	private static Layout defaultLayout = new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN);
	
	/**
	 * Default logging level for displaying log messages
	 */
	private static Level defaultLevel = Level.INFO;
	
	/**
	 * textarea used for displaying log messages 
	 */
	private JTextArea logView;
	
	/**
	 * Used to determine if the current appender if active or not
	 */
	boolean active = true;

	/**
	 * logging level for logging messages
	 */
	private Level level;

	/**
	 * Constructor. Creates a new GUILogAppender.
	 * 
	 * @param logView The textarea where log messages will appear
	 * @param layout Log message formatting
	 * @param level Minimal level for displaying log messages
	 */
	public GUILogAppender( JTextArea logView, Layout layout, Level level ) {
		this.logView = logView;
		this.layout = layout;
		this.level = level;
	}
	
	/**
	 * Constructor. Creates a new GUILogAppender.
	 * 
	 * @param logView The textarea where log messages will appear
	 */
	public GUILogAppender( JTextArea logView ) {
		this( logView, GUILogAppender.defaultLayout, GUILogAppender.defaultLevel );
	}

	/**
	 * Close the current appender. Log messages will no longer be displayed
	 */
	@Override
	public void close() {
		this.active  = false;
	}

	/**
	 * Implementation detail for log4j
	 */
	@Override
	public boolean requiresLayout() {
		return true;
	}

	/**
	 * Append a message to the appender. Will
	 * add the message to the textarea if the log level is high enough.
	 */
	@Override
	protected void append(LoggingEvent event) {
		if( this.active && event.getLevel().isGreaterOrEqual( this.level ) ) {
			String message = this.getLayout().format( event );
			this.logView.append( message );
		}
	}

}
