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
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import eu.musesproject.client.contextmonitoring.service.aidl.Action;
import eu.musesproject.musesawareapp.R;
import eu.musesproject.musesawareapp.sensorserviceconsumer.MusesServiceConsumer;
import eu.musesproject.musesawareapp.sensorserviceconsumer.ServiceModel;

public class MainActivity extends Activity implements View.OnClickListener{

	private static String TAG = MainActivity.class.getSimpleName();
	private Button openPublicAsset,openInternalAsset,openConfAsset,openStrictlyConfAsset,openAssetWithSensitivity,sendVirus,sendEmail,sendEmailWithAttachment,install; 
	private TextView resultView;
	// Muses Service
	private ServiceModel serviceModel;
	private MusesServiceConsumer musesService;
	// messages from service consumer
	public static final int ACTION_ACCEPTED = 1;
	public static final int ACTION_DENIED = 0;
	private String currentSelectedFile = "";
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		openPublicAsset = (Button) findViewById(R.id.open_btn);
		openInternalAsset = (Button) findViewById(R.id.open_internal_asset_btn);
		openConfAsset = (Button) findViewById(R.id.open_conf_btn);
		openStrictlyConfAsset = (Button) findViewById(R.id.open_strictly_conf_btn);
		openAssetWithSensitivity = (Button) findViewById(R.id.open_asset_with_sensitivity_btn);
		sendVirus = (Button) findViewById(R.id.send_virus_event_btn);
		sendEmail = (Button) findViewById(R.id.send_email_event_btn);
		sendEmailWithAttachment = (Button) findViewById(R.id.send_email_with_attachment_event_btn);
		install = (Button) findViewById(R.id.install_btn);
		resultView = (TextView) findViewById(R.id.result_text_view);
		
		openPublicAsset.setOnClickListener(this);
		openInternalAsset.setOnClickListener(this);
		openConfAsset.setOnClickListener(this);
		openStrictlyConfAsset.setOnClickListener(this);
		openAssetWithSensitivity.setOnClickListener(this);
		sendVirus.setOnClickListener(this);
		sendEmail.setOnClickListener(this);
		sendEmailWithAttachment.setOnClickListener(this);
		install.setOnClickListener(this);
		
