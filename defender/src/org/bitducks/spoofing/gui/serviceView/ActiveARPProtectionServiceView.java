package org.bitducks.spoofing.gui.serviceView;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.ActiveARPProtectionService;

/**
 * The view for Active Arp Protection Service.
 * @author Simon Perreault
 *
 */
public class ActiveARPProtectionServiceView extends View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final public static String TITLE = "Active ARP Protection";
	final public static String EXPLANATION = "<html>Scans the network for ARP reply and when when the <br />" +
			"service receives an ARP reply, it will send a request to verify if the reply was <br />" +
			"authentic or not.<br />";
	
	/**
	 * Constructor, will initialize the UI.
	 */
	public ActiveARPProtectionServiceView() {
		super( ActiveARPProtectionServiceView.TITLE );
		
		this.setupServicePanel();
	}

	/**
	 * Setup the component in the service panel.
	 */
	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		JLabel descLabel = new JLabel(ActiveARPProtectionServiceView.EXPLANATION);
		
		this.servicePanel.add(descLabel, BorderLayout.CENTER);
		
	}
	
	/**
	 * Will create the service
	 */
	@Override
	protected Service createService() {
		return new ActiveARPProtectionService();
	}

}
