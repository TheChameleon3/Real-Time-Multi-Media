package org.rtmm.prac1.client.handlers;

import org.rtmm.prac1.client.RTSPClient;
import org.rtmm.prac1.common.RTSPRequest;
import org.rtmm.prac1.common.RTSPState;
import org.rtmm.prac1.common.handlers.ButtonListener;

public class PlayButtonListener extends ButtonListener<RTSPClient> {
	public PlayButtonListener(RTSPClient client) {
		super(client);
	}
	
	public void actionPerformed() {
		System.out.println("Play Button pressed !");
		
		// send a PLAY request and await and handle response from server
		// a client can send a PLAY request ONLY if it is in READY state
		// see RTSP RFC Sec 10.5
		
		// else if state != READY then do nothing
		if (RTSPClient.state == RTSPState.READY) {
			//increase RTSP sequence number
			app.RTSPSeqNb++;
			
			// Send PLAY message to the server
			app.sendRequest(RTSPRequest.PLAY);
			System.out.println("Sending play message !");
			
			// Make sure response is 200 - OK
			if (app.parseResponse() != 200) {
				System.err.println("Invalid Server Response");
				return;
			}
			else {
                //change RTSP state and print out new state
                RTSPClient.state = RTSPState.PLAYING;
                System.out.println("New RTSP state: PLAYING");

                //start the timer
                app.timer.start();
                
                 //rtcpSender.startSend();
			}			
		}//else if state != READY then do nothing
	}
}
