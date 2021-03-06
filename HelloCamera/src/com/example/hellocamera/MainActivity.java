package com.example.hellocamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		//start to intent the image capture activity
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		
		private Uri fileUri;
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		//create the uri of a file to save the image
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	
	
	
	
	
	private static Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
		//uri is defined by media file
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK){
				//Image captured and saved to fileUri specified in the Intent
				Toast.makeText(this, "Image successfully saved", Toast.LENGTH_SHORT).show();
			} else if (resultCode == RESULT_CANCELED) {
				//User cancelled the image capture
			} else {
				//Image capture failed, advise user
			}
		}
	}
		
	private static File getOutputMediaFile(int type){
		final File mediaStorageDir;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "EMFdetectingApp");
		} else {
			mediaStorageDir = new File("/storage/sdcard0/EMFdetectingApp/");
		}
		
		//if the storage directory doesn't exist
		if (!mediaStorageDir.exists()){
			if (!mediaStorageDir.mkdirs()){
				Log.d("EMFdetectingApp", "failed to create directory");
				return null;
			}
		}
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile = null;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}
		return mediaFile;
	}
		
		
		
		
	
}
