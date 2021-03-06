

package com.example.compass;


import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * @author Joscha Zeltner s1361881
 * 
 * Assignment 1
 * 
 * This small app uses the magnetic field and the accelerometer sensor
 * for implementing a compass.
 * 
 * Along a compass rose, the degree and the direction of the main
 * cardinal directions, all three axis of each sensor is displayed.
 *
 */
public class MainActivity extends Activity implements SensorEventListener{

	// variables needed for Sensors
	private SensorManager sm; // Sensor manager
    private Sensor mSensor; // magnetic field sensor
    private Sensor aSensor; // accelerometer sensor
    
    // Text view variables    
    private TextView aX;
    private TextView aY;
    private TextView aZ;
    
    private TextView mX;
    private TextView mY;
    private TextView mZ;
    
    private TextView tv;
	private TextView tvDegree;
    
    // Image view variable
    private ImageView compass;
    
    //array for values of each sensor
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    
	//Gets executed on the creation of the app
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initialization(); //invoke initialisation
        calculateOrientation(); //invoke initial calculation of orientation
    }
    
    //initialize all textViews, imageViews and the sensors
	private void initialization() {
		 
        //get TextViews by ID
        aX = (TextView)findViewById(R.id.aX);
        aY = (TextView)findViewById(R.id.aY);
        aZ = (TextView)findViewById(R.id.aZ);
        mX = (TextView)findViewById(R.id.mX);
        mY = (TextView)findViewById(R.id.mY);
        mZ = (TextView)findViewById(R.id.mZ);
        
        tv = (TextView)findViewById(R.id.tv);
    	tvDegree = (TextView)findViewById(R.id.degree);
    	
    	//get ImageView by ID
    	compass = (ImageView)findViewById(R.id.compass_image);
        
    	
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        //Get an instance of SensorManager for accessing sensors
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //Determine a default sensor type, in this case is magnetometer
	    aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    //Determine a default sensor type, in this case is accelerometer
           	
	}
    
    
    //every time, one of the sensor is changed, this method gets executed
    @Override
    public void onSensorChanged(SensorEvent event){
    	
    	//Get the values from the magnetic field sensor and store them.
    	//display the values
    	if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
    	    magneticFieldValues[0] = event.values[0];
    	    magneticFieldValues[1] = event.values[1];
    	    magneticFieldValues[2] = event.values[2];
    	    //set text views
    	    mX.setText("mXaxis:"+ magneticFieldValues[0]);
    		mY.setText("mYaxis:"+ magneticFieldValues[1]);
    		mZ.setText("mZaxis:"+ magneticFieldValues[2]);
    	}
    	
    	//Get the values from the accelerometer sensor and store them.
    	//display the values
    	if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
    		accelerometerValues[0] = event.values[0];
    		accelerometerValues[1] = event.values[1];
    		accelerometerValues[2] = event.values[2];
    		//set textviews
    		aX.setText("aXaxis:"+ accelerometerValues[0]);
    		aY.setText("aYaxis:"+ accelerometerValues[1]);
    		aZ.setText("aZaxis:"+ accelerometerValues[2]);
    	}
    	
    	//update orientation on every change of values
    	calculateOrientation();
    }
    
    //the orientation needs to be calculated with the values
    //received by the sensors
    private void calculateOrientation() {
    	
    	float[] values = new float[3];
    	float[] R = new float[9];
    	
    	//get rotation matrix
    	SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
    	//get rotation around each axis out of rotation matrix
    	SensorManager.getOrientation(R, values);
    	
    	//convert rotation value to degrees
    	float degree = (float) Math.toDegrees(values[0]);
    	
    	//uncomment, if rotation around y and z axis are needed
    	//
    	//float rot_x = (float) Math.toDegrees(values[1]);
    	//float rot_y = (float) Math.toDegrees(values[2]);
    	
    	//display main cardinal points on display within a range
    	//of 10 degrees. Else display "reading direction..."
    	if ((degree >= -5) && (degree < 5)) {
    		tv.setText("North");
    	} else if ((degree >= 85) && (degree < 95)){
    		tv.setText("East");
    	} else if ((degree >= 175) || (degree < -175)){
    		tv.setText("South");
    	} else if ((degree >= -95) && (degree < -85)){
    		tv.setText("West");
    	} else {
    		tv.setText("reading direction...");
    	}
    	
    	//display orientation in degrees
    	tvDegree.setText("Degree: " + degree);
    	
    	//display and rotate wind rose according to current
    	//orientation value
    	Matrix matrix=new Matrix();
    	compass.setScaleType(ScaleType.MATRIX);   //required
    	// arguments (rotation, ratation center x, rotation center y)
    	matrix.postRotate((float) -degree, compass.getDrawable().getBounds().width()/2, compass.getDrawable().getBounds().height()/2);
    	compass.setImageMatrix(matrix);
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    	
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	sm.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    	//register the sensor when user returns to the activity
    	sm.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
    	//register the sensor when user returns to the activity
    	
    }
    
    protected void onPause(){
    	super.onPause();
    	sm.unregisterListener(this);
    	//disable the sensor
    }
	
	
	
	
	
    
    
}
