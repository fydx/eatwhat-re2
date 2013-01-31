package com.sssta.eatwhat_re2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.renren.api.connect.android.Renren;

public class FOOD_SELECT extends Activity implements SensorEventListener{
	private Button button;
	private Button shareButton;
	private static final String TAG = "FOOD_SELECT";
	private food myfood, temp;
	private int hasFile;
	SensorManager sensorManager;
	long mLastUpdateTime;  
	static final int UPDATE_INTERVAL = 150;  
	float mLastX, mLastY, mLastZ; 
	public int shakeThreshold = 950; 
	static long ltime =0 ;
	private Renren renren;
	private String toRenren;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foodselect);
		Animation mAnimationRight; 
		mAnimationRight = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.rotate_right);
		mAnimationRight.setFillAfter(true);   
		sensorManager =(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		renren = getIntent().getParcelableExtra(Renren.RENREN_LABEL);
		TextView resultView = (TextView) this.findViewById(R.id.textView2);
		TextView resultview2 = (TextView) this.findViewById(R.id.textView3);
		resultview2.setAnimation(mAnimationRight);  
		File f = new File(android.os.Environment.getExternalStorageDirectory()
				+ "/eatwhat/food.xml");
		String path = f.getAbsolutePath();
		File myfile = new File(path);
		if (!myfile.exists()) {
			Toast.makeText(this, "无录入食物，请先进行添加食物操作", Toast.LENGTH_LONG).show();
		}
		FileInputStream fileIS = null;
		try {
			fileIS = new FileInputStream(path);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();

		}
		button = (Button) findViewById(R.id.button1);
		shareButton=(Button) findViewById(R.id.button2);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(FOOD_SELECT.this, FOOD_SELECT.class));
				finish();
			}
		});
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FOOD_SELECT.this, StatusPublishActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(Renren.RENREN_LABEL, renren);
				bundle.putString("toRenren", toRenren);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		try {
			List<food> foods = PullBuildXML.readXml(fileIS);
			StringBuilder sb = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			myfood = new food();
			temp = new food();

			for (food food : foods) {
				Log.w(TAG, food.toString());
				food.setNum_random((int) (food.getRate() * Math.random() * 10));
			}

			for (int i = 1; i < foods.size(); i++) {
				for (int j = 0; j < foods.size() - i; j++)

					if (foods.get(j).getNum_random() < foods.get(j + 1)
							.getNum_random()) {
						temp = foods.get(j);
						foods.set(j, foods.get(j + 1));
						foods.set(j + 1, temp);

					}
				// Log.i(TAG, foods.get(i).toString());
			}
			for (food food : foods) {
				Log.i(TAG, food.toString());
			}
			myfood = foods.get(0);

			sb.append(myfood.getName());
		//	sb2.append(myfood.getPlace1()).append("-")
		//			.append(myfood.getPlace2()); 
			sb2.append(myfood.getPlace1());   //更改为只显示一级地点
			resultView.setText(sb.toString());
			resultview2.setText(sb2.toString());
			toRenren=myfood.toRenren();

		} catch (Exception e) {
			Log.e(TAG, e.toString());
			Toast.makeText(this, "文件读取失败", Toast.LENGTH_LONG).show();
		}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}
	@Override
	protected void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		long currentTime = System.currentTimeMillis();  
        long diffTime = currentTime - mLastUpdateTime;  
        if (diffTime < UPDATE_INTERVAL)  
            return;  
        mLastUpdateTime = currentTime;  
        float x = event.values[0];  
        float y = event.values[1];  
        float z = event.values[2];  
        float deltaX = x - mLastX;  
        float deltaY = y - mLastY;  
        float deltaZ = z - mLastZ;  
        mLastX = x;  
        mLastY = y;  
        mLastZ = z;  
        float delta = FloatMath.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ  
                * deltaZ)  
                / diffTime * 10000;  
        if (delta > shakeThreshold&&currentTime-ltime>1000) { // 测定加速度的算法
        	ltime=currentTime;
        	Intent intent = new Intent(FOOD_SELECT.this, FOOD_SELECT.class);
			startActivity(intent);
        	finish();
        }  
	}
	

}
