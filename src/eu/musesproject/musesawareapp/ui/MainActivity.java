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
//import eu.musesproject.musesawareapp.folder.FileChooser;
import eu.musesproject.musesawareapp.sensorserviceconsumer.MusesServiceConsumer;
import eu.musesproject.musesawareapp.sensorserviceconsumer.ServiceModel;

public class MainActivity extends Activity implements View.OnClickListener{

	private static String TAG = MainActivity.class.getSimpleName();
	private Button openPublicAsset,openInternalAsset,openConfAsset,openStrictlyConfAsset,openAssetWithSensitivity,
				   sendVirus,sendEmail,virusCleaned,
				   createAsset,copyAsset,sendAsset; 
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
		virusCleaned = (Button) findViewById(R.id.virus_cleaned_event_btn);
		createAsset = (Button) findViewById(R.id.create_asset_btn);
		copyAsset = (Button) findViewById(R.id.copy_asset_btn);
		sendAsset = (Button) findViewById(R.id.send_asset_btn);
		resultView = (TextView) findViewById(R.id.result_text_view);
		
		openPublicAsset.setOnClickListener(this);
		openInternalAsset.setOnClickListener(this);
		openConfAsset.setOnClickListener(this);
		openStrictlyConfAsset.setOnClickListener(this);
		openAssetWithSensitivity.setOnClickListener(this);
		sendVirus.setOnClickListener(this);
		sendEmail.setOnClickListener(this);
		virusCleaned.setOnClickListener(this);
		createAsset.setOnClickListener(this);
		copyAsset.setOnClickListener(this);
		sendAsset.setOnClickListener(this);
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
			openAssetProperties.put("path","/sdcard/aware_app_remote_files/MUSES_beer_competition.txt");
			Action openAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openAssetAction, openAssetProperties);
//			openFileInView(currentSelectedFile);
			break;
		
		case R.id.open_internal_asset_btn :
			currentSelectedFile = "/sdcard/Swe/MUSES_internal_asset.txt";
			Map<String, String> openInternalAssetProperties = new HashMap<String, String>();
			openInternalAssetProperties.put("resourceName","statistics");
			openInternalAssetProperties.put("resourceType","insensitive");
			openInternalAssetProperties.put("path","/sdcard/aware_app_remote_files/MUSES_internal_asset.txt");
			Action openInternalAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openInternalAssetAction, openInternalAssetProperties);
//			openFileInView(currentSelectedFile);
			break;
		
		case R.id.open_conf_btn :
			currentSelectedFile = "/sdcard/Swe/MUSES_partner_grades.txt";
			Map<String, String> openConfAssetProperties = new HashMap<String, String>();
			openConfAssetProperties.put("resourceName","statistics");
			openConfAssetProperties.put("resourceType","sensitive");
			openConfAssetProperties.put("path","/sdcard/aware_app_remote_files/MUSES_confidential_doc.txt");
			Action openConfAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openConfAssetAction, openConfAssetProperties);
//			openFileInView(currentSelectedFile);
			break;
		
		case R.id.open_strictly_conf_btn :
			currentSelectedFile = "/sdcard/Swe/MUSES_strictly_confidential.txt";
			Map<String, String> openStrictlyConfAssetProperties = new HashMap<String, String>();
			openStrictlyConfAssetProperties.put("resourceName","statistics");
			openStrictlyConfAssetProperties.put("resourceType","sensitive");
			openStrictlyConfAssetProperties.put("path","/sdcard/aware_app_remote_files/MUSES_strictly_confidential.txt");
			Action openStrictlyConfAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openStrictlyConfAssetAction, openStrictlyConfAssetProperties);
//			showResultDialog("You are not allowed to open this asset", FeedbackActivity.ACTION_RESPONSE_DENIED);
			break;
		
		case R.id.open_asset_with_sensitivity_btn :
			Map<String, String> openAssetWithSensitivityProperties = new HashMap<String, String>();
			openAssetWithSensitivityProperties.put("resourceName","statistics");
			openAssetWithSensitivityProperties.put("resourceType","sensitive");
			openAssetWithSensitivityProperties.put("path","/sdcard/aware_app_remote_files/MUSES_partner_grades.txt");
			openAssetWithSensitivityProperties.put("sensitivity_level", "internal");	
			Action openAssetWithSensitivityAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openAssetWithSensitivityAction, openAssetWithSensitivityProperties);
