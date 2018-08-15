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

import java.io.*;

public class VideoStream {
	
	private FileInputStream fis;
	//private int frame_nb = 0;
	
	public VideoStream(String filename) throws Exception {
		fis = new FileInputStream(filename);
	}
	
	/**
	 * @return the next frame as an array of byte and the size of the frame
	 */
	public int getNextFrame(byte[] frame) throws Exception {
		int length = 0;
		String length_string;
		byte[] frame_length = new byte[5];

		// read current frame length
		fis.read(frame_length, 0, 5);

		// transform frame_length to integer
		length_string = new String(frame_length);
		length = Integer.parseInt(length_string);

		return fis.read(frame, 0, length);
	}
}