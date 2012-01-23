package com.mikedg.android.asynclist.example;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.mikedg.android.asynclist.AsyncLoader;
import com.mikedg.android.asynclist.BackgroundDoer;
import com.mikedg.android.asynclist.Populator;
import com.mikedg.android.asynclist.R;
import com.mikedg.android.asynclist.R.id;
import com.mikedg.android.asynclist.R.layout;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ExampleImageCursorAdapter extends CursorAdapter implements BackgroundDoer<String, Bitmap>, Populator<Bitmap, ExampleImageCursorAdapter.Holder>{
		
		/**
		 * 
		 */
		private final Context mContext;
		private AsyncLoader<String, R, ExampleImageCursorAdapter.Holder> mAsyncLoader;

		public ExampleImageCursorAdapter(MainActivity mainActivity, Context context, Cursor c, ListView lv) {
			super(context, c);
			this.mContext = context;
		    mAsyncLoader = new AsyncLoader(this, this, lv);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = ((LayoutInflater)mContext.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.li_complex_image, null);
			view.setTag(new Holder((ImageView)view.findViewById(R.id.imageView1),(TextView)view.findViewById(R.id.textview1)));
			return view;
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Holder holder = (Holder)view.getTag();
			int position = cursor.getPosition();
			holder.textView.setText(String.valueOf(position));
			
			String path = cursor.getString(cursor.getColumnIndex(Images.Media.DATA));
			System.out.println(path);
			mAsyncLoader.load(position, path, holder);
		}
		
		public class Holder {
			final ImageView view;
			final TextView textView;
			
			public Holder(ImageView view, TextView textView) {
				this.view = view;
				this.textView = textView;
			}
		}

		@Override
		public void populate(Bitmap results, Holder holder) {
			//FIXME: This is so common, it shold always 
			if (results != null) {
				holder.view.setImageBitmap(results);
			}
		}

		@Override
		public Bitmap run(String params) {
			// TODO Auto-generated method stub
			File f = new File(params);
			BufferedInputStream bis;
			try {
				bis = new BufferedInputStream(new FileInputStream(f), 8192);
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inSampleSize = 8;
		        Bitmap bm = BitmapFactory.decodeStream(bis, null, o);
//		        System.out.println(bm.getHeight());
		        try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return bm;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}