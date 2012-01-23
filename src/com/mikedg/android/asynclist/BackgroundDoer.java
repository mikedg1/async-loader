package com.mikedg.android.asynclist;

/**
 *
 * @param <P> Parameter type that this receives when run
 * @param <R> Result type that gets sent to populator
 */
public interface BackgroundDoer<P, R> {
	public R run(P params);
}