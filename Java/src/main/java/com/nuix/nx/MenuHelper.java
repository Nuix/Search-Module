package com.nuix.nx;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuHelper {
	/**
	 * Locates a JMenu by name that is a direct child of the provided JMenuBar.
	 * If a JMenu item with the provided name is not located, one is created.
	 * @param menuBar The JMenuBar to look in.
	 * @param text The text of the JMenu item you are looking for.
	 * @return The found or created JMenu object.
	 */
	public static JMenu findOrCreateTopLevelMenu(JMenuBar menuBar, String text){
		for(Component comp : menuBar.getComponents()){
			if (comp instanceof JMenu){
				if(((JMenu)comp).getText().equals(text)){
					return ((JMenu)comp);
				}
			}
		}
		
		JMenu menu = new JMenu(text);
		menuBar.add(menu);
		return menu;
	}
}
