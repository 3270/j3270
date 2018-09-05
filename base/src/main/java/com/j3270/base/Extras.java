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

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Yokomizo
 */
public class Extras {
	public static <T> T checkNotNull(T obj, Object message) {
		if (obj == null) { throw new NullPointerException(message(message)); }
		return obj;
	}

	public static <T> T checkNotNull(T obj, String format, Object...args) {
		if (obj == null) { throw new NullPointerException(message(format, args)); }
		return obj;
	}

	public static void checkArgument(boolean condition, Object message) {
		if (!condition) { throw new IllegalArgumentException(message(message)); }
	}

	public static void checkArgument(boolean condition, String format, Object...args) {
		if (!condition) { throw new IllegalArgumentException(message(format, args)); }
	}

	public static void checkState(boolean condition, Object message) {
		if (!condition) { throw new IllegalStateException(message(message)); }
	}

	public static void checkState(boolean condition, String format, Object...args) {
		if (!condition) { throw new IllegalStateException(message(format, args)); }
	}

	public static <X extends Throwable> void check(boolean condition, Class<X> exception, Object message) throws X {
		if (!condition) { throw newInstance(exception, message(message)); }
	}

	public static <X extends Throwable> void check(boolean condition, Class<X> exception, String format, Object...args) throws X {
		if (!condition) { throw newInstance(exception, message(format, args)); }
	}

	private static <T> T newInstance(Class<T> type, String s) {
		try {
			return type.getConstructor(String.class).newInstance(s);
		} catch (Exception e) {
			throw new RuntimeException("Failed: new " + type.getName() + "(\"" + s + "\")", e);
		}
	}

	public static String message(Object message) {
		return String.valueOf(message);
	}

	public static String message(String format, Object...args) {
		for (int i = 0; i < args.length; i++) {
			if ((args[i] != null) && (args[i] instanceof Object[])) {
				args[i] = Arrays.asList((Object[]) args[i]);
			}
		}
		return String.format(format, args);
	}

	public static <E extends Enum<E>> E enumValueOfIgnoreCase(Class<E> enumType, String name) {
		checkNotNull(enumType, "enumType");
		checkNotNull(name, "name");
		try {
			return Enum.valueOf(enumType, name);
		} catch (IllegalArgumentException exc) {
			E found = null;
			for (final E e : enumType.getEnumConstants()) {
				if (e.name().equalsIgnoreCase(name)) {
					checkArgument(found == null, "Ambiguous name '%s' for %s", name, enumType.getName());
					found = e;
				}
			}
			if (found != null) { return found; }
			throw exc;
		}
	}

	public static <E> List<E> newList(Iterable<E> iterable) {
		checkNotNull(iterable, "iterable");
		final List<E> list = new ArrayList<>();
		final Iterator<E> i = iterable.iterator();
		for (int j = 0; i.hasNext(); j++) {
			list.add(checkNotNull(i.next(), "iterable[%d]", j));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <E> List<E> newList(E...array) {
		checkNotNull(array, "array");
		return newList(asList(array));
	}

	public static String escape(String s) {
		return (s == null) ? null : s.replace("\"", "\\u22");
	}

	public static String escape(String s, Object message) {
		checkNotNull(s, message);
		return s.replace("\"", "\\u22");
	}

	public static int hashCode(long l) {
		return (int) (l ^ (l >>> 32));
	}

	public static <T> void printLines(Iterable<T> i) {
		for (final T t : i) {
			System.out.println(t);
		}
	}

	private Extras() {}
}