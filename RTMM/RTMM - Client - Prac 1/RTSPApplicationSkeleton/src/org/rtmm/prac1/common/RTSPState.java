package org.rtmm.prac1.common;

public enum RTSPState {
	/* States -  INIT, READY and PLAYING.
	 */
	
	INIT(0), READY(1), PLAYING (2); 
	
	private int state;
	
	private RTSPState(final int newState){
		this.state = newState;
	}
	
	private int getState() {
		return state;
	}
};
