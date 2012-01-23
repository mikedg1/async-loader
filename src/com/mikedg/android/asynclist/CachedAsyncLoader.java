package com.mikedg.android.asynclist;

import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;

import com.mikedg.android.asynclist.example.ExampleCachedImageCursorAdapter.Params;
import com.mikedg.android.asynclist.example.ExampleCachedImageCursorAdapter.Results;


/**
 *
 * @param <E> Type of data being passed as a parameter for loading (i.e. urls, image id's, large files, dates)
 * @param <P> Type of data we are loading asynchronously, the results
 * @param <H> Type of object that should be set with results
 */
public class CachedAsyncLoader<E, P, H> extends AsyncLoader<E, P, H> {
    	final private LruCache<E, P> mCache;
		
		public CachedAsyncLoader(final BackgroundDoer<E, P> doer, final Populator<P, H> pop, final View view, final int cacheSize) {
    		super(null, pop, view);
    		
    		mCache = new LruCache<E, P>(cacheSize);
    		setBackgroundDoer(new BackgroundDoer<E, P>() {
    			public P run(E params) {
    				P results = mCache.get(params);
    	    		if (results != null) {
    	    			if (results instanceof Results) {
    	    				Log.d("Cache", "Got cache: results:" + ((Results)results).position + "  params:" + ((Params)params).position);
    	    			}
    	    		} else {
    	    			Log.d("Cache", "Get new");
        				results = doer.run(params);
        				if (results == null || params == null) {
        					Log.d("Cache", "*****One are nulls: results:" + results + "   params:"+params);
        					//Assume this means results were bad! in my test case that's what happened...
        					//FIXME: Create a NULL identifier since we can't cache nulls?
        				} else {
        					mCache.put(params, results);
        				}
    	    		}    				
    				return results;
    			}
    		});
    	}
    	
    	//Need to copy holder right since we reuse it? no, that should never get changed
    }