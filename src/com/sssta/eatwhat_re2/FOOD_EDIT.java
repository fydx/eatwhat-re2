package com.sssta.eatwhat_re2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class FOOD_EDIT extends Activity {
	private static final String TAG = "FOOD_EDIT";
	private Button button_yes;
	private Button button_reset;
	private food newFood,getfood;
	private Context context; 
	public int hasFile;
	public EditText name,place1,place2,price;
	public RatingBar rate;
	static public int position=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.foodedit);
		button_yes = (Button) findViewById((R.id.button_yes));
		button_reset = (Button) findViewById(R.id.button_reset);
		context=this.getApplicationContext();
		name = (EditText) findViewById(R.id.name);
		place1 = (EditText) findViewById(R.id.place1);
		place2 = (EditText) findViewById(R.id.place2);
		price = (EditText) findViewById(R.id.price);
		rate = (RatingBar) findViewById(R.id.rate);
		List<food> eat = null;
	//	getfood =new food();
		getfood = null;	
		File f = new File(android.os.Environment.getExternalStorageDirectory()+"/eatwhat/food.xml");
		String path=f.getAbsolutePath();
		File myfile=new File(path);
        if(myfile.exists())
       {
        hasFile=1;
       } 
        FileInputStream fileIS = null;
		try {
			fileIS = new FileInputStream(path);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			eat = PullBuildXML.readXml(fileIS);
			for (food food : eat) {
				Log.i(TAG, food.toString());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Toast.makeText(context, "文件解析失败", Toast.LENGTH_LONG).show();
		}
		Intent intent= getIntent();
		Bundle pos=intent.getExtras();
		if(pos!=null)
		position=pos.getInt("position");
		//Integer id=position;
		getfood=eat.get(position);
		name.setText(getfood.getName());
		place1.setText(getfood.getPlace1());
		place2.setText(getfood.getPlace2());
		price.setText(getfood.getPrice().toString());
		rate.setRating(getfood.getRate());	
		button_yes.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				try {
					File f = new File(android.os.Environment.getExternalStorageDirectory()+"/eatwhat/food.xml");
					String path=f.getAbsolutePath();
					File myfile=new File(path);
			        if(myfile.exists())
			       {
			        hasFile=1;
			       } 
			        FileInputStream fileIS = null;
					try {
						fileIS = new FileInputStream(path);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					List<food> eat = null;
					try {
						eat = PullBuildXML.readXml(fileIS);
						for (food food : eat) {
							Log.i(TAG, food.toString());
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Toast.makeText(context, "文件解析失败", Toast.LENGTH_LONG).show();
					}
				//	newFood =new food();
					newFood= null;
					newFood = new food(position, name.getText().toString(), place1
							.getText().toString(), place2.getText().toString(),
							(int) rate.getRating(), Double.parseDouble(price
									.getText().toString()));
					eat.set(position, newFood);
					// 对文件进行写入
					// 修正问题，写入sdcard/eatwhat 文件夹
					File fe = new File(android.os.Environment.getExternalStorageDirectory()+"/eatwhat/food.xml");
					FileOutputStream fileOS=new FileOutputStream(fe);
					BufferedWriter buf = new BufferedWriter (new OutputStreamWriter(fileOS));
					PullBuildXML.writeXML(eat, buf);
					buf.flush();
					buf.close();
					fileOS.close();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(context,"请完善信息" ,Toast.LENGTH_SHORT);
					//startActivity(new Intent(FOOD_EDIT.this, FOOD_EDIT.class));
				//	startActivity(new Intent(FOOD_EDIT.this, FOOD_MANAGE.class));
				//	finish();
				}
				startActivity(new Intent(FOOD_EDIT.this, FOOD_MANAGE.class));
				finish();
			}
		});
		button_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FOOD_EDIT.this, FOOD_EDIT.class));
				finish();
			}
		});	
	}
}
