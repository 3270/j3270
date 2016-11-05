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
import static com.j3270.external.FileTransferOption.HostFile;
import static com.j3270.external.FileTransferOption.LocalFile;
import static com.j3270.external.FileTransferOption.fileTransferOption;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.j3270.base.FileTransfer;
import com.j3270.base.J3270;
import com.j3270.base.J3270Exception;
import com.j3270.base.ToggleMode;
import com.j3270.base.ToggleOption;

/**
 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Actions
 * @author Daniel Yokomizo
 */
public enum BasicAction implements Action {
	Attn(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.attn();
			return j3270.getData();
		}
	},
	BackSpace {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.backSpace();
			return j3270.getData();
		}
	},
	BackTab {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.backTab();
			return j3270.getData();
		}
	},
	CircumNot {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.circumNot();
			return j3270.getData();
		}
	},
	Clear(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.clear();
			return j3270.getData();
		}
	},
	Connect(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			j3270.connect(call.get(0));
			return j3270.getData();
		}
	},
	CursorSelect(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.cursorSelect();
			return j3270.getData();
		}
	},
	Delete {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.delete();
			return j3270.getData();
		}
	},
	DeleteField {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.deleteField();
			return j3270.getData();
		}
	},
	DeleteWord {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.deleteWord();
			return j3270.getData();
		}
	},
	Disconnect(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.disconnect();
			return j3270.getData();
		}
	},
	Down {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.down();
			return j3270.getData();
		}
	},
	Dup {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.dup();
			return j3270.getData();
		}
	},
	Enter(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.enter();
			return j3270.getData();
		}
	},
	Erase {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.erase();
			return j3270.getData();
		}
	},
	EraseEOF {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.eraseEOF();
			return j3270.getData();
		}
	},
	EraseInput {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.eraseInput();
			return j3270.getData();
		}
	},
	Execute {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			j3270.execute(call.get(0));
			return j3270.getData();
		}
	},
	FieldEnd {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.fieldEnd();
			return j3270.getData();
		}
	},
	FieldMark {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.fieldMark();
			return j3270.getData();
		}
	},
	HexString {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			j3270.hexString(call.get(0));
			;
			return j3270.getData();
		}
	},
	Home {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.home();
			return j3270.getData();
		}
	},
	Insert {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.insert();
			return j3270.getData();
		}
	},
	Interrupt(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.interrupt();
			return j3270.getData();
		}
	},
	Key {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			j3270.key(call.get(0));
			return j3270.getData();
		}
	},
	Left {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.left();
			return j3270.getData();
		}
	},
	Left2 {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.left2();
			return j3270.getData();
		}
	},
	MonoCase {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.monoCase();
			return j3270.getData();
		}
	},
	MoveCursor {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 2);
			final int row = call.getInt(0);
			final int col = call.getInt(1);
			j3270.moveCursor(row, col);
			return j3270.getData();
		}
	},
	NewLine {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.newLine();
			return j3270.getData();
		}
	},
	NextWord {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.nextWord();
			return j3270.getData();
		}
	},
	PA {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			j3270.pa(call.getInt(0));
			return j3270.getData();
		}
	},
	PF {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 1);
			j3270.pf(call.getInt(0));
			return j3270.getData();
		}
	},
	PreviousWord {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.previousWord();
			return j3270.getData();
		}
	},
	PrintText {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			boolean modi = false;
			String caption = null;
			String label = null;
			String value = null;
			for (int i = 0; i < call.size(); i++) {
				final String s = call.get(i);
				if ("modi".equalsIgnoreCase(s)) {
					checkArgument(!modi, "Invalid PrintText: %s", call);
					modi = true;
				} else if ("caption".equalsIgnoreCase(s)) {
					checkArgument(caption == null, "Invalid PrintText: %s", call);
					checkArgument(i + 1 < call.size(), "Invalid PrintText: %s", call);
					caption = call.get(++i);
				} else if ("rtf".equalsIgnoreCase(s) || "file".equalsIgnoreCase(s) || "html".equalsIgnoreCase(s) || "string".equalsIgnoreCase(s)) {
					checkArgument(label == null, "Invalid PrintText: %s", call);
					checkArgument(i + 1 < call.size(), "Invalid PrintText: %s", call);
					label = s.toLowerCase();
					value = call.get(++i);
				} else if ("command".equalsIgnoreCase(s)) {
					checkArgument(label == null, "Invalid PrintText: %s", call);
					checkArgument(i + 1 < call.size(), "Invalid PrintText: %s", call);
					label = s.toLowerCase();
					value = call.get(++i);
				} else if ((i == 0) && (call.size() == 1)) {
					label = "command";
					value = s;
				} else {
					throw new IllegalArgumentException("Invalid PrintText: " + call);
				}
			}
			checkArgument(label != null, "Invalid %s: %s", name(), call);
			com.j3270.base.PrintText pt = j3270.printText();
			if (modi) {
				pt.modi();
			}
			if (caption != null) {
				pt.caption(caption);
			}
			if ("command".equals(label)) {
				pt.command(value);
			} else {
				final File file = new File(value);
				if ("rtf".equals(label)) {
					pt.rtf(file);
				} else if ("html".equals(label)) {
					pt.html(file);
				} else if ("string".equals(label) || "file".equals(label)) {
					pt.file(file);
				} else {
					throw new IllegalArgumentException("Invalid PrintText: " + call);
				}
			}
			return j3270.getData();
		}
	},
	Quit {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.quit();
			return j3270.getData();
		}
	},
	Redraw {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.redraw();
			return j3270.getData();
		}
	},
	Reset {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.reset();
			return j3270.getData();
		}
	},
	Right {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.right();
			return j3270.getData();
		}
	},
	Right2 {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.right2();
			return j3270.getData();
		}
	},
	Script {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			j3270.script(call.get(0), array(tail(call.arguments())));
			return j3270.getData();
		}
	},
	String {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			j3270.string(array(call.arguments()));
			return j3270.getData();
		}
	},
	SysReq(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.sysReq();
			return j3270.getData();
		}
	},
	Tab {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.tab();
			return j3270.getData();
		}
	},
	Toggle {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			final int size = call.size();
			checkArgument((size == 1) || (size == 2), "Invalid %s: %s", name(), call);
			if (size == 1) {
				final ToggleOption option = enumValueOfIgnoreCase(ToggleOption.class, call.get(0));
				j3270.toggle(option);
			} else {
				final ToggleOption option = enumValueOfIgnoreCase(ToggleOption.class, call.get(0));
				final ToggleMode mode = enumValueOfIgnoreCase(ToggleMode.class, call.get(1));
				j3270.toggle(option, mode);
			}
			return j3270.getData();
		}
	},
	ToggleInsert {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.toggleInsert();
			return j3270.getData();
		}
	},
	ToggleReverse {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.toggleReverse();
			return j3270.getData();
		}
	},
	Transfer(true) {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call);
			final Map<FileTransferOption,String> map = arguments(call.arguments());
			final String localPath = checkNotNull(map.get(LocalFile), "LocalFile");
			final String hostFile = checkNotNull(map.get(HostFile), "HostFile");
			final FileTransfer ft = j3270.transfer(new File(localPath), hostFile);
			try {
				set(map, ft);
				ft.end();
			} finally {
				ft.close();
			}
			return j3270.getData();
		}

		private Map<FileTransferOption,String> arguments(List<String> arguments) {
			final Map<FileTransferOption,String> map = new LinkedHashMap<>();
			for (int i = 0; i < arguments.size(); i++) {
				final String argument = arguments.get(i);
				final int assignment = argument.indexOf('=');
				checkArgument(assignment > 0, "Invalid argument[%d]: %s", i, argument);
				final String name = argument.substring(0, assignment).trim();
				final String value = argument.substring(assignment + 1).trim();
				final FileTransferOption option = fileTransferOption(name);
				map.put(option, value);
			}
			return map;
		}

		private void set(Map<FileTransferOption,String> map, FileTransfer ft) {
			for (final Map.Entry<FileTransferOption,String> e : map.entrySet()) {
				final FileTransferOption k = e.getKey();
				final String v = e.getValue();
				try {
					k.set(ft, v);
				} catch (Exception exc) {
					throw new IllegalArgumentException("Invalid " + k + ": " + v, exc);
				}
			}
		}
	},
	Up {
		@Override
		public List<String> apply(J3270 j3270, Call call) throws J3270Exception {
			checkApply(call, 0);
			j3270.up();
			return j3270.getData();
		}
	}, //
	;
	private final boolean blocking;

	private BasicAction(boolean blocking) {
		this.blocking = blocking;
	}

	private BasicAction() {
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
		return BasicAction.valueOf(name());
	}

	public static BasicAction basicAction(String name) {
		return enumValueOfIgnoreCase(BasicAction.class, name);
	}

	private static String[] array(List<String> list) {
		return list.toArray(new String[list.size()]);
	}

	private static <E> List<E> tail(List<E> list) {
		return list.subList(1, list.size());
	}
}
