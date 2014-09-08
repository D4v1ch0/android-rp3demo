package ec.com.rp3.demo;

import com.google.android.gms.location.LocationClient;

import ec.com.rp3.demo.R;
import ec.com.rp3.demo.models.CodeScan;
import ec.com.rp3.demo.sync.SendCode;
import ec.com.rp3.demo.sync.SyncAdapter;
import android.location.Location;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import rp3.app.BaseActivity;
import rp3.app.BaseFragment;

import rp3.content.SimpleCallback;
import rp3.data.MessageCollection;
import rp3.runtime.Session;
import rp3.util.ConnectionUtils;
import rp3.util.DateTime;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;

public class CodeScanFragment extends BaseFragment {

	LocationClient locationClient;
	String code = "";
	
	public static CodeScanFragment newInstance(){
		CodeScanFragment f = new CodeScanFragment();
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
		
		setContentView(R.layout.fragment_code_scan, R.menu.fragment_scan);
		locationClient = LocationUtils.getLocationClient((BaseActivity)this.getActivity());
		
		
	}
	
	@Override
	public void onResume() {		
		super.onResume();
		if(GooglePlayServicesUtils.servicesConnected((BaseActivity)this.getActivity()))
				locationClient.connect();
	}
	
	@Override
	public void onStop() {		
		super.onStop();
		if(GooglePlayServicesUtils.servicesConnected((BaseActivity)this.getActivity()))
				locationClient.disconnect();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_barcode:
			IntentIntegrator integrator = new IntentIntegrator(this.getActivity());
			integrator.initiateScan();
			return true;
		}		
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onSaveInstanceState(Bundle arg0) {		
		super.onSaveInstanceState(arg0);
		code = getTextViewString(R.id.textView_code);
		arg0.putString("Code", code);
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
				
		if(savedInstanceState!=null)
			code = savedInstanceState.getString("Code");
		
		setTextViewText(R.id.textView_code, code);
		
		setButtonClickListener(R.id.button_action, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(GooglePlayServicesUtils.servicesConnected((BaseActivity)CodeScanFragment.this.getActivity())){
					
					
					update();
					
//					LocationUtils.getAddressAsync(CodeScanFragment.this.getActivity(),location, new SimpleCallback() {
//						
//						@Override
//						public void onExecute(Object... params) {
//							String address = String.valueOf(params[0]);
//							setTextViewText(R.id.textView_location, 
//									address + ", " +
//									"Lat: " + String.valueOf(location.getLatitude()) + ", Lon: " + String.valueOf(location.getLongitude()));
//							
//							update(location, address);
//						}
//					});
				}
			}
		});
	}
	
	public void update(){
		if (!ConnectionUtils.isNetAvailable(this.getActivity())) {						
			showDialogMessage(R.string.message_error_sync_no_net_available);
			return;
		}
		
		final Location location = locationClient.getLastLocation();
		
		showDialogProgress(R.string.message_title_connecting, R.string.message_please_wait);						
		
		
		
		final CodeScan e = new CodeScan();
		e.setCode(getTextViewString(R.id.textView_code));
		e.setLatitude(location.getLatitude());
		e.setLongitude(location.getLongitude());
		e.setScanDate(DateTime.getCurrentDateTime());
		e.setUser(Session.getUser().getLogonName());		
		
		setTextViewText(R.id.textView_code, "");
		setTextViewText(R.id.textView_location, "");
		
		LocationUtils.getAddressAsync(CodeScanFragment.this.getActivity(), location, new SimpleCallback() {
			
			@Override
			public void onExecute(Object... params) {
				e.setAddress( String.valueOf(params[0]));
				
				CodeScan.insert(getDataBase(), e);	
				
				setTextViewText(R.id.textView_location, 
						e.getAddress() + (TextUtils.isEmpty(e.getAddress()) ? "" : ", ") +
						"Lat: " + String.valueOf(location.getLatitude()) + ", Lon: " + String.valueOf(location.getLongitude()));
				
				callSync(e);
			}
		});		
	}
	
	private void callSync(CodeScan e){
		Bundle args = new Bundle();
		args.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_CODE);
		args.putLong(SendCode.ARG_ID, e.getID());
		
		requestSync(args);
	}
	
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		closeDialogProgress();
		if(messages.hasErrorMessage()){
			showDialogMessage(messages);
		}
	}
	
	
//	public class SendCode extends AsyncTask<Object, Integer, String> {
//		
//		CodeScan e;
//		Location location;		
//		
//		@Override
//		protected String doInBackground(Object... params) {							
//			e = (CodeScan)params[0];
//			 
//			location = (Location)params[1];
//			
//			String address = LocationUtils.getAddress(CodeScanFragment.this.getActivity(), location);												
//			e.setAddress(address);							
//			
//			WebService s = new WebService("loteria","setcode");				
//			JSONObject param = new JSONObject();
//			
//			try {
//				param.put("Id", e.getID());					
//				param.put("Code", e.getCode());
//				param.put("DeviceId", Session.getDeviceId());
//				param.put("Latitude", e.getLatitude());
//				param.put("Longitude", e.getLongitude());
//				param.put("User", e.getUser());
//				param.put("Address", e.getAddress());
//				param.put("ScanDate", Convert.getTicksFromDate(e.getScanDate()));
//				
//				
//				s.addParameter("code", param);
//				
//				try {
//					s.invokeWebService();					
//				} catch (HttpResponseException ex) {						
//					Log.e("ws", ex.getMessage());
//				} catch (IOException ex) {						
//					Log.e("ws", ex.getMessage());
//				} catch (XmlPullParserException ex) {						
//					Log.e("ws", ex.getMessage());
//				}
//				
//				return s.getStringResponse();			
//				
//			} catch (JSONException e1) {				
//				Log.e("ws", e1.getMessage());
//				showDialogMessage(e1.getMessage());
//			}catch (Exception e2) {
//				showDialogMessage(e2.getMessage());
//			}
//											
//			return "ERROR";
//		}
//		
//		@Override
//		protected void onPostExecute(String result) {								
//			closeDialogProgress();			
//			
//			setTextViewText(R.id.textView_location, 
//					e.getAddress() + ", " +
//					"Lat: " + String.valueOf(location.getLatitude()) + ", Lon: " + String.valueOf(location.getLongitude()));
//			
//			if(result!=null && !result.equals("\"OK\"")){
//				showDialogMessage(R.string.message_error_sync_connection_server_fail);
//				Log.e("Server",result);
//				//showDialogMessage(result);
//			}
//		}
//		
//	}
	
}
