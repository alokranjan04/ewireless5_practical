package com.example.compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

	TextView tv;
    private SensorManager sm;
    
    //we need two sensors in this application
    private Sensor aSensor;
    private Sensor mSensor;
    
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //user defined methods
        initialization();
        calculateOrientation();
    }


    private void calculateOrientation() {
    	
    	float[] values = new float[3];
    	float[] R = new float[9];
    	
    	SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
    	SensorManager.getOrientation(R, values);
    	
    	float degree = (float) Math.toDegrees(values[0]);
    	
    	// values[1] = (float) Math.toDegrees(values[1]);
    	
    	// values[2] = (float) Math.toDegrees(values[2]);
    	
    	if (degree >= -5 && degree < 5) {
    		tv.setText("North");
    	}
    			
	}


	private void initialization() {
		tv = (TextView)findViewById(R.id.tvDirection);
		
		 sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	     //Get an instance of SensorManager for accessing sensors
		 aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	     //Determine a default sensor type, in this case is magnetometer
	     mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	     //Determine a default sensor type, in this case is magnetometer
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	
	@Override
	protected void onPause() {
		super.onPause();
		//disable the sensor
    	sm.unregisterListener(this);
	}


	
	@Override
	protected void onResume() {
		super.onResume();
		//register the sensor when user returns to the activity
		sm.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);

	}


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	private float x,y,z;
    private double h;
	@Override
	public void onSensorChanged(SensorEvent event) {
		//read sensor value from SensorEvent
    	x = event.values[0];
    	y = event.values[1];
    	z = event.values[2];
    	//calculate the vector length
    	h = Math.sqrt(event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2]);
    	
    	//set the values from sensor
    	if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
    	    magneticFieldValues[0] = x;
    	    magneticFieldValues[1] = y;
    	    magneticFieldValues[2] = z;
    	    magneticFieldValues[3] = (float) h;
    	}
    	
    	if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
    		accelerometerValues[0] = x;
    		accelerometerValues[1] = y;
    		accelerometerValues[2] = z;
    		accelerometerValues[3] = (float) h;
    	}
		
		calculateOrientation();
		
	}
	
	
	
	
    
    
}