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

package org.rtmm.prac1.common;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public abstract class Application extends JFrame {
	
	public static RTSPState state;
	
	
	/**
	 * timer used to send/receive data from the UDP socket
	 */
	public Timer timer;
	
	/**
	 * buffer used to store the images to send to the client
	 * 
	 * allocate enough memory for the buffer used to send/receive data from the server
	 */
	public byte[] buf = new byte[15000];
	
	protected RTSPPipe pipe;
	
	/**
	 * video file requested from the client
	 */
	public static String VideoFileName;
	
	/**
	 * Sequence number of RTSP messages within the session
	 */
	public int RTSPSeqNb = 0;
	
	/**
	 * socket to be used to send and receive UDP packets
	 */
	public DatagramSocket RTPsocket;
	
	/**
	 * Constructor
	 * @param name name of the type of application
	 */
	public Application(String name) {
		super(name);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				timer.stop();
				System.exit(0);
			}
		});
	}
}
