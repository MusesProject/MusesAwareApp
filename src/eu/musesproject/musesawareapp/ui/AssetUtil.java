package eu.musesproject.musesawareapp.ui;

import eu.musesproject.musesawareapp.R;

public class AssetUtil {
	public static String [] listAssets = { 
									MainActivity.getContextForApp().getResources().getString(R.string.open),
									MainActivity.getContextForApp().getResources().getString(R.string.open_confidential),
									MainActivity.getContextForApp().getResources().getString(R.string.send_virus_event),
									MainActivity.getContextForApp().getResources().getString(R.string.virus_cleaned_even_btn),
									MainActivity.getContextForApp().getResources().getString(R.string.open_internal),
									MainActivity.getContextForApp().getResources().getString(R.string.open_strictly_confidential),
									MainActivity.getContextForApp().getResources().getString(R.string.open_asset_with_sensitivity),
									MainActivity.getContextForApp().getResources().getString(R.string.send_email_event),
									MainActivity.getContextForApp().getResources().getString(R.string.decrypt_asset),
									MainActivity.getContextForApp().getResources().getString(R.string.zone_info),
//									ShowActivity.getContextForApp().getResources().getString(R.string.decrypt_asset),
//									ShowActivity.getContextForApp().getResources().getString(R.string.create_asset),
//									ShowActivity.getContextForApp().getResources().getString(R.string.copy_asset),
//									ShowActivity.getContextForApp().getResources().getString(R.string.send_asset)
										};
	
	public static String [] listSubtexts = {  	
									   "This is public asset on a remote site of the company.",
									   "This is confidential asset location on a remote location",
									   "Infect the device by sending a virus",
									   "send a fake virus cleaned (qurantine) event from antivirus app",
									   "internal asset located on a device",
									   "strictly confidential asset on a remote location",
									   "Test opening an asset by providing sensitivity",
									   "send fake email with attachments",
									   "decrypt an encrypted asset",
									   "Send zone info to muses"
//									   "test trying to decrypt an asset which is protected by encryption app.",
//									   "not implemented yet",
//									   "not implemented yet",
//									   "not implemented yet",
		  							};
}
