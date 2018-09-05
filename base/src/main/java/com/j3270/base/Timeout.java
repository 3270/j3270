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

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Duration and {@link TimeUnit} to be used in operations that can timeout.
 * @author Daniel Yokomizo
 */
public final class Timeout implements Serializable {
	private static final long serialVersionUID = -5512159324397488721L;
	private final long time;
	private final TimeUnit unit;

	/**
	 * Creates a {@link Timeout} for a specified duration and unit.
	 * 
	 * @param time
	 *          duration for this {@link Timeout}
	 * @param unit
	 *          {@link TimeUnit} for this {@link Timeout}
	 * @throws IllegalArgumentException
	 *           if time is negative
	 */
	public Timeout(long time, TimeUnit unit) {
		checkArgument(time > 0, "Time must be non-negative: %d", time);
		checkNotNull(unit, "unit");
		this.time = time;
		this.unit = unit;
	}

	/**
	 * @return this instance's duration
	 */
	public long time() {
		return time;
	}

	/**
	 * @return this instance's {@link TimeUnit}
	 */
	public TimeUnit unit() {
		return unit;
	}

	/**
	 * @param unit
	 *          target unit
	 * @return duration in the target unit
	 */
	public long to(TimeUnit unit) {
		checkNotNull(unit, "unit");
		return unit.convert(this.time, this.unit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof Timeout) && equal(this, (Timeout) obj));
	}

	private static boolean equal(Timeout o1, Timeout o2) {
		return (o1.time == o2.time) && (o1.unit.ordinal() == o2.unit.ordinal());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return 0x3dd8cf63 ^ (31 * Extras.hashCode(time) + unit.name().hashCode());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(40);
		sb.append("Timeout(");
		sb.append(time);
		sb.append(',');
		sb.append(unit.name());
		sb.append(')');
		return sb.toString();
	}
}
