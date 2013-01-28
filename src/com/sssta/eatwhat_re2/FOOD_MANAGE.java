package com.sssta.eatwhat_re2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FOOD_MANAGE extends Activity {

	private static final String TAG = "FOOD_MANAGE";
	private Button button;
	private Button button2;
	private int hasFile;
	final int EDIT = 1;
	final int DEL = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foodmanage);
		ListView list = (ListView) findViewById(R.id.listView1);
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

		button = (Button) findViewById(R.id.button_finish);
		button2 = (Button) findViewById(R.id.button_add);
		// button添加监听事件
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(FOOD_MANAGE.this,
						MainActivity.class));
				finish();

			}
		});
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(FOOD_MANAGE.this, FOOD_ADD.class));
			}
		});

		// TextView resultView = (TextView)this.findViewById(R.id.textView1);
		File f = new File(android.os.Environment.getExternalStorageDirectory()
				+ "/eatwhat/food.xml");
		String path = f.getAbsolutePath();
		
		File myfile = new File(path);
		if (myfile.exists()) {
			hasFile = 1;
		}
		FileInputStream fileIS = null;
		try {
			fileIS = new FileInputStream(path);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();

		}
		// InputStream inStream =
		// XmlActivity.class.getClassLoader().getResourceAsStream("food.xml");

		try {
			Log.e(TAG, "SUCCESS");
			List<food> eat = PullBuildXML.readXml(fileIS);
			
			// StringBuilder sb = new StringBuilder();
			for (food food : eat) {
				// sb.append(food.toString());
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ItemTitle", food.getName());
				map.put("ItemText", food.toString2());
				mylist.add(map);
			}
			// resultView.setText(sb.toString());
			SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
					mylist,// 数据来源
					R.layout.listitem,// ListItem的XML实现

					// 动态数组与ListItem对应的子项
					new String[] { "ItemTitle", "ItemText" },

					// ListItem的XML文件里面的两个TextView ID
					new int[] { R.id.ItemTitle, R.id.ItemText });
			// 添加并且显示
			list.setAdapter(mSchedule);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			Toast.makeText(this, "文件解析失败", Toast.LENGTH_LONG).show();
		}
		// 添加长按点击
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo)
			{
				menu.setHeaderTitle("请选择");
				menu.add(0, EDIT, 0, "修改食物");
				menu.add(0, DEL, 0, "删除食物");
			}
		});
	}

	public boolean onContextItemSelected(MenuItem item) {
		int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
		switch (item.getItemId()) {

		case DEL: {
			// setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目");
			//int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;// 获取点击了第几行
			File f = new File(
					android.os.Environment.getExternalStorageDirectory()
							+ "/eatwhat/food.xml");
			String path = f.getAbsolutePath();
			File myfile = new File(path);
			if (myfile.exists()) {
				hasFile = 1;
			}
			FileInputStream fileIS = null;
			try {
				fileIS = new FileInputStream(path);
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();

			}
			try {
				List<food> eat = PullBuildXML.readXml(fileIS);
				eat.remove(selectedPosition);
				File fe = new File(
						android.os.Environment.getExternalStorageDirectory()
								+ "/eatwhat/food.xml");
				FileOutputStream fileOS = new FileOutputStream(fe);
				BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(
						fileOS));
				PullBuildXML.writeXML(eat, buf);
				buf.flush();
				buf.close();
				fileOS.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				startActivity(new Intent(FOOD_MANAGE.this, FOOD_MANAGE.class));
				finish();
				Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
				break;
				// Toast.makeText(this, "发生异常写入错误，SD卡可能不存在",
				// Toast.LENGTH_LONG).show();
			}
		}
		case EDIT: {
			// setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目");
	//		int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;// 获取点击了第几行
			Bundle pos=new Bundle();
			pos.putInt("position", selectedPosition);
			Intent intent=new Intent(FOOD_MANAGE.this,FOOD_EDIT.class);
			intent.putExtras(pos);
			startActivity(intent);
			finish();
			break;
		}
		}

		return super.onContextItemSelected(item);
	}
}
