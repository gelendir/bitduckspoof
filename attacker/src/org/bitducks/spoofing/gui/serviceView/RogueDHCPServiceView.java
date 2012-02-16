package org.bitducks.spoofing.gui.serviceView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.DNSService;
import org.bitducks.spoofing.services.RogueDHCPService;

public class RogueDHCPServiceView extends View implements  ActionListener{

	public static final String CHANGE_GATEWAY = "Change Gateway";
	public static final String CHANGE_DNS = "Change DNS";
	
	public static final String BEGIN_TEXT_GATEWAY = "Gateway : ";
	public static final String BEGIN_TEXT_DNS = "DNS : ";
	
	public static final String ENTER_IP_ADDR = "Enter the IP address for the choosen field.";
	public static final String ERROR_BAD_IP = "Invalid operation: The Ip address you have provided is not valid.";
	
	
	final static private String TITLE = "Rogue DHCP";
	final static private String EXPLANATION = "This service will launch a Rogue DHCP on the network.";
	
	private JLabel gatewayAddrLabel = new JLabel();
	private JLabel dnsAddrLabel = new JLabel();
	
	private JButton changeGatway = new JButton();
	private JButton changeDNS = new JButton();
	
	private InetAddress gatewayAddr = null;
	private InetAddress dnsAddr = null;

	public RogueDHCPServiceView() {
		super( RogueDHCPServiceView.TITLE );
		
		this.gatewayAddr = Server.getInstance().getInfo().getAddress();
		this.dnsAddr = Server.getInstance().getInfo().getAddress();
		
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
			
		JLabel descLabel = new JLabel( RogueDHCPServiceView.EXPLANATION );
		this.gatewayAddrLabel.setText(RogueDHCPServiceView.BEGIN_TEXT_GATEWAY + this.gatewayAddr.getHostAddress());
		this.dnsAddrLabel.setText(RogueDHCPServiceView.BEGIN_TEXT_DNS + this.dnsAddr.getHostAddress());
		
		tmpPan.add( descLabel );
		tmpPan.add( this.gatewayAddrLabel );
		tmpPan.add( this.dnsAddrLabel );
		
		this.servicePanel.add(tmpPan, BorderLayout.CENTER);
	}
	
	private void setupButton() {
		JPanel tmpPan = new JPanel();
		tmpPan.setLayout(new BoxLayout(tmpPan, BoxLayout.Y_AXIS));
		
		this.changeGatway.setText(RogueDHCPServiceView.CHANGE_GATEWAY);
		this.changeGatway.addActionListener(this);
		this.changeGatway.setActionCommand(RogueDHCPServiceView.CHANGE_GATEWAY);
		
		this.changeDNS.setText(RogueDHCPServiceView.CHANGE_DNS);
		this.changeDNS.addActionListener(this);
		this.changeDNS.setActionCommand(RogueDHCPServiceView.CHANGE_DNS);
		
		tmpPan.add( this.changeGatway );
		tmpPan.add( this.changeDNS );
		
		this.servicePanel.add(tmpPan, BorderLayout.LINE_END);
	}

	@Override
	protected Service createService() {
		return new RogueDHCPService(this.gatewayAddr, this.dnsAddr);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		String ipAddr = "";
		
		if ((ipAddr = JOptionPane.showInputDialog(RogueDHCPServiceView.ENTER_IP_ADDR,
				Server.getInstance().getInfo().getDeviceAddress().address.getHostAddress()))
				== null) {
			// If cancel is called
			return;
		}
		
		switch (e.getActionCommand()) {
		
		case RogueDHCPServiceView.CHANGE_GATEWAY:
			
			try {
				this.gatewayAddr = InetAddress.getByName(ipAddr);
				if (this.service != null && this.service instanceof RogueDHCPService) {
					((RogueDHCPService)service).setGateway(gatewayAddr);
				}
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(null, DNSServiceView.ERROR_BAD_IP);
			}
			
			this.gatewayAddrLabel.setText(RogueDHCPServiceView.BEGIN_TEXT_GATEWAY + this.gatewayAddr.getHostAddress());
			break;
			
		case RogueDHCPServiceView.CHANGE_DNS:
			
			try {
				this.dnsAddr = InetAddress.getByName(ipAddr);
				if (this.service != null && this.service instanceof RogueDHCPService) {
					((RogueDHCPService)service).setDNS(gatewayAddr);
				}
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(null, DNSServiceView.ERROR_BAD_IP);
			}
			
			this.dnsAddrLabel.setText(RogueDHCPServiceView.BEGIN_TEXT_DNS + this.dnsAddr.getHostAddress());
			break;
		
		}
		
	}
}