		regiterForMusesService();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.open_btn :
			currentSelectedFile = "/sdcard/Swe/MUSES_beer_competition.txt";
			Map<String, String> openAssetProperties = new HashMap<String, String>();
			openAssetProperties.put("resourceName","statistics");
			openAssetProperties.put("resourceType","insensitive");
			openAssetProperties.put("resourcePath","/sdcard/Swe/MUSES_beer_competition.txt");
			Action openAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openAssetAction, openAssetProperties);
			break;
		case R.id.open_internal_asset_btn :
			currentSelectedFile = "/sdcard/Swe/MUSES_internal_asset.txt";
			Map<String, String> openInternalAssetProperties = new HashMap<String, String>();
			openInternalAssetProperties.put("resourceName","statistics");
			openInternalAssetProperties.put("resourceType","insensitive");
			openInternalAssetProperties.put("resourcePath","/sdcard/Swe/MUSES_internal_asset.txt");
			Action openInternalAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openInternalAssetAction, openInternalAssetProperties);
			openFileInView(currentSelectedFile);
			break;
		case R.id.open_conf_btn :
			currentSelectedFile = "/sdcard/Swe/MUSES_partner_grades.txt";
			Map<String, String> openConfAssetProperties = new HashMap<String, String>();
			openConfAssetProperties.put("resourceName","statistics");
			openConfAssetProperties.put("resourceType","sensitive");
			openConfAssetProperties.put("resourcePath","/sdcard/Swe/MUSES_partner_grades.txt");
			Action openConfAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openConfAssetAction, openConfAssetProperties);
			break;
		case R.id.open_strictly_conf_btn :
			currentSelectedFile = "/sdcard/Swe/MUSES_strictly_confidential.txt";
			Map<String, String> openStrictlyConfAssetProperties = new HashMap<String, String>();
			openStrictlyConfAssetProperties.put("resourceName","statistics");
			openStrictlyConfAssetProperties.put("resourceType","sensitive");
			openStrictlyConfAssetProperties.put("resourcePath","/sdcard/Swe/MUSES_strictly_confidential.txt");
			Action openStrictlyConfAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openStrictlyConfAssetAction, openStrictlyConfAssetProperties);
			showResultDialog("You are not allowed to open this asset", FeedbackActivity.ACTION_RESPONSE_DENIED);
			break;
		case R.id.open_asset_with_sensitivity_btn :
			Map<String, String> openAssetWithSensitivityProperties = new HashMap<String, String>();
			openAssetWithSensitivityProperties.put("resourceName","statistics");
			openAssetWithSensitivityProperties.put("resourceType","sensitive");
			openAssetWithSensitivityProperties.put("resourcePath","/sdcard/Swe/MUSES_partner_grades.txt");
			openAssetWithSensitivityProperties.put("sensitivity_level", "internal");	
			Action openAssetWithSensitivityAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openAssetWithSensitivityAction, openAssetWithSensitivityProperties);
			showResultDialog("Not allowed", FeedbackActivity.ACTION_RESPONSE_DENIED);
			break;
		case R.id.send_virus_event_btn :
			Map<String, String> sendVirusProperties = new HashMap<String, String>();
			sendVirusProperties.put("path","/sdcard/...");
			sendVirusProperties.put("name","blabla");
			sendVirusProperties.put("severity","high");
			Action sendVirusAction = new Action("virus_found", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(sendVirusAction, sendVirusProperties);
			showResultDialog("Your device is infected .. please contact admin..", FeedbackActivity.ACTION_RESPONSE_DENIED);
			break;
		case R.id.send_email_event_btn :
			Map<String, String> sendEmailProperties = new HashMap<String, String>();
			sendEmailProperties.put("resourceName","statistics");
			sendEmailProperties.put("resourceType","sensitive");
			sendEmailProperties.put("resourcePath","/sdcard/Swe/MUSES_partner_grades.txt");
			Action sendEmailAction = new Action("send_mail", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(sendEmailAction, sendEmailProperties);
			showResultDialog("You are not allowed to send email with this email client", FeedbackActivity.ACTION_RESPONSE_DENIED);
			break;
		case R.id.send_email_with_attachment_event_btn :
			Map<String, String> sendEmailAttachmentProperties = new HashMap<String, String>();
			sendEmailAttachmentProperties.put("resourceName","statistics");
			sendEmailAttachmentProperties.put("resourceType","sensitive");
			sendEmailAttachmentProperties.put("resourcePath","/sdcard/Swe/MUSES_partner_grades.txt");
			Action sendEmailAttachmentAction = new Action("file_attached", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(sendEmailAttachmentAction, sendEmailAttachmentProperties);
			showResultDialog("You are not allowed to attach this file", FeedbackActivity.ACTION_RESPONSE_DENIED);
			break;
		case R.id.install_btn :
			Map<String, String> installProperties = new HashMap<String, String>();
			installProperties.put("protocol","https");
			installProperties.put("url","https://...");
			installProperties.put("resourceId","file1.png");
			installProperties.put("method","post");
			Action installAction = new Action("install", System.currentTimeMillis());
			showResultDialog("You are not allowed to install this application", FeedbackActivity.ACTION_RESPONSE_DENIED);
			//sendUserActionsToRemoteMusesService(installAction,installProperties);
			break;
		}
	}
	
	// FIXME this method does not work properly 
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("eu.musesproject.client.testServiceProvider.IMusesService".equals(service.service.getClassName())) {
			   return true;
			}
		}
		return true;
	}
	
	/**
	 * Registers the Muses Service 
	 */

	private void regiterForMusesService() {
		musesService = new MusesServiceConsumer(getApplicationContext(), callbackHandler);
		serviceModel = ServiceModel.getInstance();
		serviceModel.setService(musesService);
		musesService.startService();
	}
	
	private void sendUserActionsToRemoteMusesService(Action action, Map<String, String> properties){
		if (serviceModel.getService().musesServiceConsumer != null) {
			try {
				Log.d(TAG, "Sending user's action");
				serviceModel.getService().musesServiceConsumer.sendUserAction(
						action, 
						properties);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}

	@SuppressLint("HandlerLeak")
	private Handler callbackHandler = new Handler() {


		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case ACTION_ACCEPTED:
				Log.d(TAG, "ACTION_ACCEPTED from Muses");
				resultView.setText("Action allowed");	
				openFileInView(currentSelectedFile);
				//pDialog.dismiss();
				break;
			case ACTION_DENIED:
				Log.d(TAG, "ACTION_DENIED from Muses");
				resultView.setText("Action not allowed");
				//pDialog.dismiss();
				break;
			}
		}

	};
	
	private void openFileInView(String path){
		File file = new File(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "text/plain");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		
	}
	
	// For testing delete later FIXME
	/**
	 * Shows the result dialog to the user
	 * 
	 * @param message
	 */


	private void showResultDialog(String message, int type) {
		Intent showFeedbackIntent = new Intent(
				getApplicationContext(), FeedbackActivity.class);
		showFeedbackIntent.putExtra("message", message);
		showFeedbackIntent.putExtra("type", type);
		Bundle extras = new Bundle();
		extras.putString(FeedbackActivity.DECISION_KEY, FeedbackActivity.DECISION_OK);
		showFeedbackIntent.putExtras(extras);
		startActivity(showFeedbackIntent);
	}
}
