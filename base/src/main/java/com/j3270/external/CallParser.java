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
import static com.j3270.external.Actions.actionNamed;
import static com.j3270.external.BasicAction.Execute;
import static com.j3270.external.BasicAction.PrintText;
import static com.j3270.external.BasicAction.Script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Daniel Yokomizo
 */
public class CallParser {
	public static List<Call> parse(String text) {
		checkNotNull(text, "text");
		final List<Call> cs = new ArrayList<>();
		for (final String s : lines(text)) {
			cs.add(parseCall(s));
		}
		return cs;
	}

	private static Call parseCall(String s) {
		final List<String> list = new ArrayList<>();
		parse(s, list);
		checkArgument(!list.isEmpty(), "Invalid call: %s", s);
		final String name = list.get(0);
		final Action action = actionNamed(name);
		return new Call(action.name(), list.subList(1, list.size()));
	}

	private static Iterable<String> lines(final String s) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				final StringTokenizer st = new StringTokenizer(s, "\r\n");
				return new Iterator<String>() {
					@Override
					public boolean hasNext() {
						return st.hasMoreTokens();
					}

					@Override
					public String next() {
						return st.nextToken();
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	private static final Pattern CALL = Pattern.compile("^\\s*([a-zA-Z][a-zA-Z0-9]*)\\s*(?:\\(\\s*(.*?)\\s*\\)\\s*)?$");

	private static void parse(String s, Collection<String> c) {
		final Matcher m = CALL.matcher(s);
		checkArgument(m.matches(), "Invalid call: %s", s);
		final String name = m.group(1);
		c.add(name);
		final String args = m.group(2);
		if (Execute.name().equalsIgnoreCase(name) || Script.name().equalsIgnoreCase(name) || PrintText.name().equalsIgnoreCase(name)) {
			if (args != null) {
				c.add(args);
			}
		} else {
			parseArguments(args, c);
		}
	}

	private static final Pattern ARGUMENT = Pattern.compile("^\\s*(?:([^\",]*?)|(?:\"([^\"]*)\"))\\s*(?:,\\s*(.*?)\\s*)?$");

	private static void parseArguments(String s, Collection<String> c) {
		if (s == null) { return; }
		final Matcher m = ARGUMENT.matcher(s);
		checkArgument(m.matches(), "Invalid call: %s", s);
		final String a = coalesce(m.group(1), m.group(2));
		c.add(a);
		final String as = m.group(3);
		if (as != null) {
			parseArguments(as, c);
		}
	}

	private static <T> T coalesce(T o1, T o2) {
		return (o1 == null) ? o2 : o1;
	}
}
