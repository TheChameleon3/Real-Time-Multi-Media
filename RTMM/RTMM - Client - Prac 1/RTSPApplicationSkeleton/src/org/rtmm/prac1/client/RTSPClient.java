/**
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.rtmm.prac1.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.net.ssl.HttpsURLConnection;

import org.rtmm.prac1.client.handlers.*;

import org.apache.log4j.Logger;
import org.rtmm.prac1.common.Application;
import org.rtmm.prac1.common.RTSPPipe;
import org.rtmm.prac1.common.RTSPRequest;
import org.rtmm.prac1.common.RTSPState;

/*Code Adapted from code given by Dr. Mosiuoa Tsietsi 
 * 
 * In collaboration with Cynthia Mahofa, Tamara Lancaster,, Matthew Towers, Mbali Ntlahla and others
 * 
 * references :
 * https://github.com/mutaphore/RTSP-Client-Server/blob/master/Client.java
 * https://www.ietf.org/rfc/rfc2326.txt
 * 
 *  
 */



/* Serialization is the process of turning an object in memory into a stream of bytes for the purpose
 * of storing it or sending it over a network.
 * 
 * 
 *
 */

/*If a class implements the interface java.io.Serializable, directly or indirectly, provide a field 
 *called serialVersionUID. If this filed is not provided, an error will be returned at compile time. 
 *However, the warning can be suppressed using the line of code below.
 */

@SuppressWarnings("serial")
public class RTSPClient extends Application  {
	
	/* A Logger object is used to log messages for a specific system or application component. In this
	 * case the application is RTSPClient.
	 */

	final static Logger logger = Logger.getLogger(RTSPClient.class);
	
	/*These are all a bunch of buttons.
	 */
	
	private final JButton setupButton = new JButton("Setup"),
			playButton = new JButton("Play"),
			pauseButton = new JButton("Pause"),
			tearButton = new JButton("Teardown"),
			optionsButton = new JButton("Options"),
			describeButton = new JButton("Describe");

	private final JPanel mainPanel = new JPanel(),
						buttonPanel = new JPanel();
	
	public final JLabel iconLabel = new JLabel();
	
	public static int RTP_RCV_PORT = 1024 + new Random().nextInt(64510); // set port higher than 1024 but less than upper port bound
	
	String RTSPid = "";
	
	static int MJPEG_TYPE = 26; 
	
	//This is the RTSPClient class, which is actually a GUI
	public RTSPClient() {
		super ("Client");
		
		// Buttons
		buttonPanel.setLayout(new GridLayout(1, 0));
		buttonPanel.add(setupButton);
		buttonPanel.add(playButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(tearButton);
		buttonPanel.add(optionsButton);
		buttonPanel.add(describeButton);
		
		setupButton.addActionListener(new SetupButtonListener(this));
		playButton.addActionListener(new PlayButtonListener(this));
		pauseButton.addActionListener(new PauseButtonListener(this));
		tearButton.addActionListener(new TearButtonListener(this));
		optionsButton.addActionListener(new OptionsButtonListener(this));
		describeButton.addActionListener(new DescribeButtonListener(this));
		
		// Image display label
		iconLabel.setIcon(null);
		
		// frame layout
		mainPanel.setLayout(null);
		mainPanel.add(iconLabel);
		mainPanel.add(buttonPanel);
		iconLabel.setBounds(0, 0, 580, 280);
		buttonPanel.setBounds(0, 280, 580, 50);
		
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		setSize(new Dimension(600, 370));
		setVisible(true);
		
		timer = new Timer(20, new TimerListener(this));
		timer.setInitialDelay(0);
		timer.setCoalesce(true);
	}	
	
	public static void main(String argv[]) throws Exception {		
		//Create a new RTSPClient instance
		RTSPClient theClient = new RTSPClient();
		
		// download the HTTP file and extract the parameters from the metadata (This is done using a HTTP GET request)
		String[] para = sendGET();	
		int somePort = Integer.parseUnsignedInt(para[1]);
		
		theClient.openConnection(para[0], somePort);
		VideoFileName = para[2];
	}

	private static String[] sendGET() throws Exception {
		//fetch the metadata
		String url = "http://146.231.122.116:8280/metadata/metadata.asx";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//optional default GET
		con.setRequestMethod("GET");
		
		//add request header
		con.setRequestProperty("User-Agen", "Chrome");
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL: " + url);
		System.out.println("Response Code: " + responseCode);
		
		//download file from url
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine); //Just want to get url
		}
		in.close();
		
