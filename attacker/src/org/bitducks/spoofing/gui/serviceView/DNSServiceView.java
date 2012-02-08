package org.bitducks.spoofing.gui.serviceView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.DNSService;

public class DNSServiceView extends View implements ActionListener{
	
	public static final String ADD_REGEX = "Add";
	public static final String REMOVE_REGEX = "Remove";
	
	public static final String ENTER_DNS_NAME = "Enter the DNS name you want to add rule.";
	public static final String ENTER_IP_ADDR = "Enter the IP address you want to redirect the user to.";
	public static final String ERROR_BAD_IP = "Invalid operation: The Ip address you have provided is not valid.";
	
	private JList<String> jlist;
	private DefaultListModel<String> modelList = new DefaultListModel<String>();
	private Map<String, InetAddress> dnsPacketFilter = new HashMap<String, InetAddress>();
	
	private JButton addRegex = new JButton();
	private JButton removeRegex = new JButton();
	
	

	public DNSServiceView() {
		super("DNS Poisoning");
		
		setUpServicePanel();
	}
	
	public void setUpServicePanel() {
		
		this.servicePanel.setLayout(new BorderLayout());
		
		
		jlist = new JList<String>(modelList);
		this.servicePanel.add(jlist, BorderLayout.CENTER);
		
		JScrollPane scroll = new JScrollPane(this.jlist);
		this.servicePanel.add(scroll, BorderLayout.CENTER);
		
		this.setUpButton();
	}
	
	public void setUpButton() {
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		
		this.addRegex.setText(DNSServiceView.ADD_REGEX);
		this.addRegex.addActionListener(this);
		this.addRegex.setActionCommand(DNSServiceView.ADD_REGEX);
		pan.add(this.addRegex);
		
		pan.add(Box.createRigidArea(new Dimension(0, 5)));
		
		this.removeRegex.setText(DNSServiceView.REMOVE_REGEX);
		this.removeRegex.addActionListener(this);
		this.removeRegex.setActionCommand(DNSServiceView.REMOVE_REGEX);
		pan.add(this.removeRegex);
		
		this.servicePanel.add(pan, BorderLayout.LINE_END);
		
	}
	
	private void addToList(String regex, String ipAddr) throws UnknownHostException {
		InetAddress addr = InetAddress.getByName(ipAddr);

		if (this.service != null && this.service instanceof DNSService) {
			((DNSService)this.service).addDnsPacketFilter(regex, addr);
		}
		
		this.dnsPacketFilter.put(regex, addr);
	}
	
	private void refreshList() {
		
		this.modelList.clear();
		
		Iterator<String> it = this.dnsPacketFilter.keySet().iterator();
		// Checking all the filter
		while (it.hasNext()) {
			String next = it.next();
			this.modelList.addElement(next + "   :   " + this.dnsPacketFilter.get(next).getHostAddress());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		String regex = "";
		String ipAddr = "";
		
		switch (e.getActionCommand()) {
		
		case DNSServiceView.ADD_REGEX:
			regex = JOptionPane.showInputDialog(DNSServiceView.ENTER_DNS_NAME);
			ipAddr = JOptionPane.showInputDialog(DNSServiceView.ENTER_IP_ADDR, Server.getInstance().getInfo().getDeviceAddress().address.getHostAddress());
			
			try {
				this.addToList(regex, ipAddr);
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(null, DNSServiceView.ERROR_BAD_IP);
			}
			this.refreshList();
			
			break;
			
		case DNSServiceView.REMOVE_REGEX:
			break;
		}
		
	}

	@Override
	protected Service createService() {
		return new DNSService();
	}
}
