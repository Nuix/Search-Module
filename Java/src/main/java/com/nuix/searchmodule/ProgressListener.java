package com.nuix.searchmodule;

public interface ProgressListener {
	public void whenProgressUpdated(String mainMessage, String subMessage, int currentProgress, int total);
}
