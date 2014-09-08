package ec.com.rp3.demo;

import java.util.Date;
import java.util.List;

import com.google.android.gms.location.LocationClient;

import ec.com.rp3.demo.R;
import ec.com.rp3.demo.models.CheckList;
import ec.com.rp3.demo.models.CheckListTemplate;
import ec.com.rp3.demo.models.Client;
import ec.com.rp3.demo.models.Order;
import ec.com.rp3.demo.sync.SendCheckList;
import ec.com.rp3.demo.sync.SendOrder;
import ec.com.rp3.demo.sync.SyncAdapter;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.runtime.Session;
import rp3.util.ConnectionUtils;
import rp3.util.DateTime;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;

public class CheckListFragment extends BaseFragment {
	
	private ListView listView;
	LocationClient locationClient;
	List<CheckListTemplate> data;
	
	public static CheckListFragment newInstance(){
		CheckListFragment f = new CheckListFragment();
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_checklist);
		locationClient = LocationUtils.getLocationClient((BaseActivity)this.getActivity());
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		listView = (ListView)rootView.findViewById(R.id.listView_checklist);
		
		data = CheckListTemplate.getCheckListTemplate(getDataBase());
		listView.setAdapter(new CheckListAdapter(this.getActivity(), data));
		
		setButtonClickListener(R.id.button_client_search, new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				CheckListFragment.this.startActivityForResult(ClientSearchActivity.newIntent(getActivity().getApplicationContext()),111);				
			}
		});
		
		setButtonClickListener(R.id.button_cancel, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		setButtonClickListener(R.id.button_send, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(TextUtils.isEmpty(getTextViewString(R.id.button_client_search)))
					showDialogMessage("Elija un Cliente");
				else
					save();
			}
		});
	}		
	
	private void save(){
		if(GooglePlayServicesUtils.servicesConnected((BaseActivity) this.getActivity())){
			if (!ConnectionUtils.isNetAvailable(this.getActivity())) {						
				showDialogMessage(R.string.message_error_sync_no_net_available);
				return;
			}
			
			final Location location = locationClient.getLastLocation();
			
			showDialogProgress(R.string.message_title_connecting, R.string.message_please_wait);
			
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			Date orderDate = DateTime.getCurrentDateTime();
			String code = String.valueOf(orderDate.getTime());
			String client = getTextViewString(R.id.button_client_search);
					
			
			for(CheckListTemplate t: data){
				CheckList c = new CheckList();
				c.setCheck(t.checked());
				
				c.setLatitude(latitude);
				c.setLongitude(longitude);
				c.setDate(orderDate);
				c.setUser(Session.getUser().getLogonName());				
				c.setCode(code);
				c.setClient(client);
				c.setLabel(t.getLabel());
				
				
				CheckList.insert(getDataBase(), c);
			}
			
			
			callSync(code);
					
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 111){
			if(resultCode == RESULT_OK){
				Button button = (Button)getRootView().findViewById(R.id.button_client_search);
				long id = Long.valueOf(data.getLongExtra(ClientSearchActivity.RESULT_ARG_ID,0));
				Client c = Client.getClient(getDataBase(), id);
				if(c!=null)
					button.setText(c.getNames());
			}
		}
		
	}
	
	private void callSync(String code){
		Bundle args = new Bundle();
		args.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_CHECKLIST);
		args.putString(SendCheckList.ARG_CODE, code);
		
		requestSync(args);
	}
	
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		closeDialogProgress();
		clean();
		if(messages.hasErrorMessage()){
			showDialogMessage(messages);
		}
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
	
	private void clean(){
		for(CheckListTemplate o: data){
			o.setChecked(false);			
		}
		((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
		this.setViewText(R.id.button_client_search, "");
	}
	
//	@Override
//	public void onActivityCreated(Bundle arg0) {		
//		super.onActivityCreated(arg0);
//		
//		executeLoader(0, null, this);
//	}		
	
	
	
}
