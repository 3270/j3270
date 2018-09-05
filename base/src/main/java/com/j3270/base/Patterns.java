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

import java.util.regex.Pattern;

/**
 * @author Daniel Yokomizo
 */
public final class Patterns {
	private interface Rfc2396 {
		String domainLabel = "[a-zA-Z0-9]|(?:[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])";
		String topLabel = "[a-zA-Z]|(?:[a-zA-Z][a-zA-Z0-9\\-]*[a-zA-Z0-9])";
		String hostname = "(?:(?:" + domainLabel + ")\\.)*(?:" + topLabel + ")(?:\\.?)";
		String ipv4_address = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
		String host = "(?:" + hostname + ")|(?:" + ipv4_address + ")";
		String port = "[0-9]{1,5}";
		String hostport = "(" + host + ")" + "(?:[:](" + port + "))?";
	}

	public static final Pattern HOST = Pattern.compile("^" + Rfc2396.host + "$");
	public static final Pattern HOSTPORT = Pattern.compile("^" + Rfc2396.hostport + "$");
	public static final Pattern HEXDIGITS = Pattern.compile("^[0-9a-fA-F]+$");
	public static final Pattern KEY = Pattern.compile("^(?:(?:[a-zA-Z][a-zA-Z_]*)?[a-zA-Z])|(?:0[0-9]{2,3})$");
	public static final Pattern STRING = Pattern
			.compile("^(?:[ -Z\\[\\]^-~]|(?:\\\\[bfnrtT])|(?:\\\\pa[0-9])|(?:\\\\pf[0-9]{2})|(?:\\\\[eux](?:[0-9a-fA-F]{2}|[0-9a-fA-F]{4})))*$");
	public static final Pattern HOSTFILE = Pattern.compile("^[ !#-~]+$");
	public static final Pattern INT = Pattern.compile("^[-+]?(?:0|(?:[1-9][0-9]*))$");

	private Patterns() {}
}
