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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.gui.View;
import org.bitducks.spoofing.services.DNSService;

/**
 * The view for Dns poisoning service.
 * @author Simon Perreault
 *
 */
public class DNSServiceView extends View implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String ADD_REGEX = "Add";
	public static final String REMOVE_REGEX = "Remove";
	public static final String CHANGE_FALSE_IP = "Default DNS IP";
	
	public static final String WARNING_NO_ENTRY = "This list must contain 1 or more entry.";
	public static final String ENTER_DNS_NAME = "Enter the DNS name you want to add rule.";
	public static final String ENTER_IP_ADDR = "Enter the IP address you want to redirect the user to.";
	public static final String ERROR_BAD_IP = "Invalid operation: The Ip address you have provided is not valid.";
	public static final String ERROR_BAD_CHARACTER = "Invalid operation: ':' is prohibited.";
	public static final String TITLE = "DNS Poisoning";
	
	private JList<String> jlist;
	private DefaultListModel<String> modelList = new DefaultListModel<String>();
	private Map<String, InetAddress> dnsPacketFilter = new HashMap<String, InetAddress>();
	
	private JButton addRegex = new JButton();
	private JButton removeRegex = new JButton();
	//private JButton changeFalseIp = new JButton();
	
	
	/**
	 * Constructor, will initialize the UI.
	 */
	public DNSServiceView() {
		super( DNSServiceView.TITLE );
		
		setUpServicePanel();
		
		this.addDefaultFilter(Server.getInstance().getInfo().getDeviceAddress().address);
	}
	
	/**
	 * Add the default filter
	 * @param addr the address to be related to the default filter
	 */
	private void addDefaultFilter(InetAddress addr) {
		dnsPacketFilter.put(".*", addr);
		this.refreshList();
	}
	
	/**
	 * Setup the component in the service panel.
	 */
	public void setUpServicePanel() {
		
		this.servicePanel.setLayout(new BorderLayout());
		
		
		jlist = new JList<String>(modelList);
		this.servicePanel.add(jlist, BorderLayout.CENTER);
		
		JScrollPane scroll = new JScrollPane(this.jlist);
		this.servicePanel.add(scroll, BorderLayout.CENTER);
		
		this.setUpButton();
	}
	
	/**
	 * Will setup the button in the Service panel
	 */
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
	
	/**
	 * Will add an entry to the list.
	 * @param regex
	 * @param ipAddr
	 * @throws UnknownHostException
	 */
	private void addToList(String regex, String ipAddr) throws UnknownHostException {
		if (regex.contains(":")) {
			// We do not accept the : character
			JOptionPane.showMessageDialog(null, DNSServiceView.ERROR_BAD_CHARACTER);
			return ;
		}
		
		InetAddress addr = InetAddress.getByName(ipAddr);
		
		this.dnsPacketFilter.put(regex, addr);
	}
	
	/**
	 * Will remove an entry from the list
	 * @param line
	 * @throws UnknownHostException
	 */
	private void removeFromList(String line) throws UnknownHostException {
			String splitResult[] = line.split(":");
			this.dnsPacketFilter.remove(splitResult[0].trim());
	}
	
	/**
	 * Will refresh the list
	 */
	private void refreshList() {
	
		this.modelList.clear();
		
		Iterator<String> it = this.dnsPacketFilter.keySet().iterator();
		// Checking all the filter
		while (it.hasNext()) {
			String next = it.next();
			this.modelList.addElement(next + "   :   " + this.dnsPacketFilter.get(next).getHostAddress());
		}
	}
	
	/**
	 * Used to execute an action depending of the button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		String regex = "";
		String ipAddr = "";
		
		switch (e.getActionCommand()) {
		
		case DNSServiceView.ADD_REGEX:
			if ((regex = JOptionPane.showInputDialog(DNSServiceView.ENTER_DNS_NAME))
					== null) {
				break;
			}
			if ((ipAddr = JOptionPane.showInputDialog(DNSServiceView.ENTER_IP_ADDR,
					Server.getInstance().getInfo().getDeviceAddress().address.getHostAddress()))
					== null) {
				break;
			}
			
			try {
				this.addToList(regex, ipAddr);
				if (this.service != null && this.service instanceof DNSService) {
					((DNSService)service).addDnsPacketFilter(regex, InetAddress.getByName(ipAddr));
				}
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(null, DNSServiceView.ERROR_BAD_IP);
			}
			
			this.refreshList();
			
			break;
			
		case DNSServiceView.REMOVE_REGEX:
			regex = this.jlist.getSelectedValue();
			if (regex != null) {
				try {
					this.removeFromList(regex);
					if (this.service != null && this.service instanceof DNSService) {
						((DNSService)service).removeDnsPacketFilter(regex.split("   :   ")[0]);
					}
				} catch (UnknownHostException e1) {
					JOptionPane.showMessageDialog(null, DNSServiceView.ERROR_BAD_IP);
				}
			}
			this.refreshList();
			break;

		}
		
	}

	/**
	 * Will create the service with the Dns rules list in the Ui.
	 */
	@Override
	protected Service createService() {
		DNSService service = new DNSService();
		
		Iterator<String> it = this.dnsPacketFilter.keySet().iterator();
		// Checking all the filter
		while (it.hasNext()) {
			String next = it.next();
			service.addDnsPacketFilter(next, this.dnsPacketFilter.get(next));
		}

		return service;
	}
}
