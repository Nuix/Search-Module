package com.nuix.nx;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Helper utility to change the Java look and feel.
 * @author JasonWells
 *
 */
public class LookAndFeelHelper {
	/**
	 * Changes the current Java look and feel to "Windows" if it is currently
	 * "Metal"
	 */
	public static void setWindows(){
		if(UIManager.getLookAndFeel().getName().equals("Metal")){
			try {
			    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Windows".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
