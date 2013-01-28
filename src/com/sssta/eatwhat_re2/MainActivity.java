package com.sssta.eatwhat_re2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;



import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.RenrenAuthListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {
	/** Called when the activity is first created. */
	private static final String API_KEY = "f0959c1999d74b2ab91f60c00dfba56b";
	private static final String SECRET_KEY = "e98ac6c91e05473b9aa144af4c378fcf";
	private static final String APP_ID = "197537";
	
	private Handler handler;
	private Renren renren;
	private Button button;
	private Bundle bundle;
	private SensorManager sensorManager;
	final int FOOD = 0;
	final int PLACE = 1;
	final int SETTING = 2;
	long mLastUpdateTime;  
	static final int UPDATE_INTERVAL = 150;  
	float mLastX, mLastY, mLastZ; 
	public int shakeThreshold = 1000; 
	static long ltime =0 ;
//	EditText sensor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.main);
	//	this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		renren = new Renren(API_KEY, SECRET_KEY, APP_ID, this);
        handler = new Handler();
        Log.i("message", String.valueOf(Float.parseFloat(android.os.Build.VERSION.RELEASE.substring(0,3))));
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		File f = new File(android.os.Environment.getExternalStorageDirectory()
				+ "/eatwhat/food.xml");
		String path = f.getAbsolutePath();
		File myfile = new File(path);
		if (!myfile.exists()) {
			Toast.makeText(this, "请按menu键选择食物管理，进行添加食物的操作", Toast.LENGTH_LONG).show();
		}
		FileInputStream fileIS = null;
		try {
			fileIS = new FileInputStream(path);
			
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();

		}
		try {
			List<food> foods = PullBuildXML.readXml(fileIS);
	//		List<String> place1=null;
			Set<String> placeSet=null;
			for (food food : foods) {
			//	Log.w(TAG, food.toString());
			//	place1.add(food.getPlace1());
				placeSet.add(food.getPlace1());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		button = (Button) findViewById(R.id.button1);
		// button添加监听事件
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, FOOD_SELECT.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(Renren.RENREN_LABEL, renren);
				intent.putExtras(bundle);
				startActivity(intent);
			//	startActivity(new Intent(EatwhatActivity.this,
			//			FOOD_SELECT.class));
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, FOOD, 0, "食物管理");
		menu.add(0, PLACE, 1, "人人账号关联");
		menu.add(0, SETTING, 2, "关于");
//		SubMenu place = menu.addSubMenu("MAIN")
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case FOOD:
			Intent intent = new Intent();
			/* 指定intent要启动的类 */
			intent.setClass(MainActivity.this, FOOD_MANAGE.class);
			/* 启动一个新的Activity */
			startActivity(intent);
			/* 关闭当前的Activity */
			//EatwhatActivity.this.finish();
			break;
	//	 item.setIntent(new Intent(this,FOOD_MANAGE.class));

		case PLACE:
			renrenauth();
			break;			
		case SETTING:
			Intent intent2 = new Intent();
			/* 指定intent要启动的类 */
			//intent2.setClass(MainActivity.this, About.class);
			/* 启动一个新的Activity */
			startActivity(intent2);
			/* 关闭当前的Activity */
			//EatwhatActivity.this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (renren != null) {
			renren.authorizeCallback(requestCode, resultCode, data);
		}
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
	        if (delta > shakeThreshold&&currentTime-ltime>1000) { 		// 当加速度的差值大于指定的阈值，认为这是一个摇晃  
	        	ltime=currentTime;
	        	Intent intent = new Intent(MainActivity.this, FOOD_SELECT.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(Renren.RENREN_LABEL, renren);
				intent.putExtras(bundle);
				startActivity(intent);
	        }  
	    }  
	public void renrenauth ()
	{
		final RenrenAuthListener listener = new RenrenAuthListener() 
		{

			@Override
			public void onComplete(Bundle values) {
				Log.d("test",values.toString());
				Toast.makeText(MainActivity.this, "人人账号已绑定", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onRenrenAuthError(
				RenrenAuthError renrenAuthError) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, 
								MainActivity.this.getString(R.string.auth_failed), 
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onCancelLogin() {
			}

			@Override
			public void onCancelAuth(Bundle values) {
			}
			
		};
		//RenrenAuthListener listener;
		//item.setIntent(new Intent(this, PLACE_MANAGE.class));
		renren.authorize(MainActivity.this,listener);
		//	renren.authorize(EatwhatActivity.this, null, listener, 1);
	}
	
	}
	