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

import static com.j3270.base.Extras.printLines;
import static com.j3270.external.Call.call;
import static com.j3270.external.CallParser.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.j3270.base.J3270;
import com.j3270.base.J3270Exception;

/**
 * @author Daniel Yokomizo
 */
public class CallIT {
	@Test
	public void indirect_via_calls() throws J3270Exception, IOException {
		final List<Call> cs = new ArrayList<>();
		cs.add(call("Connect", hostname));
		cs.add(call("Wait", "InputField"));
		cs.add(call("String", "logon " + username));
		cs.add(call("Enter"));
		cs.add(call("Wait", "InputField"));
		cs.add(call("String", password));
		cs.add(call("Enter"));
		cs.add(call("PF", "3"));
		cs.add(call("Ascii"));
		cs.add(call("Disconnect"));
		printLines(cs);;

		final J3270 j = new J3270();
		try {
			for (final Call c : cs) {
				printLines(c.invoke(j));
			}
		} finally {
			j.close();
		}
	}

	@Test
	public void indirect_via_parsed_calls() throws J3270Exception, IOException {
		final String s = "connect(\"" + hostname + "\")\n" //
				+ "string(\"logon " + username + "\")\n" //
				+ "enter\n" //
				+ "wait(InputField)\n" //
				+ "string(\"" + password + "\")\n" //
				+ "enter\n" //
				+ "pf(3)\n" //
				+ "ascii\n" //
				+ "disconnect\n" //
				;

		final List<Call> cs = parse(s);
		printLines(cs);
		final J3270 j = new J3270();
		try {
			for (final Call c : cs) {
				printLines(c.invoke(j));
			}
		} finally {
			j.close();
		}
	}

	String hostname = System.getProperty("j3270.hostname");
	String username = System.getProperty("j3270.username");
	String password = System.getProperty("j3270.password");
}
