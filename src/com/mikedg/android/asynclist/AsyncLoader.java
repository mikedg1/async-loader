package com.mikedg.android.asynclist;

import java.util.concurrent.LinkedBlockingDeque;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;


/**
 *
 * @param <E> Type of data being passed as a parameter for loading (i.e. urls, image id's, large files, dates)
 * @param <P> Type of data we are loading asynchronously
 * @param <H> Type of object that should be set with results
 */
public class AsyncLoader<E, P, H> {
    	LinkedBlockingDeque<HolderTriple<E, H>> queue = new LinkedBlockingDeque<HolderTriple<E, H>>();
    	BackgroundDoer<E, P> mBackgroundDoer;
    	Populator<P, H> mPopulator;
    	Thread mThread;
    	View mView; //ListView, GridView, or whatever so we can check stuff
    	
    	public AsyncLoader(BackgroundDoer<E, P> doer, Populator<P, H> pop, View view) {
    		mBackgroundDoer = doer;
    		mPopulator = pop;
    		mView = view;
    		
    		//Start a new thread
    		mThread = new Thread() {
    			public void run() {
    				while(true) {
    					//FIXME: need to get out of this duh
    					
						try {
							final HolderTriple<E, H> pair = queue.takeLast();
	    					final P results = mBackgroundDoer.run(pair.params);
	    					Log.d("Cache", "pos: " + pair.position + "    >="+((AbsListView)mView).getFirstVisiblePosition()  + "    <="+((AbsListView)mView).getLastVisiblePosition());
	    					if (mView instanceof AbsListView)
	    					{
	    						Handler h = new Handler(Looper.getMainLooper());
	    						h.post(new Runnable() {
	    							public void run() {

	    								mPopulator.populate(results, pair.holder);
	    							}
	    						});
	    					} //TODO: add support for Grids
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			}
    		};
    		mThread.start();
    	}
    	
    	protected void setBackgroundDoer(BackgroundDoer<E, P> backgroundDoer) {
    		mBackgroundDoer = backgroundDoer;
    	}
    	
    	//Need to copy holder right since we reuse it? no, that should never get changed
    	public void load(int position, E params, H holder) {
    		//Add to loading list
    		HolderTriple pair = new HolderTriple<E, H>(position, params, holder);
    		try {
//    			if (queue.contains(pair)) { //Wonder how performance is here?
//    				System.out.println("contains");
    				System.out.println(queue.remove(pair)); //Better to always remove?
//    			} else {
//    				System.out.println(queue.size());
//    			}
				queue.put(pair);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }