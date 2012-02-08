package org.bitducks.spoofing.main;

import javax.swing.WindowConstants;

import jpcap.NetworkInterface;

import org.bitducks.spoofing.gui.DeviceSelection;
import org.bitducks.spoofing.gui.Gui;

public class mainUI {

	public static void main(String[] args) throws Exception {
		
		NetworkInterface device = DeviceSelection.getSelectedDevice();
		Gui gui = new Gui(device);
		gui.setVisible(true);
		gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
