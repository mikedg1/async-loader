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
import com.mikedg.android.asynclist.Sizable;
import com.mikedg.android.asynclist.example.MainActivity.HashTest;

public class ExampleCachedImageCursorAdapter extends CursorAdapter {
		
		/**
		 * 
		 */
		private final Context mContext;
		private AsyncLoader<Params, Results, Holder> mAsyncLoader;
private ListView mListView; //FIXME: remove this , just for debugging
		public ExampleCachedImageCursorAdapter(MainActivity mainActivity, Context context, Cursor c, ListView lv) {
			super(context, c);
			this.mContext = context;
		    mAsyncLoader = new CachedAsyncLoader<Params, Results, Holder>(new ExampleCachedImageCursorBackgroundDoer(), new ExampleCachedImageCursorPopulator(), lv, 16 * 1024 * 1024); //16 megabytes
		    
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
			holder.textView.setText("loading...");
			
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

			mAsyncLoader.load(position, new Params(path, position), holder);
		}
		
		public class Holder {
			final ImageView view;
			final TextView textView;
			final ProgressBar progress;
//			final Params params;
			
			public Holder(ImageView view, TextView textView, ProgressBar progress) { //, Params params) {
				this.view = view;
				this.textView = textView;
				this.progress = progress;
//				this.params = params;
			}
		}
		
		public static class Params {
			public final String file;
			public final int position;
			
			public Params(String file, int position) {
				this.file = file;
				this.position = position;
			}
			
//			public Params() {
//			}
			
			@Override
			public int hashCode() {
				//FIXME: this is probably not great :)
				Log.d("Cache", "filehash:"+file.hashCode() + " pos:"+ position + " file:" + file);
//				return 1;
				return file.hashCode() ^ (position * 17); //Since it's low, multiply by 17 to spread the bytes out and xor it, for better distribution
			}
			
			@Override
			public boolean equals(Object o) {
				if (o instanceof Params) {
					if (((Params) o).file.equals(this.file) && ((Params)o).position == this.position) {
						return true;
					}
				}
				return false;
			}
		}

		public static class Results implements Sizable {
			public final Bitmap bitmap;
			public final int position;
			
			public Results(Bitmap bitmap, int position) {
				this.bitmap = bitmap;
				this.position = position;
			}

			@Override
			public int sizeOf() {
				return bitmap.getByteCount();
			}
		}
	}