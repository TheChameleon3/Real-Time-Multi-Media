package org.rtmm.prac1.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RTSPPipe {
	protected static final String CRLF = "\r\n";
	
	// input and output stream filters
	protected BufferedReader RTSPBufferedReader;
	protected BufferedWriter RTSPBufferedWriter;
	
	/**
	 * socket used to send/receive RTSP messages
	 */
	protected Socket socket;
	
	public RTSPPipe(Socket sock) throws IOException {
		socket = sock;
		// Set input and output stream filters:
		RTSPBufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}
	
	public void close() throws IOException {
		socket.close();
	}
	
	public boolean canRead() throws IOException {
		return RTSPBufferedReader.ready();
	}
	
	public String read() throws IOException {
		return RTSPBufferedReader.readLine();
	}
	
	public void write(String line) throws IOException {
		RTSPBufferedWriter.write(line + CRLF);
	}
	
	public void flush() throws IOException {
		RTSPBufferedWriter.flush();
	}
}
