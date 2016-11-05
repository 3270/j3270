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

/**
 * @author Daniel Yokomizo
 */
public class J3270Exception extends Exception {
	private static final long serialVersionUID = 288890927876818061L;

	public J3270Exception() {
		super();
	}

	public J3270Exception(String message) {
		super(message);
	}

	public J3270Exception(Throwable cause) {
		super(cause);
	}

	public J3270Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public J3270Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