//			showResultDialog("Not allowed", FeedbackActivity.ACTION_RESPONSE_DENIED);
			break;
		
		case R.id.send_virus_event_btn :
			Map<String, String> sendVirusProperties = new HashMap<String, String>();
			sendVirusProperties.put("path","/sdcard/aware_app_remote_files/virus.txt");
			sendVirusProperties.put("name","serious_virus");
			sendVirusProperties.put("severity","high");
			Action sendVirusAction = new Action("virus_found", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(sendVirusAction, sendVirusProperties);
			break;
		
		case R.id.send_email_event_btn :
			Map<String, String> sendEmailProperties = new HashMap<String, String>();
			sendEmailProperties.put("from", "max.mustermann@generic.com");
			sendEmailProperties.put("to", "the.reiceiver@generic.com, another.direct.receiver@generic.com");
			sendEmailProperties.put("cc", "other.listener@generic.com, 2other.listener@generic.com");
			sendEmailProperties.put("bcc", "hidden.reiceiver@generic.com");
			sendEmailProperties.put("subject", "MUSES sensor status subject");
			sendEmailProperties.put("path", "/sdcard/someattachment.pdf");
			sendEmailProperties.put("noAttachments", "1");
			sendEmailProperties.put("attachmentInfo", "");
			Action sendEmailAction = new Action("ACTION_SEND_MAIL", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(sendEmailAction, sendEmailProperties);
			break;

		case R.id.virus_cleaned_event_btn :
//			Map<String, String> virusCleanedProperties = new HashMap<String, String>();
//			virusCleanedProperties.put("path","/sdcard/aware_app_remote_files/virus.txt");
//			virusCleanedProperties.put("name","serious_virus");
//			virusCleanedProperties.put("severity","high");
//			virusCleanedProperties.put("clean_type","quarantine");
//			Action virusCleanedAction = new Action("virus_cleaned", System.currentTimeMillis());
//			sendUserActionsToRemoteMusesService(virusCleanedAction, virusCleanedProperties);
			//Intent intent1 = new Intent(this, FileChooser.class);
			//startActivityForResult(intent1,1);
			
			break;
			
		case R.id.create_asset_btn :
			Map<String, String> createAssetProperties = new HashMap<String, String>();
			createAssetProperties.put("protocol","https");
			createAssetProperties.put("url","https://...");
			createAssetProperties.put("resourceId","file1.png");
			createAssetProperties.put("method","post");
			Action createAssetAction = new Action("install", System.currentTimeMillis());
			//sendUserActionsToRemoteMusesService(createAssetAction,createAssetProperties);
			break;
			
		case R.id.copy_asset_btn :
			Map<String, String> copyAssetProperties = new HashMap<String, String>();
			copyAssetProperties.put("protocol","https");
			copyAssetProperties.put("url","https://...");
			copyAssetProperties.put("resourceId","file1.png");
			copyAssetProperties.put("method","post");
			Action copyAssetAction = new Action("copy_asset", System.currentTimeMillis());
			//sendUserActionsToRemoteMusesService(copyAssetAction,copyAssetProperties);
			break;

		case R.id.send_asset_btn :
			Map<String, String> sendAssetProperties = new HashMap<String, String>();
			sendAssetProperties.put("protocol","https");
			sendAssetProperties.put("url","https://...");
			sendAssetProperties.put("resourceId","file1.png");
			sendAssetProperties.put("method","post");
			Action sendAssetAction = new Action("install", System.currentTimeMillis());
			//sendUserActionsToRemoteMusesService(sendAssetAction,sendAssetProperties);
			break;
			
		case R.id.decrypt_asset_btn :
			Map<String, String> sendDecryptProperties = new HashMap<String, String>();
			sendDecryptProperties.put("action_type","decrypt");
			sendDecryptProperties.put("decryption_status","successfull");
			sendDecryptProperties.put("attempts","0");
			sendDecryptProperties.put("path","/sdcard/Swe/Confidential/MUSES_Confidential_doc.txt");
			Action sendDecryptAction = new Action("encrypt_event", System.currentTimeMillis());
			//sendUserActionsToRemoteMusesService(sendAssetAction,sendAssetProperties);
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
	
	
    String curFileName;
 // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
    	if (requestCode == 1){
    		if (resultCode == RESULT_OK) { 
    			curFileName = data.getStringExtra("GetFileName"); 
            	Log.d(TAG, "Current File Name:_" + curFileName);
    			//edittext.setText(curFileName);
    		}
    	 }
    }
	
	
	
	
}
