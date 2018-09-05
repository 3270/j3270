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
import static com.j3270.base.Safety.UNSAFE;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniel Yokomizo
 */
public class PrintText {
	private final J3270 j3270;
	private boolean modi;
	private String caption;

	PrintText(J3270 j3270) {
		this.j3270 = j3270;
	}

	public PrintText modi() {
		modi = true;
		return this;
	}

	public PrintText caption(String caption) {
		this.caption = escape(checkNotNull(caption, "caption"));
		return this;
	}

	public void html(File file) throws J3270Exception {
		final String path = checkFile(file);
		j3270.printText(toAction("html", path));
	}

	public void rtf(File file) throws J3270Exception {
		final String path = checkFile(file);
		j3270.printText(toAction("rtf", path));
	}

	public void file(File file) throws J3270Exception {
		final String path = checkFile(file);
		j3270.printText(toAction("file", path));
	}

	public void command(String command) throws J3270Exception {
		final String s = checkNotNull(command, "command");
		final String a = toAction("command", s);
		checkState(j3270.getSafety() == UNSAFE, "Unsafe operation requires UNSAFE safety mode");
		j3270.printText(a);
	}

	private String toAction(String label, String value) {
		final StringBuilder sb = new StringBuilder();
		sb.append("PrintText(");
		if (modi) {
			sb.append("modi,");
		}
		if (caption != null) {
			sb.append("caption,\"").append(caption).append("\",");
		}
		sb.append(label).append(",");
		if ("command".equals(label)) {
			sb.append(value);
		} else {
			sb.append('"').append(value).append('"');
		}
		sb.append(')');
		return sb.toString();
	}

	private String checkFile(File file) throws J3270Exception {
		checkNotNull(file, "file");
		final String path = file.getAbsolutePath();
		checkArgument(path.indexOf('"') < 0, "Invalid file: %s", path);
		if (file.exists()) {
			checkArgument(file.canWrite(), "File is not writeable: %s", path);
			checkArgument(file.isFile(), "File path does not denote a file: %s", path);
		} else {
			try {
				checkArgument(file.createNewFile(), "File is not writeable: %s", path);
			} catch (IOException exc) {
				throw new J3270Exception("File is not writeable: " + path, exc);
			}
			checkArgument(file.canWrite(), "File is not writeable: %s", path);
			checkArgument(file.isFile(), "File path does not denote a file: %s", path);
		}
		return path;
	}
}
