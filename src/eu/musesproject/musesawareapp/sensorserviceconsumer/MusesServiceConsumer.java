/*
 * MUSES High-Level Object Oriented Model
 * Copyright MUSES project (European Commission FP7) - 2013 
 */

package eu.musesproject.musesawareapp.sensorserviceconsumer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import eu.musesproject.client.contextmonitoring.service.aidl.IMusesService;
import eu.musesproject.client.contextmonitoring.service.aidl.IMusesServiceCallback;
import eu.musesproject.musesawareapp.ui.MainActivity;

public class MusesServiceConsumer {

	private static final String AIDL_MUSES_SERVICE_CLASS = ".IMusesServiceImpl";
	private static final String AIDL_MUSES_SERVICE_PACKAGE = "eu.musesproject.client.testServiceProvider";
	private static final String AIDL_MUSES_SERVICE_NAME = "eu.musesproject.client.testServiceProvider.IMusesService";

	private Context context;
	public IMusesService musesServiceConsumer;
	public static boolean started = false;
	protected static final String TAG = MusesServiceConsumer.class.getSimpleName();
	private Handler mHandler;

	
	public MusesServiceConsumer() {
		
	}
	
	public IMusesService getInstance(Context context){
		if (musesServiceConsumer != null)
			return musesServiceConsumer;
		return null; // Need to be fixed
	}

	public MusesServiceConsumer(Context context, Handler handler) {
		this.context = context;
		mHandler = handler;
	}
	
	public void startService() {
		Log.v(TAG, "Binding Service");
		context.bindService(new Intent(IMusesService.class.getName()), 
				mServiceConn, 
				Context.BIND_AUTO_CREATE);
		
//		if(musesServiceConsumer == null) {
//			Intent bindIntent = new Intent(AIDL_MUSES_SERVICE_NAME);
//			bindIntent.setClassName(AIDL_MUSES_SERVICE_PACKAGE, AIDL_MUSES_SERVICE_PACKAGE + AIDL_MUSES_SERVICE_CLASS);
//			context.bindService(bindIntent, mServiceConn, Context.BIND_AUTO_CREATE);
//			started = true;
//			Log.d(TAG, "The Service will be connected soon!");
//		}
	}
	
	public void stopService(){
		context.unbindService(mServiceConn);
		started = false;
	}
	
	
	private ServiceConnection mServiceConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.e(TAG, "SERVICE CONNECTED");
			musesServiceConsumer = IMusesService.Stub.asInterface(service);
			try {
				musesServiceConsumer.registerCallback(musesServiceCallback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.e(TAG, "SERVICE DISCONNECTED");
			try {
				musesServiceConsumer.unregisterCallback(musesServiceCallback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}			
		}
		
	};
	
	
	
	/**
	 * CallBack implementations for MusDEMService
	 */
	
    private IMusesServiceCallback musesServiceCallback = new IMusesServiceCallback.Stub() {

		@Override
		public void onAccept(String response) throws RemoteException {
			Log.d(TAG, "Accept in aware app");
	    	Message msg = mHandler.obtainMessage(MainActivity.ACTION_ACCEPTED);
			Bundle bundle = new Bundle();
			bundle.putString("message",response);
			msg.setData(bundle);
			mHandler.sendMessage(msg);

		}

		@Override
		public void onDeny(String response) throws RemoteException {
			Log.e(TAG, "Deny in aware app");
	    	Message msg = mHandler.obtainMessage(MainActivity.ACTION_DENIED);
			Bundle bundle = new Bundle();
			bundle.putString("message", response);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}

    };
	
}
