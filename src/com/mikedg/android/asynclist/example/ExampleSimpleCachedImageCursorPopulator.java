package com.mikedg.android.asynclist.example;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.mikedg.android.asynclist.Populator;
import com.mikedg.android.asynclist.example.ExampleCachedImageCursorAdapter.Holder;

public class ExampleSimpleCachedImageCursorPopulator implements
		Populator<Bitmap, ExampleSimpleCachedImageCursorAdapter.Holder> {
	@Override
	public void populate(Bitmap bitmap, ExampleSimpleCachedImageCursorAdapter.Holder holder) {
		//Log.d("Cache","Should populate");
		if (bitmap != null) { //Means an image couldn't be decoded
			Log.d("Cache","Populating with:" + bitmap);
			holder.view.setImageBitmap(bitmap);
			
			holder.progress.setVisibility(View.INVISIBLE);
			holder.view.setVisibility(View.VISIBLE); //TODO: look into if this causes flicker
		}
	}
}
