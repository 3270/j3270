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
import static com.j3270.base.Patterns.HOST;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Status-Format
 * @author Daniel Yokomizo
 */
public final class Status implements Serializable {
	private static final long serialVersionUID = -968581656104103545L;
	private final String status;
	private final KeyboardState keyboardState;
	private final ScreenFormatting screenFormatting;
	private final FieldProtection fieldProtection;
	private final ConnectionState connectionState;
	private final EmulatorMode emulatorMode;
	private final int modelNumber;
	private final int numberOfRows;
	private final int numberOfColumns;
	private final int cursorRow;
	private final int cursorColumn;
	private final int windowID;
	private final CommandExecutionTime commandExecutionTime;
	private final Code code;

	public Status(String status) {
		checkNotNull(status, "status");
		checkArgument(count(status, " ") == 11, "Invalid status: %s", status);
		try {
			final StringTokenizer st = new StringTokenizer(status);
			this.status = status;
			keyboardState = parse(st, "U", KeyboardState.Unlocked, "L", KeyboardState.Locked, "E", KeyboardState.Error);
			screenFormatting = parse(st, "F", ScreenFormatting.Formatted, "U", ScreenFormatting.Unformatted);
			fieldProtection = parse(st, "P", FieldProtection.Protected, "U", FieldProtection.Unprotected);
			connectionState = parseConnectionState(st);
			emulatorMode = parse(st, "I", EmulatorMode._3270, "L", EmulatorMode.Line, "C", EmulatorMode.Character, "P", EmulatorMode.Unnegotiated, "N",
					EmulatorMode.NotConnected);
			modelNumber = parseInt(st, "ModelNumber", 2, 5);
			numberOfRows = parseInt(st, "NumberOfRows", 1, Integer.MAX_VALUE);
			numberOfColumns = parseInt(st, "NumberOfColumns", 1, Integer.MAX_VALUE);
			cursorRow = parseInt(st, "CursorRow", 0, numberOfRows - 1);
			cursorColumn = parseInt(st, "CursorColumn", 0, numberOfColumns - 1);
			windowID = parseInt(st, "WindowID", 16, 0, Integer.MAX_VALUE);
			commandExecutionTime = parseCommandExecutionTime(st);
			code = parse(st, "ok", Code.Ok, "error", Code.Error);
			checkArgument(!st.hasMoreTokens(), "Invalid status: %s", status);
		} catch (Exception exc) {
			throw new IllegalArgumentException("Invalid status: " + status, exc);
		}
	}

	public KeyboardState keyboardState() {
		return keyboardState;
	}

	public ScreenFormatting screenFormatting() {
		return screenFormatting;
	}

	public FieldProtection fieldProtection() {
		return fieldProtection;
	}

	public ConnectionState connectionState() {
		return connectionState;
	}

	public EmulatorMode emulatorMode() {
		return emulatorMode;
	}

	public int modelNumber() {
		return modelNumber;
	}

	public int numberOfRows() {
		return numberOfRows;
	}

	public int numberOfColumns() {
		return numberOfColumns;
	}

	public int cursorRow() {
		return cursorRow;
	}

	public int cursorColumn() {
		return cursorColumn;
	}

	public int windowID() {
		return windowID;
	}

	public CommandExecutionTime commandExecutionTime() {
		return commandExecutionTime;
	}

