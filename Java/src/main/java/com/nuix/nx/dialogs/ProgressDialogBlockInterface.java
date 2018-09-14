package com.nuix.nx.dialogs;

/**
 * Interface to be implemented to provide code to be ran by the {@link ProgressDialog}.
 * @author JasonWells
 *
 */
public interface ProgressDialogBlockInterface {
	public void DoWork(ProgressDialog dialog);
}
