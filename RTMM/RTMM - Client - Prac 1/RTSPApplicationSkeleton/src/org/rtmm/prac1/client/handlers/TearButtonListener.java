package org.rtmm.prac1.client.handlers;

import org.rtmm.prac1.client.RTSPClient;
import org.rtmm.prac1.common.RTSPRequest;
import org.rtmm.prac1.common.RTSPState;
import org.rtmm.prac1.common.handlers.ButtonListener;

public class TearButtonListener extends ButtonListener<RTSPClient> {
	public TearButtonListener(RTSPClient client) {
		super(client);
	}

	public void actionPerformed() {
		System.out.println("Teardown Button pressed !");
		
		// send a TEARDOWN request and await and handle response from server
		// a client can send a TEARDOWN request ONLY after a SETUP has been issued
		// see RTSP RFC Sec 10.7
		
		if(RTSPClient.state == RTSPState.INIT){
			System.out.println("Send TEARDOWN message !");
			System.exit(0);
		}
		
		
		if(RTSPClient.state == RTSPState.READY || RTSPClient.state == RTSPState.PLAYING)
		{
		app.sendRequest(RTSPRequest.TEARDOWN);
		//System.out.println(app.RTSPSeqNb);
		
			//increase RTSP sequence number
			app.RTSPSeqNb++;
			
	        //Send TEARDOWN message to the server
			System.out.println("Send TEARDOWN message !");
			//app.sendRequest(RTSPRequest.TEARDOWN);
			// Make sure response is 200 - OK
			if (app.parseResponse() != 200) {
				System.err.println("Invalid Server Response");
				return;
			}
			else {
                //change RTSP state and print out new state
                RTSPClient.state = RTSPState.INIT;
                System.out.println("Hope you enjoyed your watching experience, Goodbye :)");

                //stop the timer
                app.timer.stop();

                //exit
                System.exit(0);
			}	
		}			
	}
}