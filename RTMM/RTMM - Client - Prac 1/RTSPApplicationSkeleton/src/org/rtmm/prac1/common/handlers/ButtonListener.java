package org.rtmm.prac1.common.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.rtmm.prac1.common.Application;

public abstract class ButtonListener<T extends Application> implements ActionListener {
	protected final T app;
	
	public ButtonListener(T client) {
		this.app = client;
	}
	
	public final void actionPerformed(ActionEvent e) {
		actionPerformed();
	}
	
	protected abstract void actionPerformed();
}
