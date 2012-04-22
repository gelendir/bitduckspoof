package org.bitducks.spoofing.gui.serviceView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.RedirectNAT;
import org.bitducks.spoofing.services.ReplyARPService;

/**
 * The view for Arp Reply Spoofing service
 * @author Simon Perreault
 *
 */
public class ARPReplySpoofingView extends View implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "ARP Reply Spoofing";
	private static final String EXPLANATION = "This service will poison the target arp cache and will redirect all the trafic between the gateway and the target.";
	
	public static final String CHANGE_TARGET = "Change Target";
	public static final String CHANGE_HOST = "Change Host";
	public static final String CHANGE_FREQ = "Change Frequence";
	
	public static final String BEGIN_TEXT_TARGET = "Target : ";
	public static final String BEGIN_TEXT_HOST = "Host : ";
	public static final String BEGIN_TEXT_FREQ = "Frequence : ";
	
	public static final String ENTER_IP_ADDR = "Enter the IP address for the choosen field.";
	public static final String ENTER_NUMBER = "Enter the frequence you want to spam arp request"; 
	public static final String ERROR_BAD_IP = "Invalid operation: The Ip address you have provided is not valid.";
	
	private JLabel targetAddrLabel = new JLabel();
	private JLabel hostAddrLabel = new JLabel();
	private JLabel freqLabel = new JLabel();
	
	private JButton changeTarget = new JButton();
	private JButton changeHost = new JButton();
	private JButton changeFreq = new JButton();
	
	private InetAddress target = null;
	private InetAddress host = null;
	private int freq = ReplyARPService.FREQ_SPOOF_DEFAULT;
	
	
	public ARPReplySpoofingView() {
		super( ARPReplySpoofingView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
	
		this.servicePanel.setLayout( new BorderLayout() );
		
		this.target = Server.getInstance().getInfo().getAddress();
		this.host = Server.getInstance().getInfo().getAddress();
		
		// Text
		setupText();
		
		// Button
		setupButton();
	}
	
	private void setupText() {
		JPanel tmpPan = new JPanel();
		tmpPan.setLayout(new BoxLayout(tmpPan, BoxLayout.Y_AXIS));
			
		JLabel descLabel = new JLabel( ARPReplySpoofingView.EXPLANATION );
		this.targetAddrLabel.setText(ARPReplySpoofingView.BEGIN_TEXT_TARGET + this.target.getHostAddress());
		this.hostAddrLabel.setText(ARPReplySpoofingView.BEGIN_TEXT_HOST + this.host.getHostAddress());
		this.freqLabel.setText(ARPReplySpoofingView.BEGIN_TEXT_FREQ + this.freq);
		
		tmpPan.add( descLabel );
		tmpPan.add( this.targetAddrLabel );
		tmpPan.add( this.hostAddrLabel );
		tmpPan.add( this.freqLabel );
		
		this.servicePanel.add(tmpPan, BorderLayout.CENTER);
	}
	
	private void setupButton() {
		JPanel tmpPan = new JPanel();
		tmpPan.setLayout(new BoxLayout(tmpPan, BoxLayout.Y_AXIS));
		
		this.changeTarget.setText(ARPReplySpoofingView.CHANGE_TARGET);
		this.changeTarget.addActionListener(this);
		this.changeTarget.setActionCommand(ARPReplySpoofingView.CHANGE_TARGET);
		
		this.changeHost.setText(ARPReplySpoofingView.CHANGE_HOST);
		this.changeHost.addActionListener(this);
		this.changeHost.setActionCommand(ARPReplySpoofingView.CHANGE_HOST);
		
		this.changeFreq.setText(ARPReplySpoofingView.CHANGE_FREQ);
		this.changeFreq.addActionListener(this);
		this.changeFreq.setActionCommand(ARPReplySpoofingView.CHANGE_FREQ);
		
		tmpPan.add( this.changeTarget );
		tmpPan.add( this.changeHost );
		tmpPan.add( this.changeFreq );
		
		this.servicePanel.add(tmpPan, BorderLayout.LINE_END);
	}

	@Override
	protected Service createService() {
		return new RedirectNAT(target, host, freq);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		if (running)
		{
			this.changeTarget.setEnabled(false);
			this.changeHost.setEnabled(false);
			this.changeTarget.setEnabled(false);
		}
		else
		{
			this.changeTarget.setEnabled(true);
			this.changeHost.setEnabled(true);
			this.changeTarget.setEnabled(true);
		}
		
		
		String ipAddr = "";
		
		switch (e.getActionCommand()) {
		
		case ARPReplySpoofingView.CHANGE_TARGET:
			if ((ipAddr = JOptionPane.showInputDialog(ARPReplySpoofingView.ENTER_IP_ADDR,
					Server.getInstance().getInfo().getDeviceAddress().address.getHostAddress()))
					== null) {
				// If cancel is called
				return;
			}
			
			try {
				this.target = InetAddress.getByName(ipAddr);
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(null, DNSServiceView.ERROR_BAD_IP);
			}
			
			this.targetAddrLabel.setText(ARPReplySpoofingView.BEGIN_TEXT_TARGET + this.target.getHostAddress());
			break;
			
		case ARPReplySpoofingView.CHANGE_HOST:
			if ((ipAddr = JOptionPane.showInputDialog(ARPReplySpoofingView.ENTER_IP_ADDR,
					Server.getInstance().getInfo().getDeviceAddress().address.getHostAddress()))
					== null) {
				// If cancel is called
				return;
			}
			
			try {
				this.host = InetAddress.getByName(ipAddr);
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(null, DNSServiceView.ERROR_BAD_IP);
			}
			
			this.hostAddrLabel.setText(ARPReplySpoofingView.BEGIN_TEXT_HOST + this.host.getHostAddress());
			break;
		
		case ARPReplySpoofingView.CHANGE_FREQ:
			String value = "";
			if ((value = JOptionPane.showInputDialog(ARPReplySpoofingView.ENTER_NUMBER))
					== null) {
				// If cancel is called
				return;
			}
			
			try {
				this.freq = Integer.parseInt(value);
			} catch (NumberFormatException e2) {
				
			}
			
			this.freqLabel.setText(ARPReplySpoofingView.BEGIN_TEXT_FREQ + this.freq);
			break;
		}
		
	}

}
