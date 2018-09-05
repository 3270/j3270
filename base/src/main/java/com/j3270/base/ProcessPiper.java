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

import static com.j3270.base.Extras.check;
import static com.j3270.base.Extras.checkArgument;
import static com.j3270.base.Extras.checkNotNull;
import static com.j3270.base.Extras.checkState;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A {@link Piper} that transfer messages to an underlying {@link Process}
 * 
 * @author Daniel Yokomizo
 */
public final class ProcessPiper implements Closeable, Piper {
	private static final Timeout DEFAULT_TIMEOUT = new Timeout(100, MILLISECONDS);
	private final Process process;
	private final ProcessIsRunning running;

	/**
	 * Creates a new {@link ProcessPiper} for the {@link Process} that will be created after
	 * {@link Runtime#exec(String)} is called.
	 * 
	 * @param command
	 *          argument given to {@link Runtime#exec(String)} returned by the result of {@link Runtime#getRuntime()}
	 * @throws IOException
	 *           if {@link Runtime#exec(String)} fails
	 */
	public ProcessPiper(String command) throws IOException {
		this(Runtime.getRuntime().exec(checkNotNull(command, "command")));
	}

	/**
	 * Creates a new {@link ProcessPiper} for the {@link Process} that will be created after
	 * {@link ProcessBuilder#start()} is called.
	 * 
	 * @param builder
	 *          {@link ProcessBuilder} used to {@link ProcessBuilder#start()} the {@link Process}
	 * @throws IOException
	 *           if {@link ProcessBuilder#start()} fails
	 */
	public ProcessPiper(ProcessBuilder builder) throws IOException {
		this(checkNotNull(builder, "builder").start());
	}

	/**
	 * Creates a new {@link ProcessPiper} for a given {@link Process}
	 * 
	 * @param process
	 *          {@link Process} to be used by this instance
	 */
	public ProcessPiper(Process process) {
		checkNotNull(process, "process");
		this.process = process;
		this.running = new ProcessIsRunning(process) ;
	}

	@Override
	public void close() throws IOException {
		process.destroy();
	}

	@Override
	public boolean isRunning() {
		return running.isRunning();
	}

	/**
	 * Default {@link Timeout} of 100ms.
	 * {@inheritDoc}
	 */
	@Override
	public Timeout timeout() {
		return DEFAULT_TIMEOUT;
	}

	@Override
	public String pipe(String message) throws IllegalStateException, IOException, InterruptedException, TimeoutException {
		checkNotNull(message, "message");
		return pipe(message, DEFAULT_TIMEOUT.time(), DEFAULT_TIMEOUT.unit());
	}

	@Override
	public String pipe(String message, Timeout timeout) throws IllegalStateException, IOException, InterruptedException, TimeoutException {
		checkNotNull(message, "message");
		checkNotNull(timeout, "timeout");
		return pipe(message, timeout.time(), timeout.unit());
	}

	@Override
	public String pipe(String message, long time, TimeUnit unit) throws IllegalStateException, IOException, InterruptedException, TimeoutException {
		checkNotNull(message, "message");
		checkArgument(time >= 0, "time must be non-negative: %d", time);
		checkNotNull(unit, "unit");
		checkState(isRunning(), "Underlying process is not running");
		write(message);
		return read(time, unit);
	}

	private void write(String message) throws IOException {
		final OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
		osw.write(message);
		osw.flush();
	}

	private String read(long time, TimeUnit unit) throws IOException, InterruptedException, TimeoutException {
		return read(new InputStreamReader(process.getInputStream()), time, unit);
	}

	private String read(final Reader r, long time, TimeUnit unit) throws IOException, InterruptedException, TimeoutException {
		final Sleeper s = new Sleeper() {
			@Override
			protected boolean awake() throws IOException {
				return r.ready() || !isRunning();
			}
		};
		check(s.sleep(time, unit), TimeoutException.class, "Read timed out after " + time + " " + unit);
		final StringWriter sw = new StringWriter();
		final char[] buffer = new char[1024];
		while (r.ready()) {
			final int read = r.read(buffer);
			sw.write(buffer, 0, read);
		}
		return sw.toString();
	}
}
