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

/**
 * Small dialog to help the user select a device when starting a GUI program.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class DeviceSelection extends JDialog implements ActionListener, MouseListener  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The device list to show to the user
	 */
	@SuppressWarnings("rawtypes")
	private JList deviceList;
	
	/**
	 * OK button for selecting a device
	 */
	private JButton okButton;
	
	/**
	 * Cancel button for closing the dialog
	 */
	private JButton cancelButton;
	
	/**
	 * Index of the device that was selected.
	 */
	private Integer index;
	
	/**
	 * Returns the device that was selected byt the user in the dialog.
	 * @return Network device selected by the user.
	 */
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
	
	/**
	 * Show the dialog to the user, letting him select a network device.
	 * @return the index of the device selected (in the device list)
	 */
	private Integer showDialog() {
		this.setVisible(true);
		return this.index;
	}

	/**
	 * Constructor. Create a new dialog.
	 */
	private DeviceSelection() {
		super();
		this.setTitle("Device Selection");
		this.setModal(true);
		this.generateLayout();
	}
	
	/**
	 * Utility method for generating the panel's layout.
	 */
	private void generateLayout() {
		
		JPanel panel = this.selectionPanel();
		JPanel buttonPanel = this.buttonPanel();
		
		this.getRootPane().setDefaultButton(this.okButton);
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.pack();
		
	}
	
	/**
	 * Utility method for generating the panel with the device selection
	 * list.
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	/**
	 * Utility method that returns a name for each network
	 * device available.
	 * 
	 * @return Array of Strings with each device's name.
	 */
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
	
	/**
	 * Utility method for generating the panel containing
	 * the OK and Cancel button.
	 * 
	 * @return The button panel.
	 */
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
	
	/**
	 * Utility method for generating a border for the dialog.
	 * @return
	 */
	private Border defaultBorder() {
		return BorderFactory.createEmptyBorder(10,10,10,10);
	}

	/**
	 * Finds out what device the user has selected and
	 * closes the dialog after a button click.
	 */
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

	/**
	 * Finds out what device the user has selected and
	 * closes the dialog after a mouse click.
	 */
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
