package com.sssta.eatwhat_re2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class FOOD_ADD extends Activity {
	private static final String TAG = "FOOD_ADD";
	private Button button_yes;
	private Button button_reset;
	private food newFood;
	private Context context; 
	public int hasFile;
	public EditText name,place1,place2,price;
	public RatingBar rate;
	private Integer id;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.foodadd);
		button_yes = (Button) findViewById((R.id.button_yes));
		button_reset = (Button) findViewById(R.id.button_reset);
		context=this.getApplicationContext();
		name = (EditText) findViewById(R.id.name);
		place1 = (EditText) findViewById(R.id.place1);
		place2 = (EditText) findViewById(R.id.place2);
		price = (EditText) findViewById(R.id.price);
		rate = (RatingBar) findViewById(R.id.rate);
		button_yes.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File f = new File(android.os.Environment.getExternalStorageDirectory()+"/eatwhat/food.xml");
				File dir= new File(android.os.Environment.getExternalStorageDirectory()+"/eatwhat/");
				String path=f.getAbsolutePath();
				File myfile=new File(path);
		        if(!myfile.exists())
		       {
		        dir.mkdirs();
		        try {
					myfile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       } 
		        FileInputStream fileIS = null;
				try {
					fileIS = new FileInputStream(path);
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//		InputStream inStream = XmlActivity.class.getClassLoader()
				//					.getResourceAsStream("food.xml");
				List<food> eat = null;
				try {
					eat = PullBuildXML.readXml(fileIS);
					for (food food : eat) {
						Log.i(TAG, food.toString());
					}
					id = eat.get(eat.size()-1).getId() + 1;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					id=1;
					Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
				}
				
			/*	for(;;)
				{
					if(name.getText().length()!=0&&place1.getText().length()!=0&&name.getText().length()!=0&&place2.getText().length()!=0
							&&price.getText().length()!=0&&rate.getRating()!=0)
					break;			
				} */
				if(name.getText().length()!=0&&place1.getText().length()!=0&&name.getText().length()!=0&&place2.getText().length()!=0
						&&price.getText().length()!=0&&rate.getRating()!=0)
				{
					try {
						newFood = new food(id, name.getText().toString(), place1
								.getText().toString(), place2.getText().toString(),
								(int) rate.getRating(), Double.parseDouble(price
										.getText().toString()));
						eat.add(newFood);
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
					//	startActivity(new Intent(FOOD_ADD.this, FOOD_ADD.class));
					//	finish();
					}
					
					 //这里通过判断已知，数据已经读入list内，问题在于数据写入部分
					/*	try {

						 FileOutputStream outStream = context.openFileOutput("food.xml",
								Context.MODE_PRIVATE);
						OutputStreamWriter writer = new OutputStreamWriter(
								outStream, "UTF-8");
						PullBuildXML.writeXML(eat, writer);
						outStream.flush();
						outStream.close(); 
					//	Log.i(TAG, newFood.toString());

					} catch (Exception e) {

						Log.e(TAG, e.toString());
						// Toast.makeText(this, "解析失败", Toast.LENGTH_LONG).show();
					}
					;*/
					startActivity(new Intent(FOOD_ADD.this, FOOD_MANAGE.class));
					finish();
				}
				else {
					Toast.makeText(context, "请输入完整信息",Toast.LENGTH_SHORT );
				}
			}
		});
		button_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FOOD_ADD.this, FOOD_ADD.class));
				finish();
			}
		});
		
	}
}
