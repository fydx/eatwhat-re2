/**
 * $id$
 */
package com.sssta.eatwhat_re2;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.status.StatusSetRequestParam;
import com.renren.api.connect.android.status.StatusSetResponseBean;

/**
 * The activity that demos how to use 'publishStatus' API
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class StatusPublishActivity extends Activity {
	// the publish-status button
	private Button publishButton;

	// the edittext control for the user to input status content
	private EditText status;

	// the edittext control to show the response
	private EditText response;

	private Renren renren;
	private String toRenren;
	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.renren_status_publish);
		//RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this)
		//		.inflate(R.layout.renren_status_publish, null);
		//root.addView(layout);

	//	titlebarText.setText(getString(R.string.status_pub_title));

		// cancel
	//	titlebarLeftButton.setOnClickListener(new View.OnClickListener() {

	//		@Override
	//		public void onClick(View v) {
	//			finish();
	//		}
	//	});

		status = (EditText)findViewById(R.id.renren_status_pub_text);
		response = (EditText) 
				findViewById(R.id.renren_status_pub_result_text);
		publishButton = (Button)
				findViewById(R.id.renren_status_publish);
		publishButton.setEnabled(false);
		
		// enable the button when there is content in the status edittext
		status.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (status.getText().toString().trim().toString().length() > 0) {
					publishButton.setEnabled(true);
				} else {
					publishButton.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		renren = getIntent().getParcelableExtra(Renren.RENREN_LABEL);
		toRenren=getIntent().getStringExtra("toRenren");
		status.setText(toRenren);
		// publish
		publishButton = (Button) 
				findViewById(R.id.renren_status_publish);
		publishButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			

				try {
					publishButton.setEnabled(false);
					progress = ProgressDialog.show(StatusPublishActivity.this,
							"��ʾ", "���ڷ��������Ժ�");
					response.setText("");
					StatusSetRequestParam param = new StatusSetRequestParam(status
							.getText().toString().trim());

					StatusSetListener listener = new StatusSetListener(
							StatusPublishActivity.this);
					AsyncRenren aRenren = new AsyncRenren(renren);
					aRenren.publishStatus(param, listener, true);
				} catch (Throwable e) {
					Log.e("msg","error");
					String errorMsg = e.getMessage();
					response.setText(errorMsg);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progress != null) {
			progress.dismiss();
		}
	}

	/**
	 * 监听异步调用发�?状�?接口的响�?	 * 
	 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
	 */
	private class StatusSetListener extends
			AbstractRequestListener<StatusSetResponseBean> {

		private Context context;

		private Handler handler;

		public StatusSetListener(Context context) {
			this.context = context;
			this.handler = new Handler(context.getMainLooper());
		}

		@Override
		public void onRenrenError(RenrenError renrenError) {
			final int errorCode = renrenError.getErrorCode();
			final String errorMsg = renrenError.getMessage();
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (StatusPublishActivity.this != null) {
						publishButton.setEnabled(true);
						response.setText(errorMsg);
						if (progress != null) {
							progress.dismiss();
						}
					}
					if (errorCode == RenrenError.ERROR_CODE_OPERATION_CANCELLED) {
						Toast.makeText(context, "���ͱ�ȡ��", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(context, "����ʧ��", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
		}

		@Override
		public void onFault(Throwable fault) {
			final String errorMsg = fault.toString();
			handler.post(new Runnable() {

				@Override
				public void run() {
					if (StatusPublishActivity.this != null) {
						publishButton.setEnabled(true);
						response.setText(errorMsg);
						if (progress != null) {
							progress.dismiss();
						}
					}
					Toast.makeText(context, "����ʧ��", Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		public void onComplete(StatusSetResponseBean bean) {
			final String responseStr = bean.toString();
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (StatusPublishActivity.this != null) {
						response.setText(responseStr);
						publishButton.setEnabled(true);
						if (progress != null) {
							progress.dismiss();
						}
					}
					Toast.makeText(context, "���ͳɹ�", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
