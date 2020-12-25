package com.koishi.demo.sensor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.util.Log;
import android.widget.ImageView;
import android.view.Display;
import android.graphics.Point;

public class MainActivity extends AppCompatActivity {
    
    public TextView tx,ty,tz;
    public ImageView cs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
        
        tx=(TextView)findViewById(R.id.tx);
        ty=(TextView)findViewById(R.id.ty);
        tz=(TextView)findViewById(R.id.tz);
        
        cs=(ImageView)findViewById(R.id.cs);
        cs.setX(gx);
        cs.setY(gy);
        
    }
    
    
    private SensorManager mSensorManager;
    private Sensor mSensor;
    
 
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
    }
 
    private float timestamp = 0;
    private float angle[] = new float[3];
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float gx = 0,gy = 0,gz = 0;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.accuracy != 0) {
                int type = sensorEvent.sensor.getType();
                switch (type) {
                    case Sensor.TYPE_GYROSCOPE:
                        if (timestamp != 0) {
                            final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                            angle[0] += sensorEvent.values[0] * dT;
                            angle[1] += sensorEvent.values[1] * dT;
                            angle[2] += sensorEvent.values[2] * dT;
							
 
                            float anglex = (float) Math.toDegrees(angle[0]);
                            float angley = (float) Math.toDegrees(angle[1]);
                            float anglez = (float) Math.toDegrees(angle[2]);
                            
                            if(gx != 100){
                                float c = gx - anglex;
                                if(Math.abs(c) >= 0.5 ){
                                    Log.d("================", "anglex------------>" + (gx - anglex));
                                    gx = anglex*3;
                                    tx.setText("X Position: "+gx);
                                    cs.setY(gx);
                                }
 
                            }else{
                                gx = anglex*3;
                                tx.setText("X Position: "+gx);
                                cs.setY(gx);
                            }
                            if(gy != 100){
                                float c = gy - angley;
                                if(Math.abs(c) >= 0.5 ){
                                    Log.d("================", "anglex------------>" + (gy - angley));
                                    gy = angley*3;
                                    ty.setText("Y Position: "+gy);
                                    cs.setX(gy);
                                }
                            }else{
                                gy = angley*3;
                                ty.setText("Y Position: "+gy);
                                cs.setX(gy);
                            }
                            if(gz != 100){
                                Log.d("================", "anglex------------>" + (gz - anglez));
                            }
                            
 
                            gz = anglez;
                            tz.setText("Z Position: "+gz);
 
                        }
                        timestamp = sensorEvent.timestamp;
                        break;
                }
            }
        }
 
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
 
        }
    };

    
}
