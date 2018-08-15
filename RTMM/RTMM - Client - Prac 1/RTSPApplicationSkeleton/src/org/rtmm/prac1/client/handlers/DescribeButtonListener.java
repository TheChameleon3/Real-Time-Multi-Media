package org.rtmm.prac1.client.handlers;

import org.rtmm.prac1.client.RTSPClient;
import org.rtmm.prac1.common.RTSPRequest;
import org.rtmm.prac1.common.RTSPState;
import org.rtmm.prac1.common.handlers.ButtonListener;

public class DescribeButtonListener extends ButtonListener<RTSPClient> {
	public DescribeButtonListener(RTSPClient client) {
		super(client);
	}
	
	public void actionPerformed() {
		System.out.println("Describe Button pressed !");
		
		// send a DESCRIBE request and await and handle response from server
		// a client usually sends a DEZCRIBE before issuing a PLAY request
		// see RTSP RFC Sec 10.2
	    //increase RTSP sequence number
		if(RTSPClient.state == RTSPState.INIT || RTSPClient.state == RTSPState.READY)
		{
		    app.RTSPSeqNb++;
		    
		    //Send DESCRIBE message to the server
		    app.sendRequest(RTSPRequest.DESCRIBE);
		
			// Make sure response is 200 - OK
		    if (app.parseResponse() != 200) {
		        System.out.println("Invalid Server Response");
		    }
		    else {
		    	
		        System.out.println("Received response for DESCRIBE");
		    }
		}
		else{
			System.out.println("Describe cannot be performed during play time");
		}
		
	}
}
