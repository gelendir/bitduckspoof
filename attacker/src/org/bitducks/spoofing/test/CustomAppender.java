package org.bitducks.spoofing.test;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class CustomAppender extends AppenderSkeleton {
	
	boolean active = true;
	
	public CustomAppender(Layout layout) {
		this.setLayout(layout);
	}

	@Override
	public void close() {
		this.active = false;
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	protected void append(LoggingEvent event) {
		if( this.active ) {
			Layout layout = this.getLayout();		
			String message = layout.format(event);
			this.printMessage(message);
		}
	}
	
	private void printMessage( String message ) {
		System.out.println("custom message append");
		System.out.println(message);
	}

}
