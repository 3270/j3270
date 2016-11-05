package com.j3270.base;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.concurrent.TimeUnit;

class Sleeper {
	protected boolean awake() throws Exception {
		return false;
	}

	public boolean sleep(long time, TimeUnit unit) {
		final long millis = unit.toMillis(time);
		final int nanos = (int) max(min(unit.toNanos(time) - (millis * 1000 * 1000), Integer.MAX_VALUE), Integer.MIN_VALUE);
		final int iterations = (millis > 0) ? (int) min(10000, millis) : min(10000, nanos);
		try {
			for (int i = 0; (i < iterations) && !awake(); i++) {
				try {
					Thread.sleep(millis / iterations, nanos / iterations);
				} catch (InterruptedException e) {
					break;
				}
			}
			return awake();
		} catch (Exception e) {
			return false;
		}
	}
}
