package eu.musesproject.musesawareapp.ui;

import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import eu.musesproject.client.contextmonitoring.service.aidl.Action;
import eu.musesproject.musesawareapp.R;
import eu.musesproject.musesawareapp.sensorserviceconsumer.MusesServiceConsumer;
import eu.musesproject.musesawareapp.sensorserviceconsumer.ServiceModel;

public class MainActivity extends Activity implements View.OnClickListener{

	private static String TAG = MainActivity.class.getSimpleName();
	private Button open, run,install,access; 
	private TextView resultView;
	// Muses Service
	private ServiceModel serviceModel;
	private MusesServiceConsumer musesService;
	private Context context;
	// messages from service consumer
	public static final int ACTION_ACCEPTED = 1;
	public static final int ACTION_DENIED = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		open = (Button) findViewById(R.id.open_btn);
		run = (Button) findViewById(R.id.run_btn);
		install = (Button) findViewById(R.id.install_btn);
		access = (Button) findViewById(R.id.access_btn);

		resultView = (TextView) findViewById(R.id.result_text_view);
		open.setOnClickListener(this);
		run.setOnClickListener(this);
		install.setOnClickListener(this);
		access.setOnClickListener(this);
		context = getApplicationContext();
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
			if (isServiceRunning()) {
				sendUserActionsToRemoteMusesService(new Action("open", System.currentTimeMillis()));
			}
			break;
		case R.id.run_btn :
			sendUserActionsToRemoteMusesService(new Action("run", System.currentTimeMillis()));
			break;
		case R.id.install_btn :
			sendUserActionsToRemoteMusesService(new Action("install", System.currentTimeMillis()));
			break;
		case R.id.access_btn :
			sendUserActionsToRemoteMusesService(new Action("access", System.currentTimeMillis()));
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
	
	private void sendUserActionsToRemoteMusesService(Action action){
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("protocol","https");
		properties.put("url","https://...");
	    properties.put("resourceId","file1.png");
	    properties.put("method","post");
	    	       
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
