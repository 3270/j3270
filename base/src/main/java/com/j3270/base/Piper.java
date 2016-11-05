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

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Abstracts the communication with some channel to transfer messages as an rendezvous mechanism.
 * 
 * @author Daniel Yokomizo
 */
public interface Piper extends Closeable {
	/**
	 * @return if this instance is running;
	 */
	boolean isRunning();

	/**
	 * @return the default timeout for {@link #pipe(String)} operations.
	 */
	Timeout timeout();

	/**
	 * Sends a message and receives response message back. Uses the default {@link #timeout()}.
	 * 
	 * @param message
	 *          message to be transferred
	 * @return the message transferred back
	 * @throws IllegalStateException
	 *           if the Piper is not running
	 * @throws IOException
	 *           if an I/O error occurs while piping the message
	 * @throws InterruptedException
	 *           if the process is interrupted while piping the message
	 * @throws TimeoutException
	 *           if the operation timed out while while piping the message
	 * @see #timeout()
	 */
	String pipe(String message) throws IllegalStateException, IOException, InterruptedException, TimeoutException;

	/**
	 * Sends a message and receives response message back.
	 * 
	 * @param message
	 *          message to be transferred
	 * @param timeout
	 *          timeout to wait for the transfer to complete
	 * @return the message transferred back
	 * @throws IllegalStateException
	 *           if the Piper is not running
	 * @throws IOException
	 *           if an I/O error occurs while piping the message
	 * @throws InterruptedException
	 *           if the process is interrupted while piping the message
	 * @throws TimeoutException
	 *           if the operation timed out while while piping the message
	 */
	String pipe(String message, Timeout timeout) throws IllegalStateException, IOException, InterruptedException, TimeoutException;

	/**
	 * Sends a message and receives response message back.
	 * 
	 * @param message
	 *          message to be transferred
	 * @param time
	 *          duration to wait for the transfer to complete
	 * @param unit
	 *          {@link TimeUnit} used for the {@code duration}
	 * @return the message transferred back
	 * @throws IllegalStateException
	 *           if the Piper is not running
	 * @throws IOException
	 *           if an I/O error occurs while piping the message
	 * @throws InterruptedException
	 *           if the process is interrupted while piping the message
	 * @throws TimeoutException
	 *           if the operation timed out while while piping the message
	 */
	String pipe(String message, long time, TimeUnit unit) throws IllegalStateException, IOException, InterruptedException, TimeoutException;
}