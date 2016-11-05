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
import static com.j3270.base.Extras.checkState;
import static com.j3270.base.Extras.escape;
import static com.j3270.base.Patterns.HEXDIGITS;
import static com.j3270.base.Patterns.HOST;
import static com.j3270.base.Patterns.HOSTFILE;
import static com.j3270.base.Patterns.HOSTPORT;
import static com.j3270.base.Patterns.KEY;
import static com.j3270.base.Patterns.STRING;
import static com.j3270.base.Safety.SAFE;
import static com.j3270.base.Safety.UNSAFE;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the interaction with a s3270 process.
 * 
 * Some actions are unsafe (e.g. may be exploited by command injection) and are unavailable as default. The
 * {@link Safety} mode can be modified to {@link Safety#UNSAFE} allowing such operations. The caller must ensure the
 * safety by other means.
 * 
 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
 * @see https://www.owasp.org/index.php/Command_Injection
 * @author Daniel Yokomizo
 */
public final class J3270 implements Cloneable, Closeable {
	private final Piper piper;
	private Timeout blocking;
	private Timeout nonBlocking;
	private List<String> data;
	private String message;
	private Status status;
	private Safety safety;
	private Object builder;

	/**
	 * Creates a new {@link J3270} using an underlying {@code s3270} process.
	 * 
	 * @throws J3270Exception
	 *           if the underling {@code s3270} process fails to start
	 */
	public J3270() throws J3270Exception {
		this(newProcessPiper("s3270"));
	}

	/**
	 * Creates a new {@link J3270} using a {@code Piper} to communicate via the {@code s3270} interface.
	 */
	public J3270(Piper piper) {
		checkNotNull(piper, "piper");
		this.piper = piper;
		this.blocking = new Timeout(30, SECONDS);
		this.nonBlocking = new Timeout(3, SECONDS);
		this.safety = SAFE;
	}

	/**
	 * Attn action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void attn() throws J3270Exception {
		perform("Attn", blocking);
	}

	/**
	 * BackSpace action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void backSpace() throws J3270Exception {
		perform("BackSpace", nonBlocking);
	}

	/**
	 * BackTab action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void backTab() throws J3270Exception {
		perform("BackTab", nonBlocking);
	}

	/**
	 * CircumNot action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void circumNot() throws J3270Exception {
		perform("CircumNot", nonBlocking);
	}

	/**
	 * Clear action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void clear() throws J3270Exception {
		perform("Clear", nonBlocking);
	}

	/**
	 * Connect action.
	 * 
	 * @param hostport
	 *          Hostname and (optional) port information
	 * @throws IllegalArgumentException
	 *           if the host information is not in the expected format
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 * @see Patterns#HOSTPORT
	 */
	public void connect(String hostport) throws IllegalArgumentException, J3270Exception {
		checkNotNull(hostport, "hostport");
		checkArgument(HOSTPORT.matcher(hostport).matches(), "Invalid hostport: %s", hostport);
		perform("Connect(" + hostport + ")", blocking);
	}

	/**
	 * Connect action.
	 * 
	 * @param host
	 *          Hostname information
	 * @param port
	 *          Port information
	 * @throws IllegalArgumentException
	 *           if the host information is not in the expected format
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 * @see Patterns#HOST
	 */
	public void connect(String host, int port) throws IllegalArgumentException, J3270Exception {
		checkNotNull(host, "host");
		checkArgument(HOST.matcher(host).matches(), "Invalid host: %s", host);
		checkArgument((port >= 0) && (port <= Short.MAX_VALUE), "Invalid port: %d", port);
		perform("Connect(" + host + ":" + port + ")", blocking);
	}

	/**
	 * CursorSelect action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void cursorSelect() throws J3270Exception {
		perform("CursorSelect", blocking);
	}

	/**
	 * Delete action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void delete() throws J3270Exception {
		perform("Delete", nonBlocking);
	}

	/**
	 * DeleteField action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void deleteField() throws J3270Exception {
		perform("DeleteField", nonBlocking);
	}

	/**
	 * DeleteWord action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void deleteWord() throws J3270Exception {
		perform("DeleteWord", nonBlocking);
	}

	/**
	 * Disconnect action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void disconnect() throws J3270Exception {
		perform("Disconnect", blocking);
	}

	/**
	 * Down action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void down() throws J3270Exception {
		perform("Down", nonBlocking);
	}

	/**
	 * Dup action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void dup() throws J3270Exception {
		perform("Dup", nonBlocking);
	}

	/**
	 * Enter action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void enter() throws J3270Exception {
		perform("Enter", blocking);
	}

	/**
	 * Erase action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void erase() throws J3270Exception {
		perform("Erase", nonBlocking);
	}

	/**
	 * EraseEOF action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void eraseEOF() throws J3270Exception {
		perform("EraseEOF", nonBlocking);
	}

	/**
	 * EraseInput action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void eraseInput() throws J3270Exception {
		perform("EraseInput", nonBlocking);
	}

	/**
	 * Execute action.
	 * 
	 * @param command
	 *          shell command to be executed
	 * @return the command's output
	 * @throws IllegalStateException
	 *           if the {@link Safety} mode is not set to {@link Safety#UNSAFE}
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public String execute(String command) throws IllegalStateException, J3270Exception {
		checkNotNull(command, "command");
		checkState(safety == UNSAFE, "Unsafe operation requires UNSAFE safety mode");
		return performRaw("Execute(" + command + ")", nonBlocking);
	}

	/**
	 * FieldEnd action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void fieldEnd() throws J3270Exception {
		perform("FieldEnd", nonBlocking);
	}

	/**
	 * FieldMark action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void fieldMark() throws J3270Exception {
		perform("FieldMark", nonBlocking);
	}

	/**
	 * HexString action.
	 * 
	 * @param hexDigits
	 *          hex digits
	 * @throws IllegalArgumentException
	 *           if the hexDigits contains any invalid characters
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 * @see Patterns#HEXDIGITS
	 */
	public void hexString(String hexDigits) throws IllegalArgumentException, J3270Exception {
		checkNotNull(hexDigits, "hexDigits");
		checkArgument(HEXDIGITS.matcher(hexDigits).matches(), "Invalid hexDigits: %s", hexDigits);
		perform("HexString(" + hexDigits + ")", nonBlocking);
	}

	/**
	 * Home action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void home() throws J3270Exception {
		perform("Home", nonBlocking);
	}

	/**
	 * Insert action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void insert() throws J3270Exception {
		perform("Insert", nonBlocking);
	}

	/**
	 * Interrupt action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void interrupt() throws J3270Exception {
		perform("Interrupt", blocking);
	}

	/**
	 * Key action.
	 * 
	 * @param key
	 *          a <i>keysym</i> or a <i>keycode</i>
	 * @throws IllegalArgumentException
	 *           if the key contains any invalid characters
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 * @see Patterns#KEY
	 */
	public void key(String key) throws IllegalArgumentException, J3270Exception {
		checkNotNull(key, "key");
		checkArgument(KEY.matcher(key).matches(), "Invalid key: %s", key);
		perform("Key(" + key + ")", nonBlocking);
	}

	/**
	 * Key action.
	 * 
	 * @param key
	 *          a <i>keycode</i>
	 * @throws IllegalArgumentException
	 *           if the key is not in [0..999]
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 * @see Patterns#KEY
	 */
	public void key(int key) throws IllegalArgumentException, J3270Exception {
		checkArgument((key >= 0) && (key <= 999), "Invalid key: %s", key);
		perform(String.format("Key(0%03d)", key), nonBlocking);
	}

	/**
	 * Left action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void left() throws J3270Exception {
		perform("Left", nonBlocking);
	}

	/**
	 * Left2 action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void left2() throws J3270Exception {
		perform("Left2", nonBlocking);
	}

	/**
	 * MonoCase action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void monoCase() throws J3270Exception {
		perform("MonoCase", nonBlocking);
	}

	/**
	 * MoveCursor action.
	 * 
	 * @param row
	 * @param col
	 * @throws IllegalArgumentException
	 *           if either row or col is negative
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void moveCursor(int row, int col) throws IllegalArgumentException, J3270Exception {
		checkArgument(row >= 0, "Invalid row: %d", row);
		checkArgument(col >= 0, "Invalid col: %d", col);
		perform("MoveCursor(" + row + "," + col + ")", nonBlocking);
	}

	/**
	 * NewLine action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void newLine() throws J3270Exception {
		perform("NewLine", nonBlocking);
	}

	/**
	 * NextWord action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void nextWord() throws J3270Exception {
		perform("NextWord", nonBlocking);
	}

	/**
	 * PA action.
	 * 
	 * @param n
	 * @throws IllegalArgumentException
	 *           if n is not in [1..3]
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void pa(int n) throws IllegalArgumentException, J3270Exception {
		checkArgument((n >= 1) && (n <= 3), "Invalid n (must be in [1..3]): %d", n);
		perform("PA(" + n + ")", blocking);
	}

	/**
	 * PF action.
	 * 
	 * @param n
	 * @throws IllegalArgumentException
	 *           if n is not in [1..24]
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void pf(int n) throws IllegalArgumentException, J3270Exception {
		checkArgument((n >= 1) && (n <= 24), "Invalid n (must be in [1..24]): %d", n);
		perform("PF(" + n + ")", blocking);
	}

	/**
	 * PreviousWord action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void previousWord() throws J3270Exception {
		perform("PreviousWord", nonBlocking);
	}

	public PrintText printText() {
		final PrintText pt = new PrintText(this);
		this.builder = pt;
		return pt;
	}

	/**
	 * Used by {@link FileTransfer#end()}
	 */
	void printText(String action) throws J3270Exception {
		checkState(builder instanceof PrintText, "Should be configuring a PrintText but was: %s", builder);
		builder = null;
		perform(action, nonBlocking);
	}

	/**
	 * Quit action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void quit() throws J3270Exception {
		perform("Quit", nonBlocking);
	}

	/**
	 * Redraw action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void redraw() throws J3270Exception {
		perform("Redraw", nonBlocking);
	}

	/**
	 * Reset action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void reset() throws J3270Exception {
		perform("Reset", nonBlocking);
	}

	/**
	 * Right action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void right() throws J3270Exception {
		perform("Right", nonBlocking);
	}

	/**
	 * Right2 action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void right2() throws J3270Exception {
		perform("Right2", nonBlocking);
	}

	/**
	 * Script action.
	 * 
	 * @param command
	 *          shell command to be executed
	 * @param arguments
	 *          arguments to the shell command
	 * @return the command's output
	 * @throws IllegalStateException
	 *           if the {@link Safety} mode is not set to {@link Safety#UNSAFE}
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public String script(String command, String...arguments) throws IllegalStateException, J3270Exception {
		checkNotNull(command, "command");
		checkState(safety == UNSAFE, "Unsafe operation requires UNSAFE safety mode");
		final StringBuilder sb = new StringBuilder();
		sb.append("Script(");
		sb.append(command);
		for (int i = 0; i < arguments.length; i++) {
			checkNotNull(arguments[i], "arguments[%d]", i);
			sb.append(',');
			sb.append(arguments[i]);
		}
		sb.append(')');
		return perform(sb.toString(), nonBlocking);
	}

	/**
	 * String action.
	 * 
	 * @param strings
	 * @throws IllegalArgumentException
	 *           the strings is empty or any of them contain invalid characters
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 * @see Patterns#STRING
	 */
	public void string(String...strings) throws IllegalArgumentException, J3270Exception {
		checkNotNull(strings, "strings");
		checkArgument(strings.length > 0, "Invalid strings: []");
		final StringBuilder sb = new StringBuilder();
		sb.append("String(");
		for (int i = 0; i < strings.length; i++) {
			final String s = checkString(strings[i], "Invalid strings[%d]: %s", i, strings[i]);
			if (i > 0) {
				sb.append(',');
			}
			sb.append('"');
			sb.append(s);
			sb.append('"');
		}
		sb.append(')');
		perform(sb.toString(), blocking);
	}

	/**
	 * SysReq action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void sysReq() throws J3270Exception {
		perform("SysReq", blocking);
	}

	/**
	 * Tab action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void tab() throws J3270Exception {
		perform("Tab", nonBlocking);
	}

	/**
	 * Toggle action.
	 * 
	 * @param option
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void toggle(ToggleOption option) throws J3270Exception {
		checkNotNull(option, "option");
		perform("Toggle(" + option.name() + ")", nonBlocking);
	}

	/**
	 * Toggle action.
	 * 
	 * @param option
	 * @param mode
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void toggle(ToggleOption option, ToggleMode mode) throws J3270Exception {
		checkNotNull(option, "option");
		checkNotNull(mode, "mode");
		perform("Toggle(" + option.name() + "," + mode.name() + ")", nonBlocking);
	}

	/**
	 * ToggleInsert action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void toggleInsert() throws J3270Exception {
		perform("ToggleInsert", nonBlocking);
	}

	/**
	 * ToggleReverse action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void toggleReverse() throws J3270Exception {
		perform("ToggleReverse", nonBlocking);
	}

	/**
	 * Transfer action builder.
	 * 
	 * @param localFile
	 * @param hostFile
	 * @return a {@link FileTransfer} to configure the action, must call {@link FileTransfer#end()} to perform the
	 *         transfer
	 * @throws IllegalArgumentException
	 *           if localFile is not an existing, readable, file.
	 *           if hostFile is not valid
	 * @see FileTransfer
	 * @see Patterns#HOSTFILE
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#File-Transfer
	 */
	public FileTransfer transfer(File localFile, String hostFile) throws IllegalArgumentException {
		checkNotNull(localFile, "localFile");
		checkNotNull(hostFile, "hostFile");
		final String localPath = localFile.getAbsolutePath();
		checkArgument(localFile.exists(), "Local file does not exist: %s", localPath);
		checkArgument(localFile.canRead(), "Local file is not readable: %s", localPath);
		checkArgument(localFile.isFile(), "Local file path does not denote a file: %s", localPath);
		checkArgument(localPath.indexOf('"') < 0, "Invalid localFile: %s", localPath);
		checkArgument(HOSTFILE.matcher(hostFile).matches(), "Invalid hostFile: %s", hostFile);
		checkState(builder == null, "Still configuring %s", builder);
		final FileTransfer ft = new FileTransfer(this, localPath, hostFile);
		this.builder = ft;
		return ft;
	}

	/**
	 * Used by {@link FileTransfer#end()}
	 */
	void transfer(String action) throws J3270Exception {
		checkState(builder instanceof FileTransfer, "Should be configuring a FileTransfer but was: %s", builder);
		builder = null;
		perform(action, blocking);
	}

	/**
	 * Up action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public void up() throws J3270Exception {
		perform("Up", nonBlocking);
	}

	/**
	 * AnsiText action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ansiText() throws J3270Exception {
		perform("AnsiText", nonBlocking);
		return getData();
	}

	/**
	 * Ascii action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ascii() throws J3270Exception {
		perform("Ascii", nonBlocking);
		return getData();
	}

	/**
	 * Ascii(length) action.
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
		perform("Ascii(" + length + ")", nonBlocking);
		return getData();
	}

	/**
	 * Ascii(row,col,length) action.
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
		perform("Ascii(" + row + "," + col + "," + length + ")", nonBlocking);
		return getData();
	}

	/**
	 * Ascii(row,col,rows,cols) action.
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
		perform("Ascii(" + row + "," + col + "," + rows + "," + cols + ")", nonBlocking);
		return getData();
	}

	/**
	 * AsciiField action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public String asciiField() throws J3270Exception {
		perform("AsciiField", nonBlocking);
		final List<String> l = getData();
		return l.isEmpty() ? "" : l.get(0);
	}

	/**
	 * CloseScript action.
	 * 
	 * @param status
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public void closeScript(int status) throws J3270Exception {
		perform("CloseScript(" + status + ")", nonBlocking);
	}

	/**
	 * ContinueScript action.
	 * 
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 * @see #pauseScript()
	 * @see #pauseScript(Timeout)
	 */
	public void continueScript() throws J3270Exception {
		perform("ContinueScript", nonBlocking);
	}

	/**
	 * ContinueScript action.
	 * 
	 * @param param
	 * @throws IllegalArgumentException
	 *           the param contains invalid characters
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 * @see Patterns#STRING
	 * @see #pauseScript()
	 * @see #pauseScript(Timeout)
	 */
	public void continueScript(String param) throws J3270Exception {
		checkNotNull(param, "param");
		final String s = escape(param);
		perform("ContinueScript(\"" + s + "\")", nonBlocking);
	}

	/**
	 * Ebcdic action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> ebcdic() throws J3270Exception {
		perform("Ebcdic", nonBlocking);
		return getData();
	}

	/**
	 * Ebcdic(length) action.
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
		perform("Ebcdic(" + length + ")", nonBlocking);
		return getData();
	}

	/**
	 * Ebcdic(row,col,length) action.
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
		perform("Ebcdic(" + row + "," + col + "," + length + ")", nonBlocking);
		return getData();
	}

	/**
	 * Ebcdic(row,col,rows,cols) action.
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
		perform("Ebcdic(" + row + "," + col + "," + rows + "," + cols + ")", nonBlocking);
		return getData();
	}

	/**
	 * EbcdicField action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public String ebcdicField() throws J3270Exception {
		perform("EbcdicField", nonBlocking);
		final List<String> l = getData();
		return l.isEmpty() ? "" : l.get(0);
	}

	/**
	 * Info action.
	 *
	 * @param message
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           the message contains invalid characters
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 * @see Patterns#STRING
	 */
	public void info(String message) throws J3270Exception {
		checkNotNull(message, "message");
		final String s = escape(message);
		perform("Info(\"" + s + "\")", nonBlocking);
	}

	/**
	 * Info action.
	 *
	 * @param text
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           the text contains invalid characters
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 * @see Patterns#STRING
	 */
	public void expect(String text) throws J3270Exception {
		checkNotNull(text, "text");
		final String s = escape(text);
		perform("Expect(\"" + s + "\")", nonBlocking);
	}

	/**
	 * Info action.
	 *
	 * @param text
	 * @param timeout
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           if the text contains invalid characters
	 *           if the timeout is negative
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 * @see Patterns#STRING
	 */
	public void expect(String text, int timeout) throws J3270Exception {
		checkNotNull(text, "text");
		final String s = escape(text);
		checkArgument(timeout >= 0, "Invalid timeout: %d", timeout);
		perform("Expect(\"" + s + "\"," + timeout + ")", nonBlocking);
	}

	/**
	 * PauseScript action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 * @see #continueScript()
	 * @see #continueScript(String)
	 */
	public String pauseScript() throws J3270Exception {
		return pauseScript(blocking);
	}

	/**
	 * PauseScript action.
	 * 
	 * @param timeout
	 *          duration to wait for the script to continue
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 * @see #continueScript()
	 * @see #continueScript(String)
	 */
	public String pauseScript(Timeout timeout) throws J3270Exception {
		checkNotNull(timeout, "timeout");
		perform("PauseScript", timeout);
		final List<String> l = getData();
		return l.isEmpty() ? "" : l.get(0);
	}

	/**
	 * Query action.
	 * 
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> query() throws J3270Exception {
		perform("Query", nonBlocking);
		return getData();
	}

	/**
	 * Query action.
	 * 
	 * @param keyword
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public String query(QueryKeyword keyword) throws J3270Exception {
		checkNotNull(keyword, "keyword");
		perform("Query(" + keyword.name() + ")", nonBlocking);
		final List<String> l = getData();
		return l.isEmpty() ? "" : l.get(0);
	}

	/**
	 * ReadBuffer action.
	 * 
	 * @param mode
	 * @return the data output
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> readBuffer(ReadBufferMode mode) throws J3270Exception {
		checkNotNull(mode, "mode");
		perform("ReadBuffer(" + mode.name() + ")", nonBlocking);
		return getData();
	}

	/**
	 * Snap action.
	 * if the action fails for any reason
	 * 
	 * @return a {@link Snap} to configure the action
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public Snap snap() {
		final Snap s = new Snap(this);
		this.builder = s;
		return s;
	}

	/**
	 * Used by {@link Snap}
	 */
	void snap(String action) throws J3270Exception {
		checkState(builder instanceof Snap, "Should be configuring a Snap but was: %s", builder);
		builder = null;
		perform(action, nonBlocking);
	}

	/**
	 * Used by {@link Snap}
	 */
	void snap(String action, Timeout timeout) throws J3270Exception {
		checkState(builder instanceof Snap, "Should be configuring a Snap but was: %s", builder);
		builder = null;
		perform(action, timeout);
	}

	/**
	 * Source action.
	 * 
	 * @param keyword
	 * @return the data output
	 * @throws IllegalArgumentException
	 *           if localFile is not an existing, readable, file.
	 *           if hostFile is not valid
	 * @throws IllegalStateException
	 *           if the {@link Safety} mode is not set to {@link Safety#UNSAFE}
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public List<String> source(File file) throws J3270Exception {
		checkNotNull(file, "file");
		final String path = file.getAbsolutePath();
		checkArgument(file.exists(), "File does not exist: %s", path);
		checkArgument(file.canRead(), "File is not readable: %s", path);
		checkArgument(file.isFile(), "File path does not denote a file: %s", path);
		checkArgument(path.indexOf('"') < 0, "Invalid file: %s", path);
		checkState(safety == UNSAFE, "Unsafe operation requires UNSAFE safety mode");
		perform("Source(\"" + path + "\")", nonBlocking);
		return getData();
	}

	/**
	 * Title action.
	 * 
	 * @param text
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public void title(String text) throws J3270Exception {
		final String s = escape(checkNotNull(text, "text"));
		perform("Title(\"" + s + "\")", nonBlocking);
	}

	/**
	 * Wait action.
	 * 
	 * @param mode
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public void wait(WaitMode mode) throws J3270Exception {
		checkNotNull(mode, "mode");
		perform("Wait(" + mode.code() + ")", blocking);
	}

	/**
	 * Wait action.
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
		final long s = timeout.to(SECONDS);
		if (s == 0) {
			perform("Wait(" + mode.code() + ")", blocking);
		} else {
			perform("Wait(" + s + "," + mode.code() + ")", timeout);
		}
	}

	/**
	 * WindowState action.
	 * 
	 * @param mode
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
	 */
	public void windowState(WindowMode mode) throws J3270Exception {
		checkNotNull(mode, "mode");
		perform("WindowState(" + mode.name() + ")", nonBlocking);
	}

	/**
	 * Perform a raw action, without any processing.
	 * 
	 * @param action
	 *          action string to be executed
	 * @return the action's output
	 * @throws IllegalStateException
	 *           if the {@link Safety} mode is not set to {@link Safety#UNSAFE}
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
	 */
	public String raw(String action) throws IllegalStateException, J3270Exception {
		checkNotNull(action, "action");
		checkState(safety == UNSAFE, "Unsafe operation requires UNSAFE safety mode");
		return perform(action, blocking);
	}

	/**
	 * @return the raw response message for the last action or null if no action was performed yet
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the data (as lines) for the last action or an empty {@link List} if no action was performed yet
	 */
	public List<String> getData() {
		return data;
	}

	/**
	 * @return the status for the last action or null if no action was performed yet
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * The default {@link Timeout} for blocking actions is 30s.
	 * 
	 * @return the {@link Timeout} configured for blocking actions
	 */
	public Timeout getBlocking() {
		return blocking;
	}

	/**
	 * Configures the {@link Timeout} for blocking actions
	 * 
	 * @param blocking
	 *          the new {@link Timeout} for blocking actions
	 */
	public void setBlocking(Timeout blocking) {
		checkNotNull(blocking, "blocking");
		this.blocking = blocking;
	}

	/**
	 * @param blocking
	 *          the {@link Timeout} for blocking actions
	 * @return a new {@link J3270} with the configured {@link Timeout} for blocking actions. This instance is unmodified.
	 * 
	 */
	public J3270 withBlocking(Timeout blocking) {
		checkNotNull(blocking, "blocking");
		final J3270 j = clone();
		j.setBlocking(blocking);
		return j;
	}

	/**
	 * The default {@link Timeout} for non-blocking actions is 3s.
	 * 
	 * @return the {@link Timeout} configured for non-blocking actions
	 */
	public Timeout getNonBlocking() {
		return nonBlocking;
	}

	/**
	 * Configures the {@link Timeout} for non-blocking actions
	 * 
	 * @param nonBlocking
	 *          the new {@link Timeout} for non-blocking actions
	 */
	public void setNonBlocking(Timeout nonBlocking) {
		checkNotNull(nonBlocking, "nonBlocking");
		this.nonBlocking = nonBlocking;
	}

	/**
	 * @param nonBlocking
	 *          the {@link Timeout} for non-blocking actions
	 * @return a new {@link J3270} with the configured {@link Timeout} for non-blocking actions. This instance is
	 *         unmodified.
	 * 
	 */
	public J3270 withNonBlocking(Timeout nonBlocking) {
		checkNotNull(nonBlocking, "nonBlocking");
		final J3270 j = clone();
		j.setNonBlocking(nonBlocking);
		return j;
	}

	/**
	 * @param timeout
	 *          the {@link Timeout} for both blocking and non-blocking actions
	 * @return a new {@link J3270} with the configured {@link Timeout} for both blocking and non-blocking actions. This
	 *         instance is unmodified.
	 * 
	 */
	public J3270 withTimeout(Timeout timeout) {
		checkNotNull(timeout, "timeout");
		final J3270 j = clone();
		j.setBlocking(timeout);
		j.setNonBlocking(timeout);
		return j;
	}

	/**
	 * The default {@link Safety} mode is {@link Safety#SAFE}.
	 * 
	 * @return the {@link Safety} mode
	 */
	public Safety getSafety() {
		return safety;
	}

	/**
	 * Configures the {@link Safety} mode
	 * 
	 * @param safety
	 *          the new {@link Safety} mode
	 */
	public void setSafety(Safety safety) {
		checkNotNull(safety, "safety");
		this.safety = safety;
	}

	/**
	 * @param safety
	 *          the {@link Safety} mode
	 * @return a new {@link J3270} with the configured {@link Safety} mode. This instance is unmodified.
	 */
	public J3270 withSafety(Safety safety) {
		checkNotNull(safety, "safety");
		final J3270 j = clone();
		j.setSafety(safety);
		return j;
	}

	@Override
	protected J3270 clone() {
		try {
			return (J3270) super.clone();
		} catch (CloneNotSupportedException impossible) {
			throw new UnsupportedOperationException(impossible);
		}
	}

	/**
	 * Closes the associated {@link Piper}.
	 */
	@Override
	public void close() throws IOException {
		piper.close();
	}

	private String perform(String action, Timeout timeout) throws J3270Exception {
		checkState(builder == null, "Still configuring %s", builder);
		checkState(piper.isRunning(), "Piper is not running, can not execute: %s", action);
		try {
			final String s = performRaw(action, timeout);
			processMessage(s);
			return s;
		} catch (Error e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new J3270Exception("Failed to execute: " + action, e);
		}
	}

	private void processMessage(String msg) {
		try {
			String s = msg;
			data = new ArrayList<>();
			while (s.startsWith("data: ")) {
				final int i = s.indexOf('\n');
				if (i < 0) {
					data.add(s.substring(6));
					s = null;
				} else {
					data.add(s.substring(6, i));
					s = s.substring(i + 1);
				}
			}
			status = new Status(s);
		} finally {
			data = Collections.unmodifiableList(data);
		}
	}

	void cancel() {
		builder = null;
	}

	private String performRaw(String action, Timeout timeout) throws J3270Exception {
		checkState(builder == null, "Still configuring %s", builder);
		checkState(piper.isRunning(), "Piper is not running, can not execute: %s", action);
		try {
			data = null;
			status = null;
			final String s = piper.pipe(action + "\n", timeout).replaceAll("[\r\n]+", "\n");
			message = s;
			return s;
		} catch (Exception e) {
			throw new J3270Exception("Failed to execute: " + action, e);
		}
	}

	private static ProcessPiper newProcessPiper(String command) throws J3270Exception {
		try {
			return new ProcessPiper(command);
		} catch (IOException e) {
			throw new J3270Exception("Failed to start process for '" + command + "'", e);
		}
	}

	private static String checkString(String s, String format, Object...args) {
		checkNotNull(s, format, args);
		checkArgument(STRING.matcher(s).matches(), format, args);
		return escape(s);
	}
}