		//extract IP address and port number from the downloaded file
		int startIndex = response.indexOf("//") + 2;
		
		String ip = "";
		String port = "";
		String fileName ="";
		
		char ch = response.charAt(startIndex);
		
		while (ch != ':' )
		{
			ip += response.charAt(startIndex);
			startIndex++;
			ch = response.charAt(startIndex);
		}
		
		ch = response.charAt(++startIndex);
		
		while (ch != '/')
		{			
			port += response.charAt(startIndex);
			startIndex++;
			ch = response.charAt(startIndex);
		}
		
		ch = response.charAt(++startIndex);
		
		while (ch != '"')
		{
			fileName += response.charAt(startIndex);
			startIndex++;
			ch = response.charAt(startIndex);
		}
		
		String [] arg = new String [3];
		arg[0] = ip;
		arg[1] = port;
		arg[2] = fileName;
		
		return arg;
		
		/*print result
		System.out.println(response.toString() + "\n" + ip + "\n" + port + "\n" + fileName);
		*/
	}

	public DatagramPacket getPacket() {
		return new DatagramPacket(buf, buf.length);
	}
		
	public void openConnection(String host, int port) throws IOException {
		InetAddress ServerIPAddr = InetAddress.getByName(host);
		
		// Use the RTSPPipe class to setup a TCP connection with the server to exchange RTSP messages
		try {
			Socket sock = new Socket(host, port);
			pipe = new RTSPPipe(sock);
			
			// set the state of the client to INIT
			
			RTSPClient.state = RTSPState.INIT;
		}
		catch(IOException io) {
			
		}
	}
	
	/*
	 * TODO fill in the parseRespone method to parse the response from the server
	 */
	
	public int parseResponse() {
		int reply_code = 0;
		
		// use RTSPPipe to parse status line and extract the reply_code
		
		logger.info("RTSP Client - Received from Server: ");
		
		String Status = "";
		
		// if reply code is 200 OK get and print the 2 other lines
		try {
            //parse status line and extract the reply_code:
            Status = pipe.read();
            System.out.println("RTSP Client - Received from Server:");
            System.out.println(Status);
            
            StringTokenizer tokens = new StringTokenizer(Status);
            //skip RTSP version
            tokens.nextToken(); 
            //retrieve the next token (reply code)
            reply_code = Integer.parseInt(tokens.nextToken());
            
            //if reply code is OK get and print the 2 other lines
            if (reply_code == 200) {
            	
                System.out.println(pipe.read());
                String Nxtline = pipe.read();
                System.out.println(Nxtline);
                
                
                tokens = new StringTokenizer(Nxtline);
                String testCase = tokens.nextToken();
                //if state == INIT && Nxtline[0] == Session; get RSTPid 
                if (RTSPClient.state == RTSPState.INIT && testCase.compareTo("Session:") == 0) {
                    RTSPid = tokens.nextToken();
                }
                // Basically if Describe is pressed this should be called (check what is in nxtline)
                else  {
                    // Get the DESCRIBE lines
                    while(pipe.canRead()) {
                        System.out.println(pipe.read());
                    }
                }
            }
        } catch(Exception ex) {
            System.out.println("Exception caught: "+ex);
            System.exit(0);
        }

		return reply_code;
	}
	
	/*
	 * TODO fill in the sendRequest method to send the request to the server based on button pressed
	 * @param request_type The type of request to send
	 */
	
	public void sendRequest(RTSPRequest request_type) {
		logger.info("Preparing request for server ...");
		try {
			// Use RTSPPipe variable to write to the RTSP socket
			System.out.println(request_type.name() + " " + VideoFileName + " RTSP/1.0");
			// write the request line:
			this.pipe.write(request_type.name() + " " + VideoFileName + " RTSP/1.0");
			
			// write the CSeq line:
			this.pipe.write("CSeq: " + this.RTSPSeqNb++);
			
			// set content depending on request type.
			if (request_type == RTSPRequest.SETUP) {
                this.pipe.write("Transport: RTP/UDP; client_port= " + RTP_RCV_PORT);
            }
            else if (request_type == RTSPRequest.DESCRIBE) {
                this.pipe.write("Accept: application/sdp");
            }
            else {
                //else, write the Session line from the RTSPid field
            	this.pipe.write("Session: " + RTSPid);
            }

			this.pipe.flush();
		}
		catch(IOException io) {
			io.printStackTrace();
			
		}
			
	}
}

