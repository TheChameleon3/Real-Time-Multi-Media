package org.rtmm.prac1.client.handlers;

import java.net.DatagramSocket;
import java.net.SocketException;

import org.rtmm.prac1.client.RTSPClient;
import org.rtmm.prac1.common.RTSPRequest;
import org.rtmm.prac1.common.RTSPState;
import org.rtmm.prac1.common.handlers.ButtonListener;

public class SetupButtonListener extends ButtonListener<RTSPClient> {
	public SetupButtonListener(RTSPClient client) {
		super(client);
	}
	
	public void actionPerformed() {
		System.out.println("Setup Button pressed !");
		if (RTSPClient.state == RTSPState.INIT) {
			try {
				// construct a new DatagramSocket to receive RTP packets
				// from the server, on port RTP_RCV_PORT
				app.RTPsocket = new DatagramSocket(RTSPClient.RTP_RCV_PORT);
				// set TimeOut value
				app.timer.setDelay(5);
			} catch (SocketException se) {
				System.exit(0);
			}
			
			// init RTSP sequence number
			app.RTSPSeqNb = 1;
			
			// Send SETUP message to the server
			System.out.println("Sending setup message !");
			app.sendRequest(RTSPRequest.SETUP);
			System.out.println("Sent !");
			
			// Make sure response is 200 - OK
			if (app.parseResponse() != 200) {
				System.err.println("Invalid Server Response");
				return;
			}
			else {
				//change RTSP state and print out new state
                RTSPClient.state = RTSPState.READY;
                System.out.println("New RTSP state: READY");
			}
		}
	}
}