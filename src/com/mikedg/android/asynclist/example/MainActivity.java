package com.mikedg.android.asynclist.example;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;

import com.mikedg.android.asynclist.R;

public class MainActivity extends ListActivity {
	private ArrayAdapter<String> mArrayAdapter;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        useSimpleText();
        //usePeopleCursor();
//        hashTest();
//        useCacheImageCursor();
        useSimpleCacheImageCursor();
        
    }
    
    private void hashTest() {
		// TODO Auto-generated method stub
    	LruCache lru = new LruCache<MainActivity.HashTest, Integer>(100);
		for (int x = 0; x < 10; x++) {
			HashTest test = new HashTest("" + x, x);
			Log.d("Cache", x+":test hash:" + test.hashCode());
			
			lru.put(test, x);
		}
		
		for (int x = 0; x < 10; x++) {
			HashTest newHash = new HashTest("" + x, x);
			Log.d("Cache", x+ ":test hash:" + newHash.hashCode());
			if (lru.get(newHash) != null) {
				Log.d("Cache", "got a cach" + x);
			} else {
				Log.d("Cache", "not found" + x);
			}
		} 
		//now do string
		lru = new LruCache<String, Integer>(100);
		for (int x = 0; x < 10; x++) {
			lru.put(""+x, x);
		}
		
		for (int x = 0; x < 10; x++) {
			String newHash = "" + x;
			if (lru.get(newHash) != null) {
				Log.d("Cache", "got a cach" + x);
			} else {
				Log.d("Cache", "not found" + x);
			}
		} 
	}
    
	public static class HashTest {
		public final String string;
		public final int number;
		
		public HashTest(String string, int number) {
			this.string = string;
			this.number = number;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof HashTest) {
				if (((HashTest) o).string.equals(this.string) && ((HashTest)o).number == this.number) {
					return true;
				}
			}
			return false;
		}
		@Override
		public int hashCode() {
			return string.hashCode();
		}
	}
	
	private void usePeopleCursor() {
		Cursor cursor = this.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { Images.Media._ID, Images.Media.DATA }, null, null, null);
	
		CursorAdapter adapter = new ExampleImageCursorAdapter(this, this, cursor, getListView());
		this.setListAdapter(adapter);
	}


    private void useCacheImageCursor() {
		Cursor cursor = this.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { Images.Media._ID, Images.Media.DATA }, null, null, Images.Media.DATE_ADDED + " desc");
	
		CursorAdapter adapter = new ExampleCachedImageCursorAdapter(this, this, cursor, getListView());
		this.setListAdapter(adapter);
	}
    private void useSimpleCacheImageCursor() {
		Cursor cursor = this.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { Images.Media._ID, Images.Media.DATA }, null, null, Images.Media.DATE_ADDED + " desc");
	
		CursorAdapter adapter = new ExampleSimpleCachedImageCursorAdapter(this, this, cursor, getListView());
		this.setListAdapter(adapter);
	}

    private void useSimpleText() {
    	mArrayAdapter = new ExampleArrayAdapter(this, R.id.textview1, getListView());
        for (int x = 0; x < 1000; x++) {
            mArrayAdapter.add(String.valueOf(x));
        }
        this.setListAdapter(mArrayAdapter);
	}
}    
