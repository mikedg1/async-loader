package com.mikedg.android.asynclist;


/**
 *
 * @param <R> Result type to expect
 * @param <H> Holder type, the views to populate with the results
 */
public interface Populator<R, H> {
	public void populate(R results, H holder);
}