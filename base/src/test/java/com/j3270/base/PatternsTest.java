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

import static com.j3270.base.Patterns.HEXDIGITS;
import static com.j3270.base.Patterns.HOST;
import static com.j3270.base.Patterns.HOSTPORT;
import static com.j3270.base.Patterns.STRING;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

/**
 * @author Daniel Yokomizo
 */
public class PatternsTest {
	@Test
	public void host() {
		assertThat("foo", matches(HOST, "RFC2396 host"));
		assertThat("foo.bar", matches(HOST, "RFC2396 host"));
		assertThat("foo.", matches(HOST, "RFC2396 host"));
		assertThat("foo.bar.", matches(HOST, "RFC2396 host"));
		assertThat("foo-bar.", matches(HOST, "RFC2396 host"));
		assertThat("127.0.0.1", matches(HOST, "RFC2396 host"));
		assertThat("1foo", not(matches(HOST, "RFC2396 host")));
	}

	@Test
	public void hostport() {
		assertThat("foo.bar:8080", matches(HOSTPORT, "RFC2396 hostport"));
	}

	@Test
	public void hexdigits() {
		assertThat("0123456789abcdefABCDEF", matches(HEXDIGITS, "hex"));
	}

	@Test
	public void string() {
		assertThat("foobar", matches(STRING, "string"));
		assertThat("\"", matches(STRING, "string"));
		assertThat("\\", not(matches(STRING, "string")));
		assertThat("\\pa9", matches(STRING, "string"));
		assertThat("\\pf99", matches(STRING, "string"));
		assertThat("\\e99", matches(STRING, "string"));
		assertThat("\\u99", matches(STRING, "string"));
		assertThat("\\x99", matches(STRING, "string"));
		assertThat("\\e9999", matches(STRING, "string"));
		assertThat("\\u9999", matches(STRING, "string"));
		assertThat("\\x9999", matches(STRING, "string"));
	}

	private Matcher<String> matches(final Pattern pattern, final String name) {
		return new TypeSafeDiagnosingMatcher<String>(String.class) {
			@Override
			protected boolean matchesSafely(String item, Description mismatchDescription) {
				final java.util.regex.Matcher m = pattern.matcher(item);
				if (m.matches()) { return true; }
				mismatchDescription.appendValue(item).appendText(" does not match ").appendText(name).appendText(" ").appendValue(pattern.pattern())
						.appendText(" using flags ").appendValue(pattern.flags());
				return false;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("matches ").appendText(name).appendText(" ").appendValue(pattern.pattern()).appendText(" using flags ")
						.appendValue(pattern.flags());
			}
		};
	}
}
