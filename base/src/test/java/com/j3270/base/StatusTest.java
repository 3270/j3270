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

import static com.j3270.base.Status.Code.Ok;
import static com.j3270.base.Status.CommandExecutionTime.HostResponseTook;
import static com.j3270.base.Status.CommandExecutionTime.NoHostResponse;
import static com.j3270.base.Status.ConnectionState.Connected;
import static com.j3270.base.Status.EmulatorMode._3270;
import static com.j3270.base.Status.FieldProtection.Unprotected;
import static com.j3270.base.Status.KeyboardState.Unlocked;
import static com.j3270.base.Status.ScreenFormatting.Formatted;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Yokomizo
 */
public class StatusTest {
	@Test
	public void keyboardState() {
		assertThat(s.keyboardState(), equalTo(Unlocked));
	}

	@Test
	public void screenFormatting() {
		assertThat(s.screenFormatting(), equalTo(Formatted));
	}

	@Test
	public void fieldProtection() {
		assertThat(s.fieldProtection(), equalTo(Unprotected));
	}

	@Test
	public void connectionState() {
		assertThat(s.connectionState(), equalTo(Connected("foobar")));
	}

	@Test
	public void emulatorMode() {
		assertThat(s.emulatorMode(), equalTo(_3270));
	}

	@Test
	public void modelNumber() {
		assertThat(s.modelNumber(), equalTo(4));
	}

	@Test
	public void numberOfRows() {
		assertThat(s.numberOfRows(), equalTo(24));
	}

	@Test
	public void numberOfColums() {
		assertThat(s.numberOfColumns(), equalTo(80));
	}

	@Test
	public void cursorRow() {
		assertThat(s.cursorRow(), equalTo(23));
	}

	@Test
	public void cursorColumn() {
		assertThat(s.cursorColumn(), equalTo(0));
	}

	@Test
	public void windowID() {
		assertThat(s.windowID(), equalTo(0));
	}

	@Test
	public void commandExecutionTime() {
		assertThat(s.commandExecutionTime(), equalTo(NoHostResponse()));
		assertThat(HostResponseTook(123006, MILLISECONDS).code(), equalTo("123.006"));
	}

	@Test
	public void code() {
		assertThat(s.code(), equalTo(Ok));
	}

	Status s = new Status("U F U C(foobar) I 4 24 80 23 0 0x0 -\nok\n");
}
