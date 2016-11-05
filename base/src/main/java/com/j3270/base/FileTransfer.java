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

import static com.j3270.base.Extras.checkArgument;
import static com.j3270.base.Extras.checkNotNull;
import static com.j3270.base.Extras.checkState;
import static com.j3270.base.FileTransfer.Allocation.avblock;
import static com.j3270.base.FileTransfer.Cr.remove;
import static com.j3270.base.FileTransfer.Direction.receive;
import static com.j3270.base.FileTransfer.Exist.keep;
import static com.j3270.base.FileTransfer.Host.tso;
import static com.j3270.base.FileTransfer.Host.vm;
import static com.j3270.base.FileTransfer.Mode.ascii;
import static com.j3270.base.FileTransfer.Recfm.fixed;
import static com.j3270.base.FileTransfer.Recfm.undefined;
import static com.j3270.base.FileTransfer.Recfm.variable;
import static com.j3270.base.FileTransfer.Remap.yes;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @see http://x3270.bgp.nu/Unix/s3270-man.html#File-Transfer
 * @author Daniel Yokomizo
 */
public final class FileTransfer implements Closeable {
	private final J3270 j3270;
	private final Map<String,String> options;
	private boolean ended;

	FileTransfer(J3270 j3270, String localFile, String hostFile) {
		this.j3270 = j3270;
		this.options = new LinkedHashMap<>();
		this.options.put("HostFile", hostFile);
		this.options.put("LocalFile", localFile);
	}

	@Override
	public void close() {
		j3270.cancel();
		ended = true;
	}

	public File localFile() {
		return new File(options.get("LocalFile"));
	}

	public String hostFile() {
		return options.get("HostFile");
	}

	public FileTransfer direction(Direction direction) {
		checkNotNull(direction, "direction");
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Direction", direction.name());
		return this;
	}

	public Direction direction() {
		return optionOrDefault(receive);
	}

	public FileTransfer host(Host host) {
		checkNotNull(host, "host");
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Host", host.name());
		return this;
	}

	public Host host() {
		return optionOrDefault(tso);
	}

	public FileTransfer mode(Mode mode) {
		checkNotNull(mode, "mode");
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Mode", mode.name());
		return this;
	}

	public Mode mode() {
		return optionOrDefault(ascii);
	}

	public FileTransfer cr(Cr cr) {
		checkNotNull(cr, "cr");
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Cr", cr.name());
		return this;
	}

	public Cr cr() {
		return optionOrDefault(remove);
	}

	public FileTransfer remap(Remap remap) {
		checkNotNull(remap, "remap");
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Remap", remap.name());
		return this;
	}

	public Remap remap() {
		return optionOrDefault(yes);
	}

	public FileTransfer exist(Exist exist) {
		checkNotNull(exist, "exist");
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Exist", exist.name());
		return this;
	}

	public Exist exist() {
		return optionOrDefault(keep);
	}

	public FileTransfer recfm(Recfm recfm) {
		checkNotNull(recfm, "recfm");
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Recfm", recfm.name());
		return this;
	}

	public Recfm recfm() {
		return optionOrNull(Recfm.class);
	}

	public FileTransfer lrecl(long lrecl) {
		checkArgument(lrecl >= 0, "Lrecl must not be negative: %d", lrecl);
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Lrecl", Long.toString(lrecl));
		return this;
	}

	public Long lrecl() {
		return longOptionOrNull("Lrecl");
	}

	public FileTransfer blksize(long blksize) {
		checkArgument(blksize >= 0, "Blksize must not be negative: %d", blksize);
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Blksize", Long.toString(blksize));
		return this;
	}

	public Long blksize() {
		return longOptionOrNull("Blksize");
	}

	public FileTransfer allocation(Allocation allocation) {
		checkNotNull(allocation, "allocation");
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Allocation", allocation.name());
		return this;
	}

	public Allocation allocation() {
		return optionOrNull(Allocation.class);
	}

	public FileTransfer primarySpace(long primarySpace) {
		checkArgument(primarySpace >= 0, "PrimarySpace must not be negative: %d", primarySpace);
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("PrimarySpace", Long.toString(primarySpace));
		return this;
	}

	public Long primarySpace() {
		return longOptionOrNull("PrimarySpace");
	}

	public FileTransfer secondarySpace(long secondarySpace) {
		checkArgument(secondarySpace >= 0, "SecondarySpace must not be negative: %d", secondarySpace);
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("SecondarySpace", Long.toString(secondarySpace));
		return this;
	}

	public Long secondarySpace() {
		return longOptionOrNull("SecondarySpace");
	}

	public FileTransfer avblock(long avblock) {
		checkArgument(avblock >= 0, "Avblock must not be negative: %d", avblock);
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("Avblock", Long.toString(avblock));
		return this;
	}

	public Long avblock() {
		return longOptionOrNull("Avblock");
	}

