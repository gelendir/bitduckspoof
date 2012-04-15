package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.services.ARPReplyRateService;

public class ARPReplyRateServiceView extends View implements ActionListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CHANGE_INTERVAL = "Change Interval";
	
	public static final String BEGIN_TEXT_INTERVAL = "Interval : ";
	
	public static final String ENTER_INTERVAL = "Enter the inverval you want the service to check for ARP.";
	public static final String ERROR_WRONG_NUMBER = "Invalid operation: The number you have provided is not valid.";
	
	final public static String TITLE = "ARP Reply Rate";
	final public static String EXPLANATION = "<html>ARP Reply Rate is a detector of ARP<br /> " +
			"spoofing. It calculates the rate between the number of the ARP replies<br /> " +
			"and ARP requests received. If there is more than one ARP reply for an<br /> " +
			"IP address, this means that this IP address may be spoofed.<br />";
	
	private JLabel intervalLabel = new JLabel();
	private JButton changeInterval = new JButton();
	
	private int interval = 10;
	
	public ARPReplyRateServiceView() {
		super( ARPReplyRateServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
			
		// Text
		setupText();
		
		// Button
		setupButton();
	}
	
	private void setupText() {
		JPanel tmpPan = new JPanel();
		tmpPan.setLayout(new BoxLayout(tmpPan, BoxLayout.Y_AXIS));
			
		JLabel descLabel = new JLabel( ARPReplyRateServiceView.EXPLANATION );
		this.intervalLabel.setText(ARPReplyRateServiceView.BEGIN_TEXT_INTERVAL + this.interval);
		
		tmpPan.add( descLabel );
		tmpPan.add( this.intervalLabel );
		
		this.servicePanel.add(tmpPan, BorderLayout.CENTER);
	}
	
	private void setupButton() {
		JPanel tmpPan = new JPanel();
		tmpPan.setLayout(new BoxLayout(tmpPan, BoxLayout.Y_AXIS));
		
		this.changeInterval.setText(ARPReplyRateServiceView.CHANGE_INTERVAL);
		this.changeInterval.addActionListener(this);
		this.changeInterval.setActionCommand(ARPReplyRateServiceView.CHANGE_INTERVAL);
		
		tmpPan.add( this.changeInterval );
		
		this.servicePanel.add(tmpPan, BorderLayout.LINE_END);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		if (e.getActionCommand() == ARPReplyRateServiceView.CHANGE_INTERVAL)
		{
			String retval = "";
			if ((retval = JOptionPane.showInputDialog(ARPReplyRateServiceView.ENTER_INTERVAL, this.interval + ""))
					== null) {
				// If cancel is called
				return;
			}
			
			int intRetval;
			try {
				intRetval = Integer.parseInt(retval);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ARPReplyRateServiceView.ERROR_WRONG_NUMBER);
				return;
			}
			
			if (intRetval <= 0)
			{
				JOptionPane.showMessageDialog(null, ARPReplyRateServiceView.ERROR_WRONG_NUMBER);
				return;
			}
			
			// Setting the new interval
			this.interval = intRetval;
			if (service != null && service instanceof ARPReplyRateService)
			{
				((ARPReplyRateService)service).setInterval(this.interval);
			}
			
			this.intervalLabel.setText(ARPReplyRateServiceView.BEGIN_TEXT_INTERVAL + this.interval);
		}
	}
	
	@Override
	protected Service createService() {		
		return new ARPReplyRateService(this.interval);
	}

}
