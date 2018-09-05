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

import static com.j3270.base.Extras.enumValueOfIgnoreCase;

/**
 * @see http://x3270.bgp.nu/Unix/s3270-man.html#Toggles
 * @author Daniel Yokomizo
 */
public enum ToggleMode {
	set, clear;
	private Object readResolve() {
		return ToggleMode.valueOf(name());
	}

	public static ToggleMode toggleOption(String name) {
		return enumValueOfIgnoreCase(ToggleMode.class, name);
	}
}
