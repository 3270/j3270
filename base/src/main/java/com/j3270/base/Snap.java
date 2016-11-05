/*
 * Copyright (C) 2016 Daniel Yokomizo
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.j3270.base;


import static com.j3270.base.Extras.checkArgument;
import static com.j3270.base.Extras.checkNotNull;
import static com.j3270.base.WaitMode.Output;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.List;

/**
 * @author Daniel Yokomizo
 */
public final class Snap {
	private final J3270 j3270;

	Snap(J3270 j3270) {
		this.j3270 = j3270;
	}

	/**
	 * Snap(Ascii) action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ascii() throws J3270Exception {
		j3270.snap("Snap(Ascii)");
		return j3270.getData();
	}

	/**
	 * Snap(Ascii,length) action.
	 * 
	 * @param length
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           if the length is negative
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ascii(int length) throws J3270Exception {
		checkArgument(length >= 0, "Invalid length: %d", length);
		j3270.snap("Snap(Ascii," + length + ")");
		return j3270.getData();
	}

	/**
	 * Snap(Ascii,row,col,length) action.
	 * 
	 * @param row
	 * @param col
	 * @param length
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           if row is negative
	 *           if col is negative
	 *           if the length is negative
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ascii(int row, int col, int length) throws J3270Exception {
		checkArgument(row >= 0, "Invalid row: %d", row);
		checkArgument(col >= 0, "Invalid col: %d", col);
		checkArgument(length >= 0, "Invalid length: %d", length);
		j3270.snap("Snap(Ascii," + row + "," + col + "," + length + ")");
		return j3270.getData();
	}

	/**
	 * Snap(Ascii,row,col,rows,cols) action.
	 * 
	 * @param row
	 * @param col
	 * @param rows
	 * @param cols
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           if row is negative
	 *           if col is negative
	 *           if rows is negative
	 *           if cols is negative
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ascii(int row, int col, int rows, int cols) throws J3270Exception {
		checkArgument(row >= 0, "Invalid row: %d", row);
		checkArgument(col >= 0, "Invalid col: %d", col);
		checkArgument(rows >= 0, "Invalid rows: %d", rows);
		checkArgument(cols >= 0, "Invalid cols: %d", cols);
		j3270.snap("Snap(Ascii," + row + "," + col + "," + rows + "," + cols + ")");
		return j3270.getData();
	}

	/**
	 * Snap(Cols) action.
	 * 
	 * @return the number of columns
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public int cols() throws J3270Exception {
		j3270.snap("Snap(Cols)");
		return getInt();
	}

	/**
	 * Snap(Ebcdic) action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ebcdic() throws J3270Exception {
		j3270.snap("Snap(Ebcdic)");
		return j3270.getData();
	}

	/**
	 * Snap(Ebcdic,length) action.
	 * 
	 * @param length
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           if the length is negative
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ebcdic(int length) throws J3270Exception {
		checkArgument(length >= 0, "Invalid length: %d", length);
		j3270.snap("Snap(Ebcdic," + length + ")");
		return j3270.getData();
	}

	/**
	 * Snap(Ebcdic,row,col,length) action.
	 * 
	 * @param row
	 * @param col
	 * @param length
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           if row is negative
	 *           if col is negative
	 *           if the length is negative
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ebcdic(int row, int col, int length) throws J3270Exception {
		checkArgument(row >= 0, "Invalid row: %d", row);
		checkArgument(col >= 0, "Invalid col: %d", col);
		checkArgument(length >= 0, "Invalid length: %d", length);
		j3270.snap("Snap(Ebcdic," + row + "," + col + "," + length + ")");
		return j3270.getData();
	}

	/**
	 * Snap(Ebcdic,row,col,rows,cols) action.
	 * 
	 * @param row
	 * @param col
	 * @param rows
	 * @param cols
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           if row is negative
	 *           if col is negative
	 *           if rows is negative
	 *           if cols is negative
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ebcdic(int row, int col, int rows, int cols) throws J3270Exception {
		checkArgument(row >= 0, "Invalid row: %d", row);
		checkArgument(col >= 0, "Invalid col: %d", col);
		checkArgument(rows >= 0, "Invalid rows: %d", rows);
		checkArgument(cols >= 0, "Invalid cols: %d", cols);
		j3270.snap("Snap(Ebcdic," + row + "," + col + "," + rows + "," + cols + ")");
		return j3270.getData();
	}

	/**
	 * Snap(ReadBuffer) action.
	 * 
	 * @param mode
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> readBuffer(ReadBufferMode mode) throws J3270Exception {
		checkNotNull(mode, "mode");
		j3270.snap("Snap(ReadBuffer," + mode.name() + ")");
		return j3270.getData();
	}

	/**
	 * Snap(Rows) action.
	 * 
	 * @return the number of rows
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public int rows() throws J3270Exception {
		j3270.snap("Snap(Rows)");
		return getInt();
	}

	/**
	 * Snap(Save) action.
	 * 
	 * @return the number of rows
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public void save() throws J3270Exception {
		j3270.snap("Snap(Save)");
	}

	/**
	 * Snap(Status) action.
	 * 
	 * @return the status
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public Status status() throws J3270Exception {
		j3270.snap("Snap(Status)");
		final List<String> l = j3270.getData();
		final String s = l.isEmpty() ? "" : l.get(0);
		return new Status(s + "\nok\n");
	}

	/**
	 * Snap(Wait) action.
	 * 
	 * @param mode
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public void wait(WaitMode mode) throws J3270Exception {
		checkNotNull(mode, "mode");
		checkArgument(Output == mode, "Invalid WaitMode: %s", mode);
		j3270.snap("Snap(Wait,Output)");
	}

	/**
	 * Snap(Wait) action.
	 * 
	 * @param timeout
	 * @param mode
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public void wait(Timeout timeout, WaitMode mode) throws J3270Exception {
		checkNotNull(timeout, "timeout");
		checkNotNull(mode, "mode");
		checkArgument(Output == mode, "Invalid WaitMode: %s", mode);
		final long s = timeout.to(SECONDS);
		if (s == 0) {
			j3270.snap("Snap(Wait,Output)");
		} else {
			j3270.snap("Snap(Wait," + s + ",Output)", timeout);
		}
	}

	private int getInt() {
		final List<String> l = j3270.getData();
		final String s = l.isEmpty() ? "0" : l.get(0);
		return Integer.parseInt(s);
	}
}
