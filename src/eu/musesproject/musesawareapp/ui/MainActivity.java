package eu.musesproject.musesawareapp.ui;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
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
	private Button open,openConfAssetButton, run,install,access; 
	private TextView resultView;
	// Muses Service
	private ServiceModel serviceModel;
	private MusesServiceConsumer musesService;
	// messages from service consumer
	public static final int ACTION_ACCEPTED = 1;
	public static final int ACTION_DENIED = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		open = (Button) findViewById(R.id.open_btn);
		openConfAssetButton = (Button) findViewById(R.id.open_conf_btn);
		run = (Button) findViewById(R.id.run_btn);
		install = (Button) findViewById(R.id.install_btn);
		access = (Button) findViewById(R.id.access_btn);
		
		resultView = (TextView) findViewById(R.id.result_text_view);

		open.setOnClickListener(this);
		openConfAssetButton.setOnClickListener(this);
		run.setOnClickListener(this);
		install.setOnClickListener(this);
		access.setOnClickListener(this);
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
			Map<String, String> openAssetProperties = new HashMap<String, String>();
			openAssetProperties.put("resourceName","statistics");
			openAssetProperties.put("resourceType","insensitive");
			openAssetProperties.put("resourcePath","/sdcard/Swe/MUSES_beer_competition.txt");
			Action openAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openAssetAction, openAssetProperties);
			break;
		case R.id.open_conf_btn :
			Map<String, String> openConfAssetProperties = new HashMap<String, String>();
			openConfAssetProperties.put("resourceName","statistics");
			openConfAssetProperties.put("resourceType","sensitive");
			openConfAssetProperties.put("resourcePath","/sdcard/Swe/MUSES_partner_grades.txt");
			Action openConfAssetAction = new Action("open_asset", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(openConfAssetAction, openConfAssetProperties);
			break;
		case R.id.run_btn :
			Map<String, String> runProperties = new HashMap<String, String>();
			runProperties.put("protocol","https");
			runProperties.put("url","https://...");
			runProperties.put("resourceId","file1.png");
			runProperties.put("method","post");
			Action runAction = new Action("run", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(runAction,runProperties);
			break;
		case R.id.install_btn :
			Map<String, String> installProperties = new HashMap<String, String>();
			installProperties.put("protocol","https");
			installProperties.put("url","https://...");
			installProperties.put("resourceId","file1.png");
			installProperties.put("method","post");
			Action installAction = new Action("install", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(installAction,installProperties);
			break;
		case R.id.access_btn :
			Map<String, String> accessProperties = new HashMap<String, String>();
			accessProperties.put("protocol","https");
			accessProperties.put("url","https://...");
			accessProperties.put("resourceId","file1.png");
			accessProperties.put("method","post");
			Action accessAction = new Action("access", System.currentTimeMillis());
			sendUserActionsToRemoteMusesService(accessAction,accessProperties);
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
				break;
			case ACTION_DENIED:
				Log.d(TAG, "ACTION_DENIED from Muses");
				resultView.setText("Action not allowed");
				break;
			}
		}

	};
	
}
