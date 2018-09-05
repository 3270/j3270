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

import static com.j3270.base.QueryKeyword.BindPluName;
import static com.j3270.base.ReadBufferMode.Ascii;
import static com.j3270.base.Safety.SAFE;
import static com.j3270.base.Safety.UNSAFE;
import static com.j3270.base.ToggleMode.clear;
import static com.j3270.base.ToggleOption.lineWrap;
import static com.j3270.base.WaitMode._3270;
import static com.j3270.base.WindowMode.Iconic;
import static com.j3270.external.Call.call;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

/**
 * @author Daniel Yokomizo
 */
public class J3270Test {
	@Test
	public void attn() throws J3270Exception {
		expect(blocking, "Attn");
		j.attn();
	}

	@Test
	public void backSpace() throws J3270Exception {
		expect(nonBlocking, "BackSpace");
		j.backSpace();
	}

	@Test
	public void backTab() throws J3270Exception {
		expect(nonBlocking, "BackTab");
		j.backTab();
	}

	@Test
	public void circumNot() throws J3270Exception {
		expect(nonBlocking, "CircumNot");
		j.circumNot();
	}

	@Test
	public void clear() throws J3270Exception {
		expect(nonBlocking, "Clear");
		j.clear();
	}

	@Test
	public void connect$String() throws J3270Exception {
		expect(blocking, "Connect(foobar)");
		j.connect("foobar");
	}

	@Test
	public void connect$String$int() throws J3270Exception {
		expect(blocking, "Connect(foobar:8080)");
		j.connect("foobar", 8080);
	}

	@Test
	public void cursorSelect() throws J3270Exception {
		expect(blocking, "CursorSelect");
		j.cursorSelect();
	}

	@Test
	public void delete() throws J3270Exception {
		expect(nonBlocking, "Delete");
		j.delete();
	}

	@Test
	public void deleteField() throws J3270Exception {
		expect(nonBlocking, "DeleteField");
		j.deleteField();
	}

	@Test
	public void deleteWord() throws J3270Exception {
		expect(nonBlocking, "DeleteWord");
		j.deleteWord();
	}

	@Test
	public void disconnect() throws J3270Exception {
		expect(blocking, "Disconnect");
		j.disconnect();
	}

	@Test
	public void down() throws J3270Exception {
		expect(nonBlocking, "Down");
		j.down();
	}

	@Test
	public void dup() throws J3270Exception {
		expect(nonBlocking, "Dup");
		j.dup();
	}

	@Test
	public void enter() throws J3270Exception {
		expect(blocking, "Enter");
		j.enter();
	}

	@Test
	public void erase() throws J3270Exception {
		expect(nonBlocking, "Erase");
		j.erase();
	}

	@Test
	public void eraseEOF() throws J3270Exception {
		expect(nonBlocking, "EraseEOF");
		j.eraseEOF();
	}

	@Test
	public void eraseInput() throws J3270Exception {
		expect(nonBlocking, "EraseInput");
		j.eraseInput();
	}

	@Test
	public void execute() throws J3270Exception {
		try {
			j.execute("foo bar baz");
			fail("Should not be able to perform unsafe action");
		} catch (IllegalStateException expected) {
		}
		expect(nonBlocking, "Execute(foo bar baz)", "whatever", "something");
		final String s = j.withSafety(UNSAFE).execute("foo bar baz");
		assertThat(s, equalTo("data: whatever\ndata: something\n" + response));
	}

	@Test
	public void fieldEnd() throws J3270Exception {
		expect(nonBlocking, "FieldEnd");
		j.fieldEnd();
	}

	@Test
	public void fieldMark() throws J3270Exception {
		expect(nonBlocking, "FieldMark");
		j.fieldMark();
	}

	@Test
	public void hexString() throws J3270Exception {
		expect(nonBlocking, "HexString(cafebabe)");
		j.hexString("cafebabe");
	}

	@Test
	public void home() throws J3270Exception {
		expect(nonBlocking, "Home");
		j.home();
	}

	@Test
	public void insert() throws J3270Exception {
		expect(nonBlocking, "Insert");
		j.insert();
	}

	@Test
	public void interrupt() throws J3270Exception {
		expect(blocking, "Interrupt");
		j.interrupt();
	}

	@Test
	public void key$String() throws J3270Exception {
		expect(nonBlocking, "Key(Alt_L)");
		j.key("Alt_L");

		expect(nonBlocking, "Key(0123)");
		j.key("0123");
	}

