package org.bitducks.spoofing.gui.serviceView;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.IpStealer;

/**
 * The view for Ip starvation service.
 * @author Simon Perreault
 *
 */
public class IPStealerServiceView extends View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static private String TITLE = "IP Stealer";
	final static private String EXPLANATION = "This service will launch a DHCP starvation attack in the network.";

	/**
	 * Constructor, will initialize the UI.
	 */
	public IPStealerServiceView() {
		super( IPStealerServiceView.TITLE );
		
		this.setupServicePanel();
	}

	/**
	 * Setup the component in the service panel.
	 */
	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel(IPStealerServiceView.EXPLANATION );
		
		this.servicePanel.add( descLabel, BorderLayout.CENTER );
			
	}

	/**
	 * Will create the service
	 */
	@Override
	protected Service createService() {
		return new IpStealer();
	}
	

}
