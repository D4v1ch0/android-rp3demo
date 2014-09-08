package ec.com.rp3.demo;

import java.util.Date;
import java.util.List;

import rp3.app.BaseActivity;
import rp3.data.MessageCollection;
import rp3.runtime.Session;
import rp3.util.ConnectionUtils;
import rp3.util.DateTime;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;

import com.google.android.gms.location.LocationClient;

import ec.com.rp3.demo.R;
import ec.com.rp3.demo.models.Client;
import ec.com.rp3.demo.models.CodeScan;
import ec.com.rp3.demo.models.Order;
import ec.com.rp3.demo.sync.SendCode;
import ec.com.rp3.demo.sync.SendOrder;
import ec.com.rp3.demo.sync.SyncAdapter;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

public class OrderFragment extends rp3.app.BaseFragment {

	LocationClient locationClient;
	
	private ListView listView;
	List<Order> orders;
	
	public static OrderFragment newInstance(){
		OrderFragment f = new OrderFragment();		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_order);
		
		locationClient = LocationUtils.getLocationClient((BaseActivity)this.getActivity());
	}
	
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		orders = Order.initOrder(getDataBase());				
		
		listView = (ListView)rootView.findViewById(R.id.listView_order);
		listView.setAdapter(new OrderAdapter(this.getActivity(), orders));
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {		
				Order o = orders.get(pos);
				if(o!=null){					
					showDialogFragment(ProductOrderFragment.newInstance(o.getProduct(), o.getQuantity()), "product" );
				}
			}
		});
		
		setButtonClickListener(R.id.button_client_search, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(ClientSearchActivity.newIntent(getActivity()),111);
			}
		});
		
		setButtonClickListener(R.id.button_cancel, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				clean();
			}
		});
		
		setButtonClickListener(R.id.button_send, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(TextUtils.isEmpty(getTextViewString(R.id.button_client_search)))
					showDialogMessage("Elija un Cliente");
				else
					Save();
			}
		});
	}
	
	private void clean(){
		for(Order o: orders){
			o.setQuantity(0);			
		}
		((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
		this.setViewText(R.id.button_client_search, "");
	}
	
	@Override
	public void onFragmentResult(String tagName, int resultCode, Bundle data) {		
		super.onFragmentResult(tagName, resultCode, data);
		
		if(tagName.equals("product")){
			if(resultCode == RESULT_OK){
				for(Order o: orders){
					if(o.getProduct().equals(data.get("PRODUCT"))){
						o.setQuantity(data.getLong("QUANTITY"));
						((OrderAdapter)listView.getAdapter()).notifyDataSetChanged();
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 111){
			if(resultCode == RESULT_OK){
				Button button = (Button)getRootView().findViewById(R.id.button_client_search);
				long id = data.getLongExtra(ClientSearchActivity.RESULT_ARG_ID,0);
				Client c = Client.getClient(getDataBase(), id);
				if(c!=null)
					button.setText(c.getNames());
			}
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
	
	private void Save(){
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
					
			
			for(Order o: orders){
				o.setLatitude(latitude);
				o.setLongitude(longitude);
				o.setOrderDate(orderDate);
				o.setUser(Session.getUser().getLogonName());				
				o.setCode(code);
				o.setClient(client);
				Order.insert(getDataBase(), o);
			}
			
			
			callSync(code);
					
		}				
	}
	
	private void callSync(String code){
		Bundle args = new Bundle();
		args.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_ORDER);
		args.putString(SendOrder.ARG_CODE, code);
		
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
	
}