	@Test
	public void key$int() throws J3270Exception {
		expect(nonBlocking, "Key(0012)");
		j.key(12);
	}

	@Test
	public void left() throws J3270Exception {
		expect(nonBlocking, "Left");
		j.left();
	}

	@Test
	public void left2() throws J3270Exception {
		expect(nonBlocking, "Left2");
		j.left2();
	}

	@Test
	public void monoCase() throws J3270Exception {
		expect(nonBlocking, "MonoCase");
		j.monoCase();
	}

	@Test
	public void moveCursor() throws J3270Exception {
		expect(nonBlocking, "MoveCursor(0,9)");
		j.moveCursor(0, 9);
	}

	@Test
	public void newLine() throws J3270Exception {
		expect(nonBlocking, "NewLine");
		j.newLine();
	}

	@Test
	public void nextWord() throws J3270Exception {
		expect(nonBlocking, "NextWord");
		j.nextWord();
	}

	@Test
	public void pa() throws J3270Exception {
		expect(blocking, "PA(2)");
		j.pa(2);
	}

	@Test
	public void pf() throws J3270Exception {
		expect(blocking, "PF(3)");
		j.pf(3);
	}

	@Test
	public void previousWord() throws J3270Exception {
		expect(nonBlocking, "PreviousWord");
		j.previousWord();
	}

	@Test
	public void printText() throws J3270Exception {
		final PrintText pt = j.printText();
		pt.modi();
		try {
			pt.command("foo bar");
			fail("Should not be able to perform unsafe action");
		} catch (IllegalStateException expected) {
		}
		expect(nonBlocking, "PrintText(modi,command,foo bar)");
		j.setSafety(UNSAFE);
		pt.command("foo bar");
	}

	@Test
	public void quit() throws J3270Exception {
		expect(nonBlocking, "Quit");
		j.quit();
	}

	@Test
	public void redraw() throws J3270Exception {
		expect(nonBlocking, "Redraw");
		j.redraw();
	}

	@Test
	public void reset() throws J3270Exception {
		expect(nonBlocking, "Reset");
		j.reset();
	}

	@Test
	public void right() throws J3270Exception {
		expect(nonBlocking, "Right");
		j.right();
	}

	@Test
	public void right2() throws J3270Exception {
		expect(nonBlocking, "Right2");
		j.right2();
	}

	@Test
	public void script() throws J3270Exception {
		try {
			j.script("foo", "bar baz", "qux");
			fail("Should not be able to perform unsafe action");
		} catch (IllegalStateException expected) {
		}
		expect(nonBlocking, "Script(foo,bar baz,qux)", "whatever", "something");
		final String s = j.withSafety(UNSAFE).script("foo", "bar baz", "qux");
		assertThat(s, equalTo("data: whatever\ndata: something\n" + response));
	}

	@Test
	public void string() throws J3270Exception {
		expect(blocking, "String(\"foo\\u22bar\")");
		j.string("foo\"bar");
	}

	@Test
	public void sysReq() throws J3270Exception {
		expect(blocking, "SysReq");
		j.sysReq();
	}

	@Test
	public void tab() throws J3270Exception {
		expect(nonBlocking, "Tab");
		j.tab();
	}

	@Test
	public void toggle$ToggleOption() throws J3270Exception {
		expect(nonBlocking, "Toggle(lineWrap)");
		j.toggle(lineWrap);
	}

	@Test
	public void toggle$ToggleOption$ToggleMode() throws J3270Exception {
		expect(nonBlocking, "Toggle(lineWrap,clear)");
		j.toggle(lineWrap, clear);
	}

	@Test
	public void toggleInsert() throws J3270Exception {
		expect(nonBlocking, "ToggleInsert");
		j.toggleInsert();
	}

	@Test
	public void toggleReverse() throws J3270Exception {
		expect(nonBlocking, "ToggleReverse");
		j.toggleReverse();
	}

	@Test
	public void transfer$File$String() throws IOException, J3270Exception {
		final File f = File.createTempFile("foo", "bar");
		f.deleteOnExit();
		final FileTransfer ft = j.transfer(f, "foo bar");
		expect(blocking, "Transfer(\"HostFile=foo bar\",LocalFile=" + f.getAbsolutePath() + ")");
		f.delete();
		ft.end();
	}

	@Test
	public void up() throws J3270Exception {
		expect(nonBlocking, "Up");
		j.up();
	}

