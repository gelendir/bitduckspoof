package org.bitducks.spoofing.main;

import javax.swing.WindowConstants;

import org.bitducks.spoofing.gui.Gui;

public class mainUI {

	public static void main(String[] args) throws Exception {
		
		Gui gui = new Gui();
		gui.setVisible(true);
		gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
