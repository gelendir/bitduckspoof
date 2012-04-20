package org.bitducks.spoofing.main;

import javax.swing.WindowConstants;

import jpcap.NetworkInterface;

import org.apache.log4j.BasicConfigurator;
import org.bitducks.spoofing.gui.DeviceSelection;
import org.bitducks.spoofing.gui.Gui;

public class Launcher {

	public static void main(String[] args) throws Exception {
		
		BasicConfigurator.configure();
		
		NetworkInterface device = DeviceSelection.getSelectedDevice();
		
		if( device != null ) {
			Gui gui = new Gui(device);
			gui.setVisible(true);
			gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
	}
}
