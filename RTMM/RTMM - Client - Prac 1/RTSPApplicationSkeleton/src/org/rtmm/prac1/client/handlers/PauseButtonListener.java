package org.rtmm.prac1.client.handlers;

import org.rtmm.prac1.client.RTSPClient;
import org.rtmm.prac1.common.RTSPRequest;
import org.rtmm.prac1.common.RTSPState;
import org.rtmm.prac1.common.handlers.ButtonListener;

public class PauseButtonListener extends ButtonListener<RTSPClient> {
	public PauseButtonListener(RTSPClient client) {
		super(client);
	}
	
	public void actionPerformed() {
		System.out.println("Pause Button pressed !");
		
		// send a PLAY request and await and handle response from server
		// note client MUST already be in PLAYING state
		// see RTSP RFC Sec 10.6
		
		// else if state != PLAYING then do nothing
		if (RTSPClient.state == RTSPState.PLAYING) {
			//increase RTSP sequence number
			app.RTSPSeqNb++;
			
			// Send PAUSE message to the server
			app.sendRequest(RTSPRequest.PAUSE);
			System.out.println("Sending pause message !");
			
			// Make sure response is 200 - OK
			if (app.parseResponse() != 200) {
				System.err.println("Invalid Server Response");
				return;
			}
			else {
                //change RTSP state and print out new state
                RTSPClient.state = RTSPState.READY;
                System.out.println("New RTSP state: READY");

                //start the timer
                app.timer.stop();
                
                /*Um...what is this?
                 * rtcpSender.startSend();
                 */
			}
		}//else if state != READY then do nothing
	}
}