	@Test
	public void ansiText() throws J3270Exception {
		expect(nonBlocking, "AnsiText", "whatever", "something");
		final List<String> l = j.ansiText();
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void ascii() throws J3270Exception {
		expect(nonBlocking, "Ascii", "whatever", "something");
		final List<String> l = j.ascii();
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void ascii$int() throws J3270Exception {
		expect(nonBlocking, "Ascii(42)", "whatever", "something");
		final List<String> l = j.ascii(42);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void ascii$int$int$int() throws J3270Exception {
		expect(nonBlocking, "Ascii(0,9,42)", "whatever", "something");
		final List<String> l = j.ascii(0, 9, 42);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void ascii$int$int$int$int() throws J3270Exception {
		expect(nonBlocking, "Ascii(0,9,10,15)", "whatever", "something");
		final List<String> l = j.ascii(0, 9, 10, 15);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void asciiField() throws J3270Exception {
		expect(nonBlocking, "AsciiField", "whatever", "something");
		final String s = j.asciiField();
		assertThat(s, equalTo("whatever"));
	}

	@Test
	public void closeScript() throws J3270Exception {
		expect(nonBlocking, "CloseScript(42)");
		j.closeScript(42);
	}

	@Test
	public void continueScript() throws J3270Exception {
		expect(nonBlocking, "ContinueScript");
		j.continueScript();
	}

	@Test
	public void continueScript$String() throws J3270Exception {
		expect(nonBlocking, "ContinueScript(\"foo\\u22bar\")");
		j.continueScript("foo\"bar");
	}

	@Test
	public void ebcdic() throws J3270Exception {
		expect(nonBlocking, "Ebcdic", "whatever", "something");
		final List<String> l = j.ebcdic();
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void ebcdic$int() throws J3270Exception {
		expect(nonBlocking, "Ebcdic(42)", "whatever", "something");
		final List<String> l = j.ebcdic(42);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void ebcdic$int$int$int() throws J3270Exception {
		expect(nonBlocking, "Ebcdic(0,9,42)", "whatever", "something");
		final List<String> l = j.ebcdic(0, 9, 42);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void ebcdic$int$int$int$int() throws J3270Exception {
		expect(nonBlocking, "Ebcdic(0,9,10,15)", "whatever", "something");
		final List<String> l = j.ebcdic(0, 9, 10, 15);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void ebcdicField() throws J3270Exception {
		expect(nonBlocking, "EbcdicField", "whatever", "something");
		final String s = j.ebcdicField();
		assertThat(s, equalTo("whatever"));
	}

	@Test
	public void info() throws J3270Exception {
		expect(nonBlocking, "Info(\"foo\\u22bar\")");
		j.info("foo\"bar");
	}

	@Test
	public void expect$String() throws J3270Exception {
		expect(nonBlocking, "Expect(\"foo\\u22bar\")");
		j.expect("foo\"bar");
	}

	@Test
	public void expect$String$int() throws J3270Exception {
		expect(nonBlocking, "Expect(\"foo\\u22bar\",42)");
		j.expect("foo\"bar", 42);
	}

	@Test
	public void pauseScript() throws J3270Exception {
		expect(blocking, "PauseScript", "whatever", "something");
		final String s = j.pauseScript();
		assertThat(s, equalTo("whatever"));
	}

	@Test
	public void pauseScript$Timeout() throws J3270Exception {
		final Timeout timeout = new Timeout(60, SECONDS);
		expect(timeout, "PauseScript", "whatever", "something");
		final String s = j.pauseScript(timeout);
		assertThat(s, equalTo("whatever"));
	}

	@Test
	public void query() throws J3270Exception {
		expect(nonBlocking, "Query", "whatever", "something");
		final List<String> l = j.query();
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void query$QueryKeyword() throws J3270Exception {
		expect(nonBlocking, "Query(BindPluName)", "whatever", "something");
		final String s = j.query(BindPluName);
		assertThat(s, equalTo("whatever"));
	}

	@Test
	public void readBuffer() throws J3270Exception {
		expect(nonBlocking, "ReadBuffer(Ascii)", "whatever", "something");
		final List<String> l = j.readBuffer(Ascii);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_ascii() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Ascii)", "whatever", "something");
		final List<String> l = s.ascii();
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_ascii$int() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Ascii,42)", "whatever", "something");
		final List<String> l = s.ascii(42);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_ascii$int$int$int() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Ascii,0,9,42)", "whatever", "something");
		final List<String> l = s.ascii(0, 9, 42);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_ascii$int$int$int$int() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Ascii,0,9,10,15)", "whatever", "something");
		final List<String> l = s.ascii(0, 9, 10, 15);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_cols() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Cols)", "42", "something");
		final int n = s.cols();
		assertThat(n, equalTo(42));
	}

	@Test
	public void snap_ebcdic() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Ebcdic)", "whatever", "something");
		final List<String> l = s.ebcdic();
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_ebcdic$int() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Ebcdic,42)", "whatever", "something");
		final List<String> l = s.ebcdic(42);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_ebcdic$int$int$int() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Ebcdic,0,9,42)", "whatever", "something");
		final List<String> l = s.ebcdic(0, 9, 42);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_ebcdic$int$int$int$int() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Ebcdic,0,9,10,15)", "whatever", "something");
		final List<String> l = s.ebcdic(0, 9, 10, 15);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_readBuffer() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(ReadBuffer,Ascii)", "whatever", "something");
		final List<String> l = s.readBuffer(Ascii);
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void snap_rows() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Rows)", "42", "something");
		final int n = s.rows();
		assertThat(n, equalTo(42));
	}

	@Test
	public void snap_save() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Save)");
		s.save();
	}

	@Test
	public void snap_status() throws J3270Exception {
		final Snap s = j.snap();
		expect(nonBlocking, "Snap(Status)", status);
		final Status st = s.status();
		assertThat(st, equalTo(new Status(response)));
	}

	@Test
	public void source() throws IOException, J3270Exception {
		final File f = File.createTempFile("foo", "bar");
		f.deleteOnExit();
		try {
			j.source(f);
			fail("Should not be able to perform unsafe action");
		} catch (IllegalStateException expected) {
		}
		expect(nonBlocking, "Source(\"" + f.getAbsolutePath() + "\")", "whatever", "something");
		final List<String> l = j.withSafety(UNSAFE).source(f);
		f.delete();
		assertThat(l, equalTo(asList("whatever", "something")));
	}

	@Test
	public void title() throws J3270Exception {
		expect(nonBlocking, "Title(\"foo\\u22bar\")");
		j.title("foo\"bar");
	}

	@Test
	public void wait$WaitMode() throws J3270Exception {
		expect(blocking, "Wait(3270)");
		j.wait(_3270);
	}

	@Test
	public void wait$Timeout$WaitMode() throws J3270Exception {
		final Timeout timeout = new Timeout(6300, MILLISECONDS);
		expect(timeout, "Wait(6,3270)");
		j.wait(timeout, _3270);
	}

	@Test
	public void windowState() throws J3270Exception {
		expect(nonBlocking, "WindowState(Iconic)");
		j.windowState(Iconic);
	}

	@Test
	public void raw() throws J3270Exception {
		try {
			j.raw("foo bar baz");
			fail("Should not be able to perform unsafe action");
		} catch (IllegalStateException expected) {
		}
		expect(blocking, "foo bar baz", "whatever", "something");
		final String s = j.withSafety(UNSAFE).raw("foo bar baz");
		assertThat(s, equalTo("data: whatever\ndata: something\n" + response));
	}

	@Test
	public void call_invoke() throws Exception {
		expect(blocking, "Connect(foobar)");
		call("connect", "foobar").invoke(j);
	}

	@Test
	public void getBlocking() {
		assertThat(j.getBlocking(), equalTo(new Timeout(30, SECONDS)));
	}

	@Test
	public void setBlocking() {
		final Timeout timeout = new Timeout(60, SECONDS);
		j.setBlocking(timeout);
		assertThat(j.getBlocking(), equalTo(timeout));
	}

	@Test
	public void getNonBlocking() {
		assertThat(j.getNonBlocking(), equalTo(new Timeout(3, SECONDS)));
	}

	@Test
	public void setNonBlocking() {
		final Timeout timeout = new Timeout(6, SECONDS);
		j.setNonBlocking(timeout);
		assertThat(j.getNonBlocking(), equalTo(timeout));
	}

	@Test
	public void getSafety() {
		assertThat(j.getSafety(), equalTo(SAFE));
	}

	@Test
	public void testSetSafety() {
		j.setSafety(UNSAFE);
		assertThat(j.getSafety(), equalTo(UNSAFE));
	}

	@Test
	public void withBlocking() {
		j.setBlocking(new Timeout(40, SECONDS));
		j.setNonBlocking(new Timeout(4, SECONDS));
		j.setSafety(UNSAFE);

		final J3270 j2 = j.withBlocking(new Timeout(60, SECONDS));

		assertThat(j2, not(sameInstance(j)));

		assertThat(j2.getBlocking(), equalTo(new Timeout(60, SECONDS)));
		assertThat(j2.getNonBlocking(), equalTo(j.getNonBlocking()));
		assertThat(j2.getSafety(), equalTo(j.getSafety()));

		assertThat(j.getBlocking(), equalTo(new Timeout(40, SECONDS)));
		assertThat(j.getNonBlocking(), equalTo(new Timeout(4, SECONDS)));
		assertThat(j.getSafety(), equalTo(UNSAFE));
	}

	@Test
	public void withNonBlocking() {
		j.setBlocking(new Timeout(40, SECONDS));
		j.setNonBlocking(new Timeout(4, SECONDS));
		j.setSafety(UNSAFE);

		final J3270 j2 = j.withNonBlocking(new Timeout(6, SECONDS));

		assertThat(j2, not(sameInstance(j)));

		assertThat(j2.getBlocking(), equalTo(j.getBlocking()));
		assertThat(j2.getNonBlocking(), equalTo(new Timeout(6, SECONDS)));
		assertThat(j2.getSafety(), equalTo(j.getSafety()));

		assertThat(j.getBlocking(), equalTo(new Timeout(40, SECONDS)));
		assertThat(j.getNonBlocking(), equalTo(new Timeout(4, SECONDS)));
		assertThat(j.getSafety(), equalTo(UNSAFE));
	}

	@Test
	public void withTimeout() {
		j.setBlocking(new Timeout(40, SECONDS));
		j.setNonBlocking(new Timeout(4, SECONDS));
		j.setSafety(UNSAFE);

		final J3270 j2 = j.withTimeout(new Timeout(10, SECONDS));

		assertThat(j2, not(sameInstance(j)));

		assertThat(j2.getBlocking(), equalTo(new Timeout(10, SECONDS)));
		assertThat(j2.getNonBlocking(), equalTo(new Timeout(10, SECONDS)));
		assertThat(j2.getSafety(), equalTo(j.getSafety()));

		assertThat(j.getBlocking(), equalTo(new Timeout(40, SECONDS)));
		assertThat(j.getNonBlocking(), equalTo(new Timeout(4, SECONDS)));
		assertThat(j.getSafety(), equalTo(UNSAFE));
	}

	@Test
	public void withSafety() {
		j.setBlocking(new Timeout(40, SECONDS));
		j.setNonBlocking(new Timeout(4, SECONDS));
		j.setSafety(UNSAFE);

		final J3270 j2 = j.withSafety(SAFE);

		assertThat(j2, not(sameInstance(j)));

		assertThat(j2.getBlocking(), equalTo(j.getBlocking()));
		assertThat(j2.getNonBlocking(), equalTo(j.getNonBlocking()));
		assertThat(j2.getSafety(), equalTo(SAFE));

		assertThat(j.getBlocking(), equalTo(new Timeout(40, SECONDS)));
		assertThat(j.getNonBlocking(), equalTo(new Timeout(4, SECONDS)));
		assertThat(j.getSafety(), equalTo(UNSAFE));
	}

	Mockery context = new Mockery();
	Piper piper = context.mock(Piper.class);
	J3270 j = new J3270(piper);
	Timeout blocking = j.getBlocking();
	Timeout nonBlocking = j.getNonBlocking();
	String status = "U F U C(mainframe.sec4you.com.br) I 4 24 80 23 0 0x0 -";
	String response = status + "\nok\n";

	void expect(final Timeout timeout, final String action, final String...data) {
		context.checking(new Expectations() {{
			try {
				allowing(piper).isRunning(); will(returnValue(true));
				oneOf(piper).pipe(action + '\n', timeout); will(returnValue(response(data)));
			} catch (Exception impossible) {
				throw new RuntimeException(impossible);
			}
		}});
	}

	String response(String...data) {
		if (data.length == 0) {
			return response;
		} else {
			final StringBuilder sb = new StringBuilder();
			for (final String datum : data) {
				sb.append("data: ");
				sb.append(datum);
				sb.append('\n');
			}
			sb.append(response);
			return sb.toString();
		}
	}
}
