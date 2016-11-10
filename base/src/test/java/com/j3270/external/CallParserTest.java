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

import static com.j3270.base.Extras.newList;
import static com.j3270.external.Call.call;
import static com.j3270.external.CallParser.parse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Daniel Yokomizo
 */
public class CallParserTest {
	@Test
	public void parse_simple_statement() {
		assertThat(parse("Attn"), equalTo(newList(call("Attn"))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void fails_to_parse_inexistent_action() {
		parse("Foobar");
	}

	@Test
	public void parse_simple_statement_normalizing() {
		assertThat(parse("  attn  "), equalTo(newList(call("Attn"))));
	}

	@Test
	public void parse_statement_with_arguments() {
		assertThat(parse("toggle(foobar)"), equalTo(newList(call("Toggle", "foobar"))));
		assertThat(parse("toggle(foo,bar)"), equalTo(newList(call("Toggle", "foo", "bar"))));
	}

	@Test
	public void parse_statement_with_arguments_normalizing() {
		assertThat(parse("  toggle  (  foobar  )  "), equalTo(newList(call("Toggle", "foobar"))));
		assertThat(parse("  toggle  (  foo  ,  bar  )  "), equalTo(newList(call("Toggle", "foo", "bar"))));
	}

	@Test
	public void parse_raw_statement() {
		assertThat(parse("  execute  (  foobar  )  "), equalTo(newList(call("Execute", "foobar"))));
		assertThat(parse("  execute  (  foo  |  bar  )  "), equalTo(newList(call("Execute", "foo  |  bar"))));
	}

	@Test
	public void parse_multiple_statements() {
		assertThat(parse("Attn\ntoggle(foobar)\n\n  execute  (  foo  |  bar  )  "),
				equalTo(newList(call("Attn"), call("Toggle", "foobar"), call("Execute", "foo  |  bar"))));
	}

	@Test
	public void parse_script() {
		final String s = "connect(\"foobar\")\n" //
				+ "string(\"logon foo\")\n" //
				+ "enter\n" //
				+ "wait(InputField)\n" //
				+ "string(\"bar\")\n" //
				+ "enter\n" //
				+ "pf(3)\n" //
				+ "ascii\n" //
				+ "disconnect\n" //
				;
		assertThat(parse(s), equalTo(newList(call("Connect", "foobar"), call("String", "logon foo"), call("Enter"), call("Wait", "InputField"),
				call("String", "bar"), call("Enter"), call("PF", "3"), call("Ascii"), call("Disconnect"))));
	}
}
