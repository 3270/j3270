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
public enum WaitMode {
	_3270 {
		@Override
		public String code() {
			return name().substring(1);
		}
	},
	_3270Mode {
		@Override
		public String code() {
			return name().substring(1);
		}
	},
	Disconnect, InputField, NVTMode, Output, Unlock, Seconds;

	public String code() {
		return name();
	}

	@Override
	public String toString() {
		return code();
	}

	private Object readResolve() {
		return WaitMode.valueOf(name());
	}

	public static WaitMode waitMode(String name) {
		if ("3270".equals(name)) { return _3270; }
		if ("3270Mode".equals(name)) { return _3270Mode; }
		return enumValueOfIgnoreCase(WaitMode.class, name);
	}
}
