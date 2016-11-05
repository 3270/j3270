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

import static com.j3270.base.Extras.checkNotNull;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniel Yokomizo
 */
public final class Actions {
	public static Action actionNamed(String name) {
		checkNotNull(name, "name");
		for (final Action a : ACTIONS) {
			if (a.name().equalsIgnoreCase(name)) { return a; }
		}
		throw new IllegalArgumentException("No action named: " + name);
	}

	public static Set<Action> actions() {
		return ACTIONS;
	}

	private static final Set<Action> ACTIONS;

	static {
		final Set<Action> s = new LinkedHashSet<>();
		s.addAll(asList(BasicAction.values()));
		s.addAll(asList(ScriptSpecificAction.values()));
		ACTIONS = unmodifiableSet(s);
	}

	private Actions() {}
}
