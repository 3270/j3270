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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Yokomizo
 */
public class ProcessPiperTest {
	@Test
	public void sh() throws Exception {
		final ProcessPiper p = new ProcessPiper("sh");
		try {
			assertThat(p.isRunning(), is(true));
			System.out.println(p.pipe("pwd\n"));
			System.out.println(p.pipe("ls -lh\n"));
			System.out.println(p.pipe("exit\n"));
			assertThat(p.isRunning(), is(false));
		} finally {
			p.close();
		}
	}
}
