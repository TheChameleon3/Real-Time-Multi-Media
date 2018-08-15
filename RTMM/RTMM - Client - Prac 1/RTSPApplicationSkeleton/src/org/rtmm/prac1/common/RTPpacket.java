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

public class RTPpacket {
	
	// size of the RTP header:
	private static int HEADER_SIZE = 12;
	
	// RTP header Fields
	public int Version = 2;
	public int Padding = 0;
	public int Extension = 0;
	public int CC = 0;
	public int Marker = 0;
	public int Ssrc = 0;
	
	private int PayloadType;
	private int SequenceNumber;
	private int TimeStamp;
	
	/**
	 * Bitstream of the RTP header
	 */
	private byte[] header = new byte[HEADER_SIZE];
	
	/**
	 * size of the RTP payload
	 */
	private int payload_size;
	/**
	 * Bitstream of the RTP payload
	 */
	private byte[] payload;
	
	/**
	 * Constructor of an RTPpacket object from header fields and payload bitstream
	 * @param PType
	 * @param Framenb
	 * @param Time
	 * @param data
	 * @param data_length
	 */
	public RTPpacket(int PType, int Framenb, int Time, byte[] data, int data_length) {
		// fill changing header fields:
		SequenceNumber = Framenb;
		TimeStamp = Time;
		PayloadType = PType;
		
		// build the header bistream:
		
		// .............
		// TO COMPLETE
		// .............
		// fill the header array of byte with RTP header fields
		header[0] = (byte) (0 | Version << 6 | Padding << 5 | Extension << 4 | CC);
		header[1] = (byte) (0 | Marker << 7 | PayloadType);
		header[2] = (byte) (SequenceNumber >> 8);
		header[3] = (byte) (SequenceNumber & 0xFF);
		header[4] = (byte) (TimeStamp >> 24);
		header[5] = (byte) ((TimeStamp >> 16) & 0xFF);
		header[6] = (byte) ((TimeStamp >> 8) & 0xFF);
		header[7] = (byte) (TimeStamp & 0xFF);
		header[8] = (byte) (Ssrc >> 24);
		header[9] = (byte) ((Ssrc >> 16) & 0xFF);
		header[10] = (byte) ((Ssrc >> 8) & 0xFF);
		header[11] = (byte) (Ssrc & 0xFF);
		
		// fill the payload bitstream:
		payload_size = data_length;
		payload = new byte[data_length];
		
		// fill payload array of byte from data (given in parameter of the constructor)
		for (int i = 0; i < payload_size; i++) {
			payload[i] = data[i];
		}
		
		// TODO: Do not forget to uncomment method printheader() below !
	}
	
	/**
	 * Constructor of an RTPpacket object from the packet bistream
	 * @param packet
	 * @param packet_size
	 */
	public RTPpacket(byte[] packet, int packet_size) {
		
		// check if total packet size is lower than the header size
		if (packet_size >= HEADER_SIZE) {
			// get the header bitsream:
			for (int i = 0; i < HEADER_SIZE; i++) {
				header[i] = packet[i];
			}
			
			// get the payload bitstream:
			payload_size = packet_size - HEADER_SIZE;
			payload = new byte[payload_size];
			for (int i = HEADER_SIZE; i < packet_size; i++) {
				payload[i - HEADER_SIZE] = packet[i];
			}
			
			// interpret the changing fields of the header:
			PayloadType = header[1] & 127;
			SequenceNumber = unsigned_int(header[3]) + 256 * unsigned_int(header[2]);
			TimeStamp = unsigned_int(header[7]) + 256 * unsigned_int(header[6]) + 65536 * unsigned_int(header[5]) + 16777216 * unsigned_int(header[4]);
		}
	}
	
	private int unsigned_int(int b) {
		return (int)((long)(b & 0xffffffff));
	}
	
	/**
	 * @param data
	 * @return the payload bitstream of the RTPpacket and its size
	 */
	public int getPayload(byte[] data) {
		for (int i = 0; i < payload_size; i++) {
			data[i] = payload[i];
		}
		return payload_size;
	}
	
	/**
	 * @return the length of the payload
	 */
	public int getPayloadSize() {
		return payload_size;
	}
	
	/**
	 * @return the total length of the RTP packet
	 */
	public int getLength() {
		return payload_size + HEADER_SIZE;
	}
	
	/**
	 * @param packet
	 * @return the packet bitstream and its length
	 */
	public int getPacket(byte[] packet) {
		// construct the packet = header + payload
		for (int i = 0; i < HEADER_SIZE; i++) {
			packet[i] = header[i];
		}
		for (int i = 0; i < payload_size; i++) {
			packet[i + HEADER_SIZE] = payload[i];
		}
		// return total size of the packet
		return payload_size + HEADER_SIZE;
	}
	
	public int getTimestamp() {
		return TimeStamp;
	}
	
	public int getSequenceNumber() {
		return SequenceNumber;
	}
	
	public int getPayloadType() {
		return PayloadType;
	}
	
	/**
	 * print headers without the SSRC
	 */
	public void printHeader() {
		System.out.print("[RTP-Header] ");
        System.out.println("Version: " + Version 
                           + ", Padding: " + Padding
                           + ", Extension: " + Extension 
                           + ", CC: " + CC
                           + ", Marker: " + Marker 
                           + ", PayloadType: " + PayloadType
                           + ", SequenceNumber: " + SequenceNumber
                           + ", TimeStamp: " + TimeStamp);

	}
}