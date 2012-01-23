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

public class ExampleSimpleCachedImageCursorBackgroundDoer implements
		BackgroundDoer<String, Bitmap> {

	@Override
	public Bitmap run(String path) {
		File f = new File(path);
		BufferedInputStream bis;
		try {
			bis = new BufferedInputStream(new FileInputStream(f), 8192);
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inSampleSize = 8;
			Bitmap bm = BitmapFactory.decodeStream(bis, null, o);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// System.out.println(bm.getHeight());
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