	public Code code() {
		return code;
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof Status) && equal(this, (Status) obj));
	}

	private static boolean equal(Status o1, Status o2) {
		return o1.status.equals(o2.status);
	}

	@Override
	public int hashCode() {
		return 0x3bb46be7 ^ status.hashCode();
	}

	@Override
	public String toString() {
		return status;
	}

	public interface StatusField {
		String code();
	}

	public enum KeyboardState implements StatusField {
		Unlocked, Locked, Error;
		public String code() {
			return name().substring(0, 1);
		}

		private Object readResolve() {
			return KeyboardState.valueOf(name());
		}
	}

	public enum ScreenFormatting implements StatusField {
		Formatted, Unformatted;
		public String code() {
			return name().substring(0, 1);
		}

		private Object readResolve() {
			return ScreenFormatting.valueOf(name());
		}
	}

	public enum FieldProtection implements StatusField {
		Protected, Unprotected;
		public String code() {
			return name().substring(0, 1);
		}

		private Object readResolve() {
			return FieldProtection.valueOf(name());
		}
	}

	public static final class ConnectionState implements StatusField, Serializable {
		private static final long serialVersionUID = 1977203552866521721L;
		private static final ConnectionState UNCONNECTED = new ConnectionState(null);
		private final String hostname;

		private ConnectionState(String hostname) {
			this.hostname = hostname;
		}

		public String hostname() {
			return hostname;
		}

		@Override
		public String code() {
			return (hostname == null) ? "N" : "C(" + hostname + ")";
		}

		@Override
		public boolean equals(Object obj) {
			return (this == obj) || ((obj instanceof ConnectionState) && equal(this, (ConnectionState) obj));
		}

		private static boolean equal(ConnectionState o1, ConnectionState o2) {
			return (o1.hostname == null) ? (o2.hostname == null) : o1.hostname.equals(o2.hostname);
		}

		@Override
		public int hashCode() {
			return 0x3c1af93e ^ ((hostname == null) ? 0 : hostname.hashCode());
		}

		@Override
		public String toString() {
			return code();
		}

		public static ConnectionState Connected(String hostname) {
			checkNotNull(hostname, hostname);
			return new ConnectionState(hostname);
		}

		public static ConnectionState Unconnected() {
			return UNCONNECTED;
		}
	}

	public enum EmulatorMode implements StatusField {
		_3270("I"), Line("L"), Character("C"), Unnegotiated("P"), NotConnected("N");
		private final String code;

		private EmulatorMode(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}

		private Object readResolve() {
			return EmulatorMode.valueOf(name());
		}
	}

	public static final class CommandExecutionTime implements StatusField, Serializable {
		private static final long serialVersionUID = -8053517657020612370L;
		private static final String FORMAT = "%d.%03d";
		private static final Pattern PATTERN = Pattern.compile("^(0|(?:[1-9][0-9]*))(?:\\.([0-9]{1,3}))?$");

		private static final CommandExecutionTime NO_HOST_RESPONSE = new CommandExecutionTime(0L, NANOSECONDS);
		private final long time;
		private final TimeUnit unit;

		private CommandExecutionTime(long time, TimeUnit unit) {
			this.time = time;
			this.unit = unit;
		}

		public boolean isNoHostResponse() {
			return (time == 0L) && (unit.ordinal() == NANOSECONDS.ordinal());
		}

		@Override
		public String code() {
			if (isNoHostResponse()) { return "-"; }
			final long s = unit.toSeconds(time);
			final long ms = unit.toMillis(time) - (s * 1000);
			return String.format(FORMAT, s, ms);
		}

		/**
		 * @return this instance's duration
		 */
		public long time() {
			return time;
		}

		/**
		 * @return this instance's {@link TimeUnit} or null
		 */
		public TimeUnit unit() {
			return unit;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			return (this == obj) || ((obj instanceof CommandExecutionTime) && equal(this, (CommandExecutionTime) obj));
		}

		private static boolean equal(CommandExecutionTime o1, CommandExecutionTime o2) {
			return (o1.time == o2.time) && (o1.unit.ordinal() == o2.unit.ordinal());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return 0xd1f85491 ^ (31 * Extras.hashCode(time) + unit.name().hashCode());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			if (isNoHostResponse()) { return "CommandExecutionTime(-)"; }
			final StringBuilder sb = new StringBuilder(40);
			sb.append("CommandExecutionTime(");
			sb.append(time);
			sb.append(',');
			sb.append(unit.name());
			sb.append(')');
			return sb.toString();
		}

		public static CommandExecutionTime NoHostResponse() {
			return NO_HOST_RESPONSE;
		}

		public static CommandExecutionTime HostResponseTook(long time, TimeUnit unit) {
			checkArgument(time > 0, "Time must be non-negative: %d", time);
			checkNotNull(unit, "unit");
			checkArgument((MILLISECONDS.compareTo(unit) <= 0) && (SECONDS.compareTo(unit) >= 0), "Unit must be either SECONDS or MILLISECONDS: %s", unit);
			return new CommandExecutionTime(time, unit);
		}
	}

	public enum Code {
		Ok, Error;

		private Object readResolve() {
			return Code.valueOf(name());
		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	@SuppressWarnings("unchecked")
	private static <E extends Enum<E>> E parse(StringTokenizer st, String label, E value, Object...choices) {
		checkArgument(st.hasMoreTokens(), "Insufficient data to parse %s", value.getDeclaringClass().getSimpleName());
		final String s = st.nextToken();
		if (s.equalsIgnoreCase(label)) { return value; }
		for (int i = 0; i < choices.length; i += 2) {
			if (s.equalsIgnoreCase((String) choices[i])) { return (E) choices[i + 1]; }
		}
		throw new IllegalArgumentException("Invalid value for " + value.getDeclaringClass().getSimpleName() + ": '" + s + "'");
	}

	private static ConnectionState parseConnectionState(StringTokenizer st) {
		checkArgument(st.hasMoreTokens(), "Insufficient data to parse ConnectionState");
		final String code = st.nextToken();
		if (code.equalsIgnoreCase("N")) {
			return ConnectionState.Unconnected();
		} else if ((code.startsWith("C(") || code.startsWith("c(")) && code.endsWith(")")) {
			final String hostname = code.substring(2, code.length() - 1);
			checkArgument(HOST.matcher(hostname).matches(), "Invalid ConnectionState: %s", code);
			return ConnectionState.Connected(hostname);
		} else {
			throw new IllegalArgumentException("Invalid ConnectionState: " + code);
		}
	}

	private static int parseInt(StringTokenizer st, String field, int min, int max) {
		return parseInt(st, field, 10, min, max);
	}

	private static int parseInt(StringTokenizer st, String field, int radix, int min, int max) {
		checkArgument(st.hasMoreTokens(), "Insufficient data to parse %s", field);
		final String code = st.nextToken();
		try {
			checkArgument((radix != 16) || code.startsWith("0x"), "Invalid %s: %s", field, code);
			final int n = Integer.parseInt((radix == 16) ? code.substring(2) : code, radix);
			checkArgument((n >= min) && (n <= max), "Invalid %s: %s", field, code);
			return n;
		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException("Invalid " + field + ": " + code, exc);
		}
	}

	private static CommandExecutionTime parseCommandExecutionTime(StringTokenizer st) {
		checkArgument(st.hasMoreTokens(), "Insufficient data to parse CommandExecutionTime");
		final String code = st.nextToken();
		if (code.equals("-")) {
			return CommandExecutionTime.NO_HOST_RESPONSE;
		} else {
			final Matcher m = CommandExecutionTime.PATTERN.matcher(code);
			checkArgument(m.matches(), "Invalid CommandExecutionTime: %s", code);
			try {
				final String g1 = m.group(1);
				final String g2 = m.group(2);
				final long s = Long.parseLong(g1);
				if (g2 == null) {
					return new CommandExecutionTime(s, TimeUnit.SECONDS);
				} else {
					final long ms = Long.parseLong(g2);
					checkArgument(s < Long.MAX_VALUE / 1000, "Invalid CommandExecutionTime: %s", code);
					return new CommandExecutionTime(s * 1000 + (ms >= 100 ? ms : ms >= 10 ? ms * 10 : ms * 100), TimeUnit.MILLISECONDS);
				}
			} catch (NumberFormatException exc) {
				throw new IllegalArgumentException("Invalid CommandExecutionTime: " + code, exc);
			}
		}
	}

	private static int count(String s, String t) {
		int count = 0;
		for (int i = -1; (i = s.indexOf(t, i + 1)) >= 0; i++) {
			count++;
		}
		return count;
	}
}
