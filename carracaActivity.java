package com.carraca;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CarracaActivity extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */
	
    private long last_update = 0, last_movement = 0;
    private float prevX = 0, prevY = 0, prevZ = 0;
    private float curX = 0, curY = 0, curZ = 0;	
    
    private ImageView carraca;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //carraca = (ImageView) findViewById(R.id.imageView1);
        //carraca.setBackgroundResource(R.drawable.carraca);
        
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        /*MediaPlayer mp = MediaPlayer.create(getBaseContext(),
                R.raw.carraca);
        mp.start();
        mp.setOnCompletionListener(new OnCompletionListener() {            
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });*/        
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);        
        if (sensors.size() > 0) {
        	sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }
    
    @Override
    protected void onStop() {
    	SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);    	
        sm.unregisterListener(this);
        super.onStop();
    }

	//@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	//@Override
	public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
        	long current_time = event.timestamp;
            
            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];
            
            if (prevX == 0 && prevY == 0 && prevZ == 0) {
                last_update = current_time;
                last_movement = current_time;
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
            }

            long time_difference = current_time - last_update;
            if (time_difference > 0) {
                float movement = Math.abs((curX + curY + curZ) - (prevX - prevY - prevZ)) / time_difference;
                int limit = 1500;
                float min_movement = 1E-6f;
                if (movement > min_movement) {
                    if (current_time - last_movement >= limit) {      
                    	
                        Toast.makeText(getApplicationContext(), "Hay movimiento de " + movement, Toast.LENGTH_SHORT).show();
                    	
                    	/*MediaPlayer mp = MediaPlayer.create(getBaseContext(),
                                R.raw.carraca);
                        mp.start();
                        mp.setOnCompletionListener(new OnCompletionListener() {            
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });  */                  	
                    	
                    }
                    last_movement = current_time;
                }
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
                last_update = current_time;
            }            
            
            ((TextView) findViewById(R.id.textView1)).setText("Aceler—metro X: " + curX);
            ((TextView) findViewById(R.id.textView2)).setText("Aceler—metro Y: " + curY);
            ((TextView) findViewById(R.id.textView3)).setText("Aceler—metro Z: " + curZ);
        }

	}        
    
}