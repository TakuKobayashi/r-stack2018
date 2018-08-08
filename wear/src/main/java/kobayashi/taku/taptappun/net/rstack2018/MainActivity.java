package kobayashi.taku.taptappun.net.rstack2018;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends WearableActivity implements SensorEventListener{

    private TextView mTextView;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Config.TAG, "Resume");
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE), SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(Config.TAG, "Sensor");
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            for(float heart : event.values){
                Log.d(Config.TAG, String.valueOf(heart));
                //心拍数を表示
                mTextView.setText(String.valueOf(heart));
            }
        }
        /*
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(Config.TAG, "acceler");
            for(float heart : event.values){
                Log.d(Config.TAG, String.valueOf(heart));
            }
        }
        */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(Config.TAG, "ac");
    }
}
