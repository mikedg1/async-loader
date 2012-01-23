package com.mikedg.android.asynclist.example;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikedg.android.asynclist.AsyncLoader;
import com.mikedg.android.asynclist.CachedAsyncLoader;
import com.mikedg.android.asynclist.R;

public class ExampleSimpleCachedImageCursorAdapter extends CursorAdapter {
		
		/**
		 * 
		 */
		private final Context mContext;
		private AsyncLoader<String, Bitmap, Holder> mAsyncLoader;
private ListView mListView; //FIXME: remove this , just for debugging
		public ExampleSimpleCachedImageCursorAdapter(MainActivity mainActivity, Context context, Cursor c, ListView lv) {
			super(context, c);
			this.mContext = context;
		    mAsyncLoader = new CachedAsyncLoader<String, Bitmap, Holder>(new ExampleSimpleCachedImageCursorBackgroundDoer(), new ExampleSimpleCachedImageCursorPopulator(), lv, 70);
		    mListView = lv;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = ((LayoutInflater)mContext.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.li_complex_image, null);
			view.setTag(new Holder((ImageView)view.findViewById(R.id.imageView1),(TextView)view.findViewById(R.id.textview1), (ProgressBar)view.findViewById(R.id.progressBar1)));//, new Params()));
			return view;
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Holder holder = (Holder)view.getTag();
			int position = cursor.getPosition();
			holder.textView.setText(position);
			
			holder.progress.setVisibility(View.VISIBLE);
			holder.view.setVisibility(View.INVISIBLE); //TODO: look into if this causes flicker
			
			String path = cursor.getString(cursor.getColumnIndex(Images.Media.DATA));
			System.out.println(path);
			/*
			 * The secret sauce
			 */
//			holder.params.file = path;
//			holder.params.position = position;
			Log.d("Cache", "Binding: pos: " + position + "    >="+mListView.getFirstVisiblePosition()  + "    <="+mListView.getLastVisiblePosition());

			mAsyncLoader.load(position, path, holder);
		}
		
		public class Holder {
			final ImageView view;
			final TextView textView;
			final ProgressBar progress;
			
			public Holder(ImageView view, TextView textView, ProgressBar progress) { //, Params params) {
				this.view = view;
				this.textView = textView;
				this.progress = progress;
//				this.params = params;
			}
		}
	}