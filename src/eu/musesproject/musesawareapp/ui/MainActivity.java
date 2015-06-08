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


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import eu.musesproject.client.contextmonitoring.service.aidl.Action;
import eu.musesproject.musesawareapp.R;
import eu.musesproject.musesawareapp.sensorserviceconsumer.MusesServiceConsumer;
import eu.musesproject.musesawareapp.sensorserviceconsumer.ServiceModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements OnItemClickListener {

	private static String TAG = MainActivity.class.getSimpleName();
	// Muses Service
	private ServiceModel serviceModel;
	private MusesServiceConsumer musesService;
	// messages from service consumer
	public static final int ACTION_ACCEPTED = 1;
	public static final int ACTION_DENIED = 0;
	private String currentSelectedFile = "";
	
	public static final String KEY_NAME = "name";
	public static final String KEY_SUBTEXT = "sub_text";
	public static final String KEY_TIME = "time";
	public static final String KEY_IMAGE_URL = "image_url";
	public static Context mContext;
	public String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
		mContext = getApplicationContext();
		ArrayList<HashMap<String, String>> assetList = new ArrayList<HashMap<String, String>>();
		assetList = getAssetsList(AssetUtil.listAssets, AssetUtil.listSubtexts);
		ListView list = (ListView) findViewById(R.id.list);
		// Getting adapter by passing xml data ArrayList
		AssetListAdapter adapter = new AssetListAdapter(this, assetList);
		list.setAdapter(adapter);
		// Click event for single list row
		list.setOnItemClickListener(this);
		// Register for Muses
		regiterForMusesService();
	}

	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		// FIXME change the numbering after brussels demo
		case 0: // Open public Asset
			currentSelectedFile = "/sdcard/aware_app_remote_files/MUSES_beer_competition.txt"; // FIXME Sergio if absolute path can be used here or not
			Map<String, String> openAssetProperties = new HashMap<String, String>();
			openAssetProperties.put("resourceName","statistics");
			openAssetProperties.put("resourceType","PUBLIC");
			openAssetProperties.put("path","/sdcard/aware_app_remote_files/MUSES_beer_competition.txt");
			Action openAssetAction = new Action("open_asset", System.currentTimeMillis(), true);
			sendUserActionsToRemoteMusesService(openAssetAction, openAssetProperties);
			break;
			
		case 1: // Open confidential asset
			currentSelectedFile = "/sdcard/aware_app_remote_files/MUSES_confidential_doc.txt";
			Map<String, String> openConfAssetProperties = new HashMap<String, String>();
			openConfAssetProperties.put("resourceName","statistics");
			openConfAssetProperties.put("resourceType","CONFIDENTIAL");
			openConfAssetProperties.put("path","/sdcard/aware_app_remote_files/MUSES_confidential.txt");
			Action openConfAssetAction = new Action("open_asset", System.currentTimeMillis(), true);
			sendUserActionsToRemoteMusesService(openConfAssetAction, openConfAssetProperties);
			break;
			
		case 2: // Send virus event
			
			Map<String, String> sendVirusProperties = new HashMap<String, String>();
			sendVirusProperties.put("path","/sdcard/aware_app_remote_files/virus.txt");
			sendVirusProperties.put("name","serious_virus");
			sendVirusProperties.put("severity","high");
			Action sendVirusAction = new Action("virus_found", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(sendVirusAction, sendVirusProperties);
			break;
			
		case 3: // Virus cleaned event // FIXME not implemented on Server yet
			Map<String, String> virusCleanedProperties = new HashMap<String, String>();
			virusCleanedProperties.put("path","/sdcard/aware_app_remote_files/virus.txt");
			virusCleanedProperties.put("name","serious_virus");
			virusCleanedProperties.put("severity","high");
			virusCleanedProperties.put("clean_type","quarantine");
			Action virusCleanedAction = new Action("virus_cleaned", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(virusCleanedAction, virusCleanedProperties);
			break;
			
		case 4: // Open internal asset
			currentSelectedFile = "/sdcard/Swe/MUSES_internal_asset.txt";
			Map<String, String> openInternalAssetProperties = new HashMap<String, String>();
			openInternalAssetProperties.put("resourceName","statistics");
			openInternalAssetProperties.put("resourceType","INTERNAL");
			openInternalAssetProperties.put("path","/sdcard/aware_app_remote_files/MUSES_internal_asset.txt");
			Action openInternalAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openInternalAssetAction, openInternalAssetProperties);
			break;
			
		case 5: // Open strictly confidential asset
			currentSelectedFile = "/sdcard/Swe/MUSES_strictly_confidential.txt";
			Map<String, String> openStrictlyConfAssetProperties = new HashMap<String, String>();
			openStrictlyConfAssetProperties.put("resourceName","statistics");
			openStrictlyConfAssetProperties.put("resourceType","STRICTLY_CONFIDENTIAL");
			openStrictlyConfAssetProperties.put("path","/sdcard/aware_app_remote_files/MUSES_strictly_confidential.txt");
			Action openStrictlyConfAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openStrictlyConfAssetAction, openStrictlyConfAssetProperties);
			break;
				
		case 6: // Open asset with sensitivity level
			Map<String, String> openAssetWithSensitivityProperties = new HashMap<String, String>();
			openAssetWithSensitivityProperties.put("resourceName","statistics");
			openAssetWithSensitivityProperties.put("resourceType","CONFIDENTIAL");
			openAssetWithSensitivityProperties.put("path","/sdcard/aware_app_remote_files/MUSES_partner_grades.txt");
			openAssetWithSensitivityProperties.put("sensitivity_level", "internal");	
			Action openAssetWithSensitivityAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openAssetWithSensitivityAction, openAssetWithSensitivityProperties);
			break;
		
		case 7: // Send email event
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
		case 8: // decrypt asset
			Map<String, String> sendDecryptProperties = new HashMap<String, String>();
			sendDecryptProperties.put("action_type","decrypt");
			sendDecryptProperties.put("decryption_status","successfull");
			sendDecryptProperties.put("attempts","0");
			sendDecryptProperties.put("path","/sdcard/Swe/Confidential/MUSES_confidential.txt");
			Action sendDecryptAction = new Action("encrypt_event", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(sendDecryptAction,sendDecryptProperties);
		
		case 9: // send zone info
			Map<String, String> zoneInfoProperties = new HashMap<String, String>();
			zoneInfoProperties.put("type","CONTEXT_SENSOR_LOCATION");
			zoneInfoProperties.put("isWithinZone","zone1");
			Action zoneInfoAction = new Action("CONTEXT_SENSOR_LOCATION", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(zoneInfoAction,zoneInfoProperties);

			
//			 
//		case 8: // Create asset
//			Map<String, String> createAssetProperties = new HashMap<String, String>();
//			createAssetProperties.put("protocol","https");
//			createAssetProperties.put("url","https://...");
//			createAssetProperties.put("resourceId","file1.png");
//			createAssetProperties.put("method","post");
//			Action createAssetAction = new Action("install", System.currentTimeMillis());
//			sendUserActionsToRemoteMusesService(createAssetAction,createAssetProperties);
//			break;
//			
//		case 9: // Copy asset
//			Map<String, String> copyAssetProperties = new HashMap<String, String>();
//			copyAssetProperties.put("protocol","https");
//			copyAssetProperties.put("url","https://...");
//			copyAssetProperties.put("resourceId","file1.png");
//			copyAssetProperties.put("method","post");
//			Action copyAssetAction = new Action("copy_asset", System.currentTimeMillis());
//			sendUserActionsToRemoteMusesService(copyAssetAction,copyAssetProperties);
//			break;
//			 
//		case 10: // Send asset
//			Map<String, String> sendAssetProperties = new HashMap<String, String>();
//			sendAssetProperties.put("protocol","https");
//			sendAssetProperties.put("url","https://...");
//			sendAssetProperties.put("resourceId","file1.png");
//			sendAssetProperties.put("method","post");
//			Action sendAssetAction = new Action("send_asset", System.currentTimeMillis());
//			sendUserActionsToRemoteMusesService(sendAssetAction,sendAssetProperties);
//			break;
//			
//		case 11:
//			Map<String, String> sendDecryptProperties = new HashMap<String, String>();
//			sendDecryptProperties.put("action_type","decrypt");
//			sendDecryptProperties.put("decryption_status","successfull");
//			sendDecryptProperties.put("attempts","0");
//			sendDecryptProperties.put("path","/sdcard/Swe/Confidential/MUSES_Confidential_doc.txt");
//			Action sendDecryptAction = new Action("encrypt_event", System.currentTimeMillis());
//			sendUserActionsToRemoteMusesService(sendDecryptAction,sendDecryptProperties);
//			break;

		default:
			Log.d(TAG, "Unknown operation..");
			break;
		}
	}

	/**
	 * Registers the Muses Service
	 */

	private void regiterForMusesService() {
		musesService = new MusesServiceConsumer(getApplicationContext(),
				callbackHandler);
		serviceModel = ServiceModel.getInstance();
		serviceModel.setService(musesService);
		musesService.startService();
	}

	private void sendUserActionsToRemoteMusesService(Action action,
			Map<String, String> properties) {
		if (serviceModel.getService().musesServiceConsumer != null) {
			try {
				Log.d(TAG, "Sending user's action");
				serviceModel.getService().musesServiceConsumer.sendUserAction(
						action, properties);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler callbackHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ACTION_ACCEPTED:
				Log.d(TAG, "ACTION_ACCEPTED from Muses");
				openFileInView(currentSelectedFile);
				Toast.makeText(getApplicationContext(), "Action Allowed..",Toast.LENGTH_SHORT).show();
				// pDialog.dismiss();
				break;
			case ACTION_DENIED:
				Log.d(TAG, "ACTION_DENIED from Muses");
				Toast.makeText(getApplicationContext(), "Action Denied..",Toast.LENGTH_SHORT).show();
				// pDialog.dismiss();
				break;
			}
		}

	};

	private void openFileInView(String path) {
		File file = new File(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);

	}

	private ArrayList<HashMap<String, String>> getAssetsList(String[] listAssets, String[] subtexts) {
		int publicId = R.drawable.check_green;
		int lockedId = R.drawable.locked;
		int virusId  = R.drawable.virus;
		
		ArrayList<HashMap<String, String>> assetList = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < listAssets.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(KEY_NAME, listAssets[i]);
			map.put(KEY_SUBTEXT, subtexts[i]);
			// FIXME for Brussels
			if (i==0) {
				map.put(KEY_IMAGE_URL, Integer.toString(publicId));
			}
			if (i==1) {
				map.put(KEY_IMAGE_URL, Integer.toString(lockedId));
			}
			if (i==2) {
				map.put(KEY_IMAGE_URL, Integer.toString(virusId));
			}
			if (i==3) {
				map.put(KEY_IMAGE_URL, Integer.toString(publicId));
			}
			if (i==4) {
				map.put(KEY_IMAGE_URL, Integer.toString(publicId));
			}
			if (i==5) {
				map.put(KEY_IMAGE_URL, Integer.toString(lockedId));
			}
			if (i==6) {
				map.put(KEY_IMAGE_URL, Integer.toString(virusId));
			}
			if (i==7) {
				map.put(KEY_IMAGE_URL, Integer.toString(publicId));
			}
			if (i==8) {
				map.put(KEY_IMAGE_URL, Integer.toString(virusId));
			}
			if (i==9) {
				map.put(KEY_IMAGE_URL, Integer.toString(virusId));
			}
			assetList.add(map);
		}

		return assetList;

	}

	public static Context getContextForApp() {
		return mContext;
	}

}
