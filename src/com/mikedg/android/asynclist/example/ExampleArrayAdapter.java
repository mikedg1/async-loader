package com.mikedg.android.asynclist.example;

import com.mikedg.android.asynclist.AsyncLoader;
import com.mikedg.android.asynclist.BackgroundDoer;
import com.mikedg.android.asynclist.Populator;
import com.mikedg.android.asynclist.R;
import com.mikedg.android.asynclist.R.id;
import com.mikedg.android.asynclist.R.layout;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

class ExampleArrayAdapter extends ArrayAdapter<String> implements BackgroundDoer<String, String>, Populator<String, ExampleArrayAdapter.Holder> {
	private ListView mListView;
    private AsyncLoader mAsyncLoader;

    public ExampleArrayAdapter(Context context, int textViewResourceId, ListView lv) {
		super(context, textViewResourceId);
		mListView = lv;
	    mAsyncLoader = new AsyncLoader(this, this, lv);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExampleArrayAdapter.Holder holder;
		if(convertView == null) {
			//System.out.println("New view");
			convertView = ((LayoutInflater)getContext().getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.li_test, null);
			convertView.setTag(new Holder((TextView)convertView.findViewById(R.id.textview1)));
		} 
		holder = (ExampleArrayAdapter.Holder)convertView.getTag();
		
		String item = getItem(position);
		
		//This is the secret sauce, call AsyncLoader.load with your parameters, and viola it loads at some point
		mAsyncLoader.load(position, item, holder);
		
		holder.view.setText("Loading...");
		
		return convertView;
	}

	@Override
	public void populate(final String results, final ExampleArrayAdapter. Holder holder) {
		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {
			public void run() {
				holder.view.setText(results);
			}
		});
		
	}

	@Override
	public String run(String params) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} //Wait a while since this is a demo
		return params;
	}
	
	public static class Holder {
		final TextView view;
		
		public Holder(TextView view) {
			this.view = view;
		}
	}
}