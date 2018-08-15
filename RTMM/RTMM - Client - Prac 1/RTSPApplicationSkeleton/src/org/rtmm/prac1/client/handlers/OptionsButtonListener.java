package org.rtmm.prac1.client.handlers;

import org.rtmm.prac1.client.RTSPClient;
import org.rtmm.prac1.common.RTSPRequest;
import org.rtmm.prac1.common.handlers.ButtonListener;

public class OptionsButtonListener extends ButtonListener<RTSPClient> {
	public OptionsButtonListener(RTSPClient client) {
		super(client);
	}
	
	public void actionPerformed() {
		System.out.println("Options Button pressed !");
		
		// send a PLAY request and await and handle response from server
		// a client can send an OPTIONS at any time once connected
		// see RTSP RFC Sec 10.1
		app.RTSPSeqNb++;
		//app.sendRequest(RTSPRequest.OPTIONS);
	    //Send DESCRIBE message to the server
	    app.sendRequest(RTSPRequest.OPTIONS);
	
		// Make sure response is 200 - OK 
	    if (app.parseResponse() != 200) {
	        System.out.println("Invalid Server Response");
	    }
	    else {     
	        System.out.println("Received response for PLAY");
	    }
	}
}