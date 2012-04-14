package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.services.RogueDHCPDetectionService;

public class RogueDHCPDetectionServiceView extends View implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String ADD_SERVER = "Add";
	public static final String REMOVE_SERVER = "Remove";
	
	public static final String ENTER_DHCP_SERVER = "Enter the DHCP server IP you want to add.";
	public static final String ERROR_BAD_IP = "Invalid operation: The Ip address you have provided is not valid.";
	
	final public static String TITLE = "Rogue DHCP Detection";
	final public static String EXPLANATION = "<html>Rogue DHCP Detection is used to detect rogue DHCP servers<br /> " +
			"by sending a DHCP discover and comparing the offers with the available<br /> " +
			"trusted DHCP servers in the list bellow.<br />";
	
	private JList<String> jlist;
	private DefaultListModel<String> modelList = new DefaultListModel<String>();
	private List<InetAddress> dhcpServerList = new ArrayList<InetAddress>();
	
	private JButton addServer = new JButton();
	private JButton removeServer = new JButton();
	
	public RogueDHCPDetectionServiceView() {
		super( RogueDHCPDetectionServiceView.TITLE );
		
		this.setupServicePanel();
	}

	private void setupServicePanel() {
		
		this.servicePanel.setLayout(new BorderLayout());
		
		JLabel descLabel = new JLabel( RogueDHCPDetectionServiceView.EXPLANATION );
		this.servicePanel.add(descLabel, BorderLayout.NORTH);
		
		jlist = new JList<String>(modelList);
		this.servicePanel.add(jlist, BorderLayout.CENTER);
		
		JScrollPane scroll = new JScrollPane(this.jlist);
		this.servicePanel.add(scroll, BorderLayout.CENTER);
		
		this.setUpButton();
	}
	
	public void setUpButton() {
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		
		this.addServer.setText(RogueDHCPDetectionServiceView.ADD_SERVER);
		this.addServer.addActionListener(this);
		this.addServer.setActionCommand(RogueDHCPDetectionServiceView.ADD_SERVER);
		pan.add(this.addServer);
		
		pan.add(Box.createRigidArea(new Dimension(0, 5)));
		
		this.removeServer.setText(RogueDHCPDetectionServiceView.REMOVE_SERVER);
		this.removeServer.addActionListener(this);
		this.removeServer.setActionCommand(RogueDHCPDetectionServiceView.REMOVE_SERVER);
		pan.add(this.removeServer);
		
		this.servicePanel.add(pan, BorderLayout.LINE_END);
	}
	
	private void addToList(String ipAddr) throws UnknownHostException {
		InetAddress addr = InetAddress.getByName(ipAddr);
		
		this.dhcpServerList.add(addr);
	}
	
	private void removeFromList(String ipAddr) throws UnknownHostException {
		InetAddress addr = InetAddress.getByName(ipAddr);
		
		this.dhcpServerList.remove(addr);
	}
	
	private void refreshList() {
		this.modelList.clear();
		
		Iterator<InetAddress> it = this.dhcpServerList.iterator();
		// Checking all the filter
		while (it.hasNext()) {
			InetAddress addr = it.next();
			this.modelList.addElement(addr.getHostAddress());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		String ipAddr = "";
		
		switch (e.getActionCommand()) {
		
		case RogueDHCPDetectionServiceView.ADD_SERVER:
			if ((ipAddr = JOptionPane.showInputDialog(RogueDHCPDetectionServiceView.ENTER_DHCP_SERVER))
					== null) {
				break;
			}
			
			try {
				this.addToList(ipAddr);
				if (this.service != null && this.service instanceof RogueDHCPDetectionService) {
					((RogueDHCPDetectionService)this.service).addSupposedServer(InetAddress.getByName(ipAddr));
				}
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(null, RogueDHCPDetectionServiceView.ERROR_BAD_IP);
			}
			
			this.refreshList();
			
			break;
			
		case RogueDHCPDetectionServiceView.REMOVE_SERVER:
			ipAddr = this.jlist.getSelectedValue();
			if (ipAddr != null) {
				try {
					this.removeFromList(ipAddr);
					if (this.service != null && this.service instanceof RogueDHCPDetectionService) {
						((RogueDHCPDetectionService)this.service).removeSupposedServer(InetAddress.getByName(ipAddr));
					}
				} catch (UnknownHostException e1) {
					JOptionPane.showMessageDialog(null, RogueDHCPDetectionServiceView.ERROR_BAD_IP);
				}
			}
			
			this.refreshList();
			break;
		}
	}
	
	@Override
	protected Service createService() {
		return new RogueDHCPDetectionService(this.dhcpServerList);
	}
}
