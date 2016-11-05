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

import static com.j3270.base.Extras.printLines;
import static com.j3270.base.WaitMode.InputField;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Daniel Yokomizo
 */
public class J3270IT {
	@Test
	public void direct() throws J3270Exception, IOException {
		final J3270 j = new J3270();
		try {
			j.connect(hostname);
			j.wait(InputField);
			j.string("logon " + username);
			j.enter();
			j.wait(InputField);
			j.string(password);
			j.enter();
			j.pf(3);
			printLines(j.ascii());
			j.disconnect();
		} finally {
			j.close();
		}
	}

	String hostname = System.getProperty("j3270.hostname");
	String username = System.getProperty("j3270.username");
	String password = System.getProperty("j3270.password");
}
