package com.mikedg.android.asynclist.example;

import android.util.Log;
import android.view.View;

import com.mikedg.android.asynclist.Populator;
import com.mikedg.android.asynclist.example.ExampleCachedImageCursorAdapter.Holder;

public class ExampleCachedImageCursorPopulator implements
		Populator<ExampleCachedImageCursorAdapter.Results, ExampleCachedImageCursorAdapter.Holder> {
	@Override
	public void populate(ExampleCachedImageCursorAdapter.Results results, Holder holder) {
		//Log.d("Cache","Should populate");
		if (results != null) { //Means an image couldn't be decoded
			Log.d("Cache","Populating:" + results.position + " with:" + results.bitmap);
			holder.view.setImageBitmap(results.bitmap);
			
			holder.progress.setVisibility(View.INVISIBLE);
			holder.view.setVisibility(View.VISIBLE); //TODO: look into if this causes flicker
			holder.textView.setText(String.valueOf(results.position)); //Setting position here would be nice... hmmm
		}
	}
}
