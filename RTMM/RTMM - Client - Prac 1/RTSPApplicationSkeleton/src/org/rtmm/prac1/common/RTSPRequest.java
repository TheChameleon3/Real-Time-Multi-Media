package org.rtmm.prac1.common;

public enum RTSPRequest {
	SETUP(0), PLAY(1), PAUSE(2), TEARDOWN(3), OPTIONS(4), DESCRIBE(5), UNKNOWN(6);
	
	private RTSPRequest(final int newRequestType){
		this.requestType = newRequestType;
	}
	
	private int requestType;
	
	public int getRequestType(){
		return this.requestType;
	}
	
	public static RTSPRequest fromString(String string) {
		try {
			return RTSPRequest.valueOf(string);
		} catch (Exception e) {
			return UNKNOWN;
		}
	}
}