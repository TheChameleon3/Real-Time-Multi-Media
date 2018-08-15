package org.rtmm.prac1.client.handlers;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;

import javax.swing.ImageIcon;

import org.rtmm.prac1.client.RTSPClient;
import org.rtmm.prac1.common.RTPpacket;
import org.rtmm.prac1.common.handlers.ButtonListener;

public class TimerListener extends ButtonListener<RTSPClient> {
	public TimerListener(RTSPClient client) {
		super(client);
	}
	
	public void actionPerformed() {
		// Construct a DatagramPacket to receive data from the UDP socket
		DatagramPacket rcvdp = app.getPacket();
		
		try {

            //System.out.println("****");
			// receive the DP from the socket:
			app.RTPsocket.receive(rcvdp);
            //System.out.println("**0**");
			
			// create an RTPpacket object from the DP
			RTPpacket rtpPacket = new RTPpacket(rcvdp.getData(), rcvdp.getLength());
			// print important header fields of the RTP packet received:
			//System.out.println("Got RTP packet with SeqNum # " + rtp_packet.getSequenceNumber() + " TimeStamp " + rtp_packet.getTimestamp() + " ms, of type " + rtp_packet.getPayloadType());
			System.out.println("Got RTP packet with SeqNum # " + rtpPacket.getSequenceNumber() + " of type " + rtpPacket.getPayloadType());
			// print header bitstream:
			rtpPacket.printHeader();
            
			// get the payload bitstream from the RTPpacket object
			int payload_length = rtpPacket.getPayloadSize();
			byte[] payload = new byte[payload_length];
			rtpPacket.getPayload(payload);

			// get an Image object from the payload bitstream
			Image image = Toolkit.getDefaultToolkit().createImage(payload, 0, payload_length);
			
			// display the image as an ImageIcon object
			app.iconLabel.setIcon(new ImageIcon(image));
            
		} catch (InterruptedIOException iioe) {
			System.out.println("Nothing to read");
		} catch (IOException ioe) {
			System.out.println("Exception caught in client timer: " + ioe);
		}
	}
}