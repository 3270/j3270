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
package com.j3270.external;

import static com.j3270.base.Extras.checkArgument;
import static com.j3270.base.Extras.checkNotNull;
import static com.j3270.base.Extras.enumValueOfIgnoreCase;
import static com.j3270.base.QueryKeyword.queryKeyword;
import static com.j3270.base.ReadBufferMode.readBufferMode;
import static com.j3270.base.WaitMode.Output;
import static com.j3270.base.WaitMode.waitMode;
import static com.j3270.base.WindowMode.windowMode;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.j3270.base.J3270;
import com.j3270.base.J3270Exception;
import com.j3270.base.QueryKeyword;
import com.j3270.base.ReadBufferMode;
import com.j3270.base.Status;
import com.j3270.base.Timeout;
import com.j3270.base.WaitMode;
import com.j3270.base.WindowMode;

/**
 * @see http://x3270.bgp.nu/Unix/x3270-script.html#Script-Specific-Actions
 * @author Daniel Yokomizo
 */
public enum ScriptSpecificAction implements Action {
	AnsiText {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			return j3270.ansiText();
		}
	},
	Ascii {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			if (call.size() == 0) {
				return j3270.ascii();
			} else if (call.size() == 1) {
				final int length = call.getInt(0);
				return j3270.ascii(length);
			} else if (call.size() == 3) {
				final int row = call.getInt(0);
				final int col = call.getInt(1);
				final int length = call.getInt(2);
				return j3270.ascii(row, col, length);
			} else if (call.size() == 4) {
				final int row = call.getInt(0);
				final int col = call.getInt(1);
				final int rows = call.getInt(2);
				final int cols = call.getInt(3);
				return j3270.ascii(row, col, rows, cols);
			} else {
				throw new IllegalArgumentException("Invalid " + name() + ": " + call);
			}
		}
	},
	AsciiField {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			return list(j3270.asciiField());
		}
	},
	Connect(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			return BasicAction.Connect.apply(j3270, call);
		}
	},
	CloseScript {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			final int status = call.getInt(0);
			j3270.closeScript(status);
			return j3270.getData();
		}
	},
	ContinueScript {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			if (call.size() == 0) {
				j3270.continueScript();
				return j3270.getData();
			} else if (call.size() == 1) {
				j3270.continueScript(call.get(0));
				return j3270.getData();
			} else {
				throw new IllegalArgumentException("Invalid " + name() + ": " + call);
			}
		}
	},
	Disconnect(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			return BasicAction.Disconnect.apply(j3270, call);
		}
	},
	Ebcdic {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			if (call.size() == 0) {
				return j3270.ebcdic();
			} else if (call.size() == 1) {
				final int length = call.getInt(0);
				return j3270.ebcdic(length);
			} else if (call.size() == 3) {
				final int row = call.getInt(0);
				final int col = call.getInt(1);
				final int length = call.getInt(2);
				return j3270.ebcdic(row, col, length);
			} else if (call.size() == 4) {
				final int row = call.getInt(0);
				final int col = call.getInt(1);
				final int rows = call.getInt(2);
				final int cols = call.getInt(3);
				return j3270.ebcdic(row, col, rows, cols);
			} else {
				throw new IllegalArgumentException("Invalid " + name() + ": " + call);
			}
		}
	},
	EbcdicField {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			return list(j3270.ebcdicField());
		}
	},
	Info {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			j3270.info(call.get(0));
			return j3270.getData();
		}
	},
	Expect {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			if (call.size() == 1) {
				j3270.expect(call.get(0));
				return j3270.getData();
			} else if (call.size() == 2) {
				j3270.expect(call.get(0), call.getInt(1));
				return j3270.getData();
			} else {
				throw new IllegalArgumentException("Invalid " + name() + ": " + call);
			}
		}
	},
	MoveCursor {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			return BasicAction.MoveCursor.apply(j3270, call);
		}
	},
	PauseScript {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			if (call.size() == 0) {
				return list(j3270.pauseScript());
			} else if (call.size() == 1) {
				return list(j3270.pauseScript(new Timeout(call.getInt(0), SECONDS)));
			} else {
				throw new IllegalArgumentException("Invalid " + name() + ": " + call);
			}
		}
	},
	PrintText {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			return BasicAction.PrintText.apply(j3270, call);
		}
	},
	Query {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			if (call.size() == 0) {
				return j3270.query();
			} else if (call.size() == 1) {
				final QueryKeyword keyword = queryKeyword(call.get(0));
				return list(j3270.query(keyword));
			} else {
				throw new IllegalArgumentException("Invalid " + name() + ": " + call);
			}
		}
	},
	ReadBuffer {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			final String s = call.get(0);
			final ReadBufferMode mode = readBufferMode(s);
			return j3270.readBuffer(mode);
		}
	},
	Script {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			return BasicAction.Script.apply(j3270, call);
		}
	},
	Snap {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			if (call.size() == 0) {
				j3270.snap().save();
				return j3270.getData();
			}
			final String action = call.get(0);
			if ("Ascii".equalsIgnoreCase(action)) {
				if (call.size() == 1) {
					return j3270.snap().ascii();
				} else if (call.size() == 2) {
					return j3270.snap().ascii(call.getInt(1));
				} else if (call.size() == 4) {
					return j3270.snap().ascii(call.getInt(1), call.getInt(2), call.getInt(3));
				} else if (call.size() == 5) {
					return j3270.snap().ascii(call.getInt(1), call.getInt(2), call.getInt(3), call.getInt(4));
				} else {
					throw new IllegalArgumentException("Invalid " + name() + ": " + call);
				}
			} else if ("Cols".equalsIgnoreCase(action)) {
				checkArgument(call.size() == 1, "Invalid %s: %s", name(), call);
				final int cols = j3270.snap().cols();
				return list(String.valueOf(cols));
			} else if ("Ebcdic".equalsIgnoreCase(action)) {
				if (call.size() == 1) {
					return j3270.snap().ebcdic();
				} else if (call.size() == 2) {
					return j3270.snap().ebcdic(call.getInt(1));
				} else if (call.size() == 4) {
					return j3270.snap().ebcdic(call.getInt(1), call.getInt(2), call.getInt(3));
				} else if (call.size() == 5) {
					return j3270.snap().ebcdic(call.getInt(1), call.getInt(2), call.getInt(3), call.getInt(4));
				} else {
					throw new IllegalArgumentException("Invalid " + name() + ": " + call);
				}
			} else if ("ReadBuffer".equalsIgnoreCase(action)) {
				checkArgument(call.size() == 2, "Invalid %s: %s", name(), call);
				final ReadBufferMode mode = readBufferMode(call.get(1));
				return j3270.snap().readBuffer(mode);
			} else if ("Rows".equalsIgnoreCase(action)) {
				checkArgument(call.size() == 1, "Invalid %s: %s", name(), call);
				final int rows = j3270.snap().rows();
				return list(String.valueOf(rows));
			} else if ("Save".equalsIgnoreCase(action)) {
				checkArgument(call.size() == 1, "Invalid %s: %s", name(), call);
				j3270.snap().save();
				return j3270.getData();
			} else if ("Status".equalsIgnoreCase(action)) {
				checkArgument(call.size() == 1, "Invalid %s: %s", name(), call);
				final Status s = j3270.snap().status();
				return list(s.toString());
			} else if ("Wait".equalsIgnoreCase(action)) {
				checkArgument((call.size() == 2) || (call.size() == 3), "Invalid %s: %s", name(), call);
				if (call.size() == 2) {
					checkArgument(Output.name().equalsIgnoreCase(call.get(1)), "Invalid %s: %s", name(), call);
					j3270.snap().wait(Output);
					return j3270.getData();
				} else {
					checkArgument(Output.name().equalsIgnoreCase(call.get(2)), "Invalid %s: %s", name(), call);
					final int time = call.getInt(1);
					j3270.snap().wait(new Timeout(time, SECONDS), Output);
					return j3270.getData();
				}
			} else {
				throw new IllegalArgumentException("Invalid " + name() + ": " + call);
			}
		}
	},
	Source {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			final File file = new File(call.get(0));
			return j3270.source(file);
		}
	},
	Title {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			j3270.title(call.get(0));
			return j3270.getData();
		}
	},
	Transfer(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			return BasicAction.Transfer.apply(j3270, call);
		}
	},
	Wait {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			if (call.size() == 1) {
				final WaitMode mode = waitMode(call.get(0));
				j3270.wait(mode);
				return j3270.getData();
			} else if (call.size() == 2) {
				final Timeout timeout = new Timeout(call.getInt(0), SECONDS);
				final WaitMode mode = waitMode(call.get(1));
				j3270.wait(timeout, mode);
				return j3270.getData();
			} else {
				throw new IllegalArgumentException("Invalid " + name() + ": " + call);
			}
		}
	},
	WindowState {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			final WindowMode mode = windowMode(call.get(0));
			j3270.windowState(mode);
			return j3270.getData();
		}
	}, //
	;

	private final boolean blocking;

	private ScriptSpecificAction(boolean blocking) {
		this.blocking = blocking;
	}

	private ScriptSpecificAction() {
		this(false);
	}

	protected void checkApply(Call call, int length) {
		checkApply(call);
		checkCallArguments(call, length);
	}

	protected void checkApply(Call call) {
		checkNotNull(call, "call");
		checkArgument(name().equalsIgnoreCase(call.action()), "Invalid %s: %s", name(), call);
	}

	protected void checkCallArguments(Call call, int length) {
		checkArgument(call.size() == length, (length == 1) ? "Invalid %s, expected %d argument: %s" : "Invalid %s, expected %d arguments: %s", name(),
				length, call);
	}

	@Override
	public boolean isBlocking() {
		return blocking;
	}

	private Object readResolve() {
		return ScriptSpecificAction.valueOf(name());
	}

	private static <E> List<E> list(E e) {
		final List<E> l = new ArrayList<>();
		l.add(e);
		return l;
	}

	public static ScriptSpecificAction scriptSpecificAction(String name) {
		return enumValueOfIgnoreCase(ScriptSpecificAction.class, name);
	}
}
