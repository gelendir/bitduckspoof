package org.bitducks.spoofing.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class DeviceSelection extends JDialog implements ActionListener, MouseListener  {
	
	private JList deviceList;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private Integer index;
	
	static public NetworkInterface getSelectedDevice() {
		
		DeviceSelection dialog = new DeviceSelection();
		Integer index = dialog.showDialog();
		
		if( index == null ) {
			
			return null;
			
		} else {
			
			NetworkInterface devices[] = JpcapCaptor.getDeviceList();
			return devices[ dialog.index ];
			
		}
		
	}
	
	private Integer showDialog() {
		this.setVisible(true);
		return this.index;
	}

	private DeviceSelection() {
		super();
		this.setTitle("Device Selection");
		this.setModal(true);
		this.generateLayout();
	}
	
	private void generateLayout() {
		
		JPanel panel = this.selectionPanel();
		JPanel buttonPanel = this.buttonPanel();
		
		this.getRootPane().setDefaultButton(this.okButton);
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.pack();
		
	}
	
	private JPanel selectionPanel() {
		
		JPanel panel = new JPanel();
		panel.setBorder(this.defaultBorder());
		
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);

		JLabel label = new JLabel("Choose a device");
		panel.add(label);
		
		this.deviceList = new JList(this.deviceList());
		this.deviceList.addMouseListener( this );
		this.deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.deviceList.setLayoutOrientation(JList.VERTICAL);		
		
		JScrollPane scroll = new JScrollPane(this.deviceList);
		
		panel.add(scroll);
		
		return panel;
		
	}
	
	private String[] deviceList() {
		
		NetworkInterface devices[] = JpcapCaptor.getDeviceList();
		String[] deviceNames = new String[devices.length];
		
		for( int i = 0; i < devices.length; i++ ) {
			
			String label = devices[i].name;
			if ( devices[i].description != null ) {
				label += 	" (" +
							devices[i].description +
							") ";
			}
			
			deviceNames[i] = label;
			
		}
		
		return deviceNames;
		
	}
	
	private JPanel buttonPanel() {
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(this.defaultBorder());
		buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS) );
		
		this.okButton = new JButton("OK");
		this.okButton.addActionListener( this );
		
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener( this );
		
		buttonPanel.add(this.okButton);
		buttonPanel.add(this.cancelButton);
		
		return buttonPanel;
	}
	
	private Border defaultBorder() {
		return BorderFactory.createEmptyBorder(10,10,10,10);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		if( event.getSource().equals( this.okButton ) ) {
			this.index = this.deviceList.getSelectedIndex();
		} else if( event.getSource().equals( this.cancelButton ) ) {	
			this.index = null;
		}
		this.setVisible(false);
		this.dispose();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if( e.getClickCount() >= 2 ) {
			this.index = this.deviceList.getSelectedIndex();
			this.setVisible(false);
			this.dispose();
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
