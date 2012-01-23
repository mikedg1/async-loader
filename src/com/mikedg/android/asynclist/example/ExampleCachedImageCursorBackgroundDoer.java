package com.mikedg.android.asynclist.example;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mikedg.android.asynclist.BackgroundDoer;
import com.mikedg.android.asynclist.example.ExampleCachedImageCursorAdapter.Results;

public class ExampleCachedImageCursorBackgroundDoer implements
		BackgroundDoer<ExampleCachedImageCursorAdapter.Params, ExampleCachedImageCursorAdapter.Results> {

	@Override
	public ExampleCachedImageCursorAdapter.Results run(ExampleCachedImageCursorAdapter.Params params) {
		File f = new File(params.file);
		BufferedInputStream bis;
		try {
			bis = new BufferedInputStream(new FileInputStream(f), 8192);
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inSampleSize = 8;
			Bitmap bm = BitmapFactory.decodeStream(bis, null, o);
			// System.out.println(bm.getHeight());
			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ExampleCachedImageCursorAdapter.Results results = new ExampleCachedImageCursorAdapter.Results(bm, params.position);
			return results;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
