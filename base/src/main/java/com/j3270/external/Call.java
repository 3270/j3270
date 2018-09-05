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
import static com.j3270.base.Extras.newList;
import static com.j3270.base.Patterns.INT;
import static com.j3270.external.Actions.actionNamed;

import java.io.Serializable;
import java.util.List;

import com.j3270.base.J3270;
import com.j3270.base.J3270Exception;

/**
 * Encapsulates a single action call to s3270.
 * 
 * @author Daniel Yokomizo
 */
public final class Call implements Serializable {
	private static final long serialVersionUID = 396983265448397473L;
	private final String action;
	private final List<String> arguments;

	Call(String action, List<String> arguments) {
		this.action = action;
		this.arguments = arguments;
	}

	public String action() {
		return action;
	}

	public List<String> arguments() {
		return arguments;
	}

	public int size() {
		return arguments.size();
	}

	public String get(int index) {
		return arguments.get(index);
	}

	public int getInt(int index) {
		final String s = get(index);
		checkArgument(INT.matcher(s).matches(), "Invalid %s", this);
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException("Invalid " + this, exc);
		}
	}

	public List<String> invoke(J3270 j3270) throws J3270Exception {
		final Action a = actionNamed(action());
		return a.apply(j3270, this);
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof Call) && equal(this, (Call) obj));
	}

	private static boolean equal(Call o1, Call o2) {
		return o1.action.equals(o2.action) && o1.arguments.equals(o2.arguments);
	}

	@Override
	public int hashCode() {
		return 0xa1c9131d ^ (31 * action.hashCode() + arguments.hashCode());
	}

	@Override
	public String toString() {
		if (arguments.isEmpty()) { return action; }
		final StringBuilder sb = new StringBuilder();
		sb.append(action);
		char separator = '(';
		for (final String s : arguments) {
			sb.append(separator).append(s);
			separator = ',';
		}
		sb.append(')');
		return sb.toString();
	}

	public static Call call(String action, String...arguments) {
		checkNotNull(action, "action");
		checkNotNull(arguments, "arguments");
		return new Call(action, newList(arguments));
	}

	public static Call call(String action, Iterable<String> arguments) {
		checkNotNull(action, "action");
		checkNotNull(arguments, "arguments");
		return new Call(action, newList(arguments));
	}
}