	public FileTransfer bufferSize(int bufferSize) {
		checkArgument((bufferSize >= 256) && (bufferSize <= 32768), "Invalid BufferSize (must be in [256..32768]): %d", bufferSize);
		checkState(!ended, "File Transfer already ended: %s", this);
		options.put("BufferSize", Integer.toString(bufferSize));
		return this;
	}

	public int bufferSize() {
		final String s = options.get("BufferSize");
		return (s == null) ? 4096 : Integer.parseInt(s);
	}

	/**
	 * @throws IllegalStateException
	 *           if this File Transfer already ended
	 *           if incompatible options are defined
	 *           if required options are undefined
	 * @throws J3270Exception
	 *           if the action fails for any reason
	 * @see http://x3270.bgp.nu/Unix/s3270-man.html#File-Transfer
	 */
	public void end() throws IllegalStateException, J3270Exception {
		checkState(!ended, "File Transfer already ended: %s", this);
		checkOptions();
		final String s = toString();
		j3270.transfer(s);
		ended = true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Transfer");
		char separator = '(';
		for (final Map.Entry<String,String> e : options.entrySet()) {
			sb.append(separator);
			final String k = e.getKey();
			final String v = e.getValue();
			if ((v.indexOf(' ') < 0) && (v.indexOf(',') < 0) && (v.indexOf(')') < 0)) {
				sb.append(k);
				sb.append('=');
				sb.append(v);
			} else {
				sb.append('"');
				sb.append(k);
				sb.append('=');
				sb.append(v);
				sb.append('"');
			}
			separator = ',';
		}
		sb.append(')');
		return sb.toString();
	}

	private void checkOptions() {
		final Mode mode = mode();
		final Recfm recfm = recfm();
		final Host host = host();
		final Allocation allocation = allocation();
		final Long primarySpace = primarySpace();
		final Long secondarySpace = secondarySpace();

		final Checker c = new Checker();
		c.check(!has("Cr") || (mode == ascii), "Cr can only be specified for ascii Mode");
		c.check(!has("Remap") || (mode == ascii), "Remap can only be specified for ascii Mode");
		c.check((recfm == null) || (host == tso) || (host == vm), "Recfm can only be specified for tso or vm Host");
		c.check((recfm != undefined) || (host == tso), "Undefined Recfm can only be specified for tso Host");
		c.check(!has("Lrecl") || (recfm != null), "Lrecl can only be specified if Recfm is specified");
		c.check(!has("Lrecl") || (recfm == fixed) || (recfm == variable), "Lrecl can only be specified for fixed or variable Recfm");
		c.check(!has("Blksize") || (host == tso) || (host == vm), "Blksize can only be specified for tso or vm Host");
		c.check((allocation == null) || (host == tso), "Allocation can only be specified for tso Host");
		c.check((primarySpace == null) || (host == tso), "PrimarySpace can only be specified for tso Host");
		c.check((secondarySpace == null) || (host == tso), "SecondarySpace can only be specified for tso Host");
		c.check((allocation == null) == (primarySpace == null), "Both or neither of Allocation and PrimarySpace must be specified");
		c.check((secondarySpace == null) || (allocation != null), "SecondarySpace can only be specified if Allocation is specified");
		c.check((allocation == avblock) == has("Avblock"), "Both or neither of avblock Allocation and Avblock must be specified");
		c.check(!has("Avblock") || (host == tso), "Avblock can only be specified for tso Host");
		c.verify(this);
	}

	private boolean has(String key) {
		return options.containsKey(key);
	}

	private <E extends Enum<E>> E optionOrDefault(E e) {
		@SuppressWarnings("unchecked")
		final Class<E> enumType = (Class<E>) (Object) e.getClass();
		final String name = options.get(enumType.getSimpleName());
		return (name == null) ? e : Enum.valueOf(enumType, name);
	}

	private <E extends Enum<E>> E optionOrNull(Class<E> enumType) {
		final String name = options.get(enumType.getSimpleName());
		return (name == null) ? null : Enum.valueOf(enumType, name);
	}

	private Long longOptionOrNull(String key) {
		final String s = options.get(key);
		return (s == null) ? null : Long.valueOf(s);
	}

	public enum Direction {
		send, receive
	}

	public enum Host {
		tso, vm, cics
	}

	public enum Mode {
		ascii, binary
	}

	public enum Cr {
		remove, add, keep
	}

	public enum Remap {
		yes, no
	}

	public enum Exist {
		keep, replace, append
	}

	public enum Recfm {
		fixed, variable, undefined
	}

	public enum Allocation {
		tracks, cylinders, avblock
	}

	private static class Checker {
		private final List<String> list;

		public Checker() {
			this.list = new ArrayList<>();
		}

		public void check(boolean condition, String message) {
			if (condition) { return; }
			this.list.add(message);
		}

		public void verify(Object title) {
			if (list.isEmpty()) { return; }
			final StringBuilder sb = new StringBuilder();
			sb.append("Invalid ").append(title).append(":");
			for (final String s : list) {
				sb.append("\r\n\t").append(s);
			}
			throw new IllegalStateException(sb.toString());
		}
	}
}
