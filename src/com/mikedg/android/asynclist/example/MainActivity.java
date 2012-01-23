package com.mikedg.android.asynclist.example;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images;
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
        //useCacheImageCursor();
        useSimpleCacheImageCursor();
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
