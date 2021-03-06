package com.example.hellolocation;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView textLat;
	TextView textLong;
	LocationManager locationManager;
	LocationListener locationlistener;
	String bestprovider;
	Criteria criteria;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textLat = (TextView)findViewById(R.id.textLat);
		textLong = (TextView)findViewById(R.id.textLong);
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationlistener = new mylocationlistener();
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Toast.makeText(this,  "Open GPS",  Toast.LENGTH_LONG).show();
		}
		
		//Update the current activity periodically
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
		
		bestprovider = locationManager.getBestProvider(getcriteria(), true);
		
		textLat.setText("init");
		textLong.setText("init");
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	@Override
	protected void onPause() {
		//Remove the listener when the activity is not in use
		locationManager.removeUpdates(locationlistener);
		super.onPause();
	}



	@Override
	protected void onResume() {
		//register the listener
		locationManager.requestLocationUpdates(bestprovider, 0, 0, locationlistener);
		super.onResume();
	}



	class mylocationlistener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			if (location != null){
				double tlat = location.getLatitude();
				double tlong = location.getLongitude();
				textLat.setText(Double.toString(tlat));
				textLong.setText(Double.toString(tlong));
			}
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private Criteria getcriteria(){
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}
	
}


