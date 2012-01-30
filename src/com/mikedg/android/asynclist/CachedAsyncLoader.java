package com.mikedg.android.asynclist;

import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;

/**
 * 
 * @param <E>
 *            Type of data being passed as a parameter for loading (i.e. urls,
 *            image id's, large files, dates)
 * @param <P>
 *            Type of data we are loading asynchronously, the results
 * @param <H>
 *            Type of object that should be set with results
 */
public class CachedAsyncLoader<E, P, H> extends AsyncLoader<E, P, H> {
	final private LruCache<E, P> mCache;

	public CachedAsyncLoader(final BackgroundDoer<E, P> doer,
			final Populator<P, H> pop, final View view, final int cacheSize) {
		super(null, pop, view);

		mCache = new LruCache<E, P>(cacheSize) {
			@Override
			protected int sizeOf(Object key, Object value) {
				// FIXME: assume key is negligible
				if (value instanceof Sizable) {
					return ((Sizable) value).sizeOf();
				} else {
					// Assumes one for non sizables
					return 1;
				}
			}
		};
		setBackgroundDoer(new BackgroundDoer<E, P>() {
			public P run(E params) {
				P results = mCache.get(params);
				if (results != null) {
					//FIXME: This might be redundant since we probably shouldn't get here if this was in cache
					Log.d("Cache", "Got cache:");
				} else {
					Log.d("Cache", "Get new");
					results = doer.run(params);
					if (results == null || params == null) {
						Log.d("Cache", "*****One are nulls: results:" + results
								+ "   params:" + params);
						// Assume this means results were bad! in my test case
						// that's what happened...
						// FIXME: Create a NULL identifier since we can't cache
						// nulls?
					} else {
						mCache.put(params, results);
					}
				}
				return results;
			}
		});
	}

	// Need to copy holder right since we reuse it? no, that should never get
	// changed
	// Need to copy holder right since we reuse it? no, that should never get
	// changed
	public void load(int position, E params, H holder) {
		P results = mCache.get(params);
		if (results != null) {
			//We do this here instead of a custom backgrounder so we can load this instantly
			//This is called from UI thread right?
			Log.d("Cache", "Got cache:");
			mPopulator.populate(results, holder);
			//FIXME: this is a bit inelegant since we don't do anything with pair... hmmm
			//But we want to make sure we take any waiting ones out so they dont load
			//FIXME: we likely want to load but not populate? but we also don't want to waste caches that are close with a long list hmmm
    		HolderTriple pair = new HolderTriple<E, H>(position, params, holder);
			queue.remove(pair); //Better to always remove?
		} else {
			super.load(position, params, holder);
		}
	}
}