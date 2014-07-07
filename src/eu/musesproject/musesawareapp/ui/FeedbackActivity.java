package eu.musesproject.musesawareapp.ui;
/*
 * #%L
 * MUSES Client
 * %%
 * Copyright (C) 2013 - 2014 Sweden Connectivity
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import eu.musesproject.musesawareapp.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FeedbackActivity extends Activity implements View.OnClickListener {
	private static final String TAG = FeedbackActivity.class.getSimpleName();
	private RelativeLayout mainLayout;
	private Bundle extras;
	private TextView feedbackView, feedbackTitleView, currentStatusView;
	private Button resolveConflictAutoBtn, okBtn, cancelBtn;
	private String feedback = "";
	private Dialog feedBackDialog;
	private String message;
	private int type;
	
	// CallBack messages
	public static final int LOGIN_SUCCESSFUL = 0;
	public static final int LOGIN_UNSUCCESSFUL = 1;
	public static final int ACTION_RESPONSE_ACCEPTED = 2;
	public static final int ACTION_RESPONSE_DENIED = 3;
	public static final int ACTION_RESPONSE_MAY_BE = 4;
	public static final int ACTION_RESPONSE_UP_TO_USER = 5;
	
	public static final String GRANTED = "granted";
	public static final String DENIED = "denied";
	public static final String MAY_BE = "maybe";
	public static final String UP_TO_USER = "uptouser";
	
	
	public static final String DECISION_OK = "ok";
	public static final String DECISION_CANCEL = "cancel";
	public static final String DECISION_KEY = "decision";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		message = extras.getString("message");
		type = extras.getInt("type");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.resolve_conflict_auto_btn:
			Log.v(TAG, "resolve_conflict_auto_btn..");
			feedBackDialog.dismiss();
			finish();		
			// Not implemented
			break;
		case R.id.details_btn:
			feedBackDialog.dismiss();
			finish();
			break;
		case R.id.cancel_btn:
			feedBackDialog.dismiss();
			finish();
			break;
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// Dialog
		feedBackDialog = new Dialog(this);
		feedBackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		feedBackDialog.setContentView(R.layout.feedback_dialog);
		mainLayout = (RelativeLayout) feedBackDialog.findViewById(R.id.relative_layout_main);
		// Views	
		feedbackView = (TextView)feedBackDialog.findViewById(R.id.feedback_txt);
		feedbackTitleView = (TextView)feedBackDialog.findViewById(R.id.feedback_title_txt);
		currentStatusView = (TextView)feedBackDialog.findViewById(R.id.server_status);
		
		// Buttons
		resolveConflictAutoBtn = (Button)feedBackDialog.findViewById(R.id.resolve_conflict_auto_btn);
		okBtn = (Button)feedBackDialog.findViewById(R.id.details_btn);
		cancelBtn = (Button)feedBackDialog.findViewById(R.id.cancel_btn);
		resolveConflictAutoBtn.setOnClickListener(this);
		
		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		
		switch (type) {
		case ACTION_RESPONSE_DENIED:
			Log.v(TAG, "acion denied in feedback shown..");
			mainLayout.setContentDescription(DENIED);
			feedback = String.format("%s %s %s",getResources().getString(R.string.feedback_txt_1),
					message,
					getResources().getString(R.string.feedback_txt_2)); 
			feedback = message; // temp FIXME
			
			okBtn.setEnabled(false);
			okBtn.setText(getResources().getString(R.string.details_btn_txt));
			feedbackView.setText(feedback);
			feedbackTitleView.setText(getResources().getString(R.string.feedback_title_txt));
			resolveConflictAutoBtn.setVisibility(View.GONE);
			
			// show current status
			currentStatusView.setText("");
			break;
		case ACTION_RESPONSE_MAY_BE:
			Log.v(TAG, "acion maybe in feedback shown..");
			mainLayout.setContentDescription(MAY_BE);
			feedback = String.format("%s %s %s",getResources().getString(R.string.feedback_txt_1),
					message,
					getResources().getString(R.string.feedback_txt_2)); 
			feedback = message; // temp FIXME
			
			feedbackView.setText(feedback);
			feedbackTitleView.setText(getResources().getString(R.string.feedback_title_txt));
			resolveConflictAutoBtn.setVisibility(View.GONE);
			okBtn.setText(getResources().getString(R.string.what_can_i_do_btn_txt));
			// show current status
			currentStatusView.setText("");
			
			break;
		case ACTION_RESPONSE_UP_TO_USER:
			Log.v(TAG, "acion up to user in feedback shown..");
			mainLayout.setContentDescription(UP_TO_USER);
			feedback = String.format("%s %s %s",getResources().getString(R.string.feedback_txt_1),
					message,
					getResources().getString(R.string.feedback_txt_2));
			feedback = message; // temp FIXME
			feedbackView.setText(feedback);
			feedbackTitleView.setText(getResources().getString(R.string.feedback_title_txt));
			okBtn.setText(getResources().getString(R.string.continue_btn_txt));
			
			// show current status
			currentStatusView.setText("");
			
			break;

		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				feedBackDialog.show();
			}
		}, 100);

	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (feedBackDialog != null){
			feedBackDialog.dismiss();
			feedBackDialog = null;
		}
	}
	
}
