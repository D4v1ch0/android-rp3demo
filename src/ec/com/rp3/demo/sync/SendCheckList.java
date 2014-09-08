package ec.com.rp3.demo.sync;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import ec.com.rp3.demo.models.CheckList;

import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;

public class SendCheckList {
	
	public static final String ARG_CODE = "code";
	
	public static int executeSync(DataBase db, String code) {
		//android.os.Debug.waitForDebugger();
		List<CheckList> list = CheckList.getCheckList(db, code);  		 																											
		
		WebService service = new WebService("loteria","setchecklist");				
		JSONArray arrayOrders = new JSONArray();
		
		for(CheckList reg : list)
		{				
			JSONObject param = new JSONObject();		
			try {
				
				param.put("Code", reg.getCode());			
				param.put("Latitude", reg.getLatitude());
				param.put("Longitude", reg.getLongitude());
				param.put("User", reg.getUser());
				param.put("Address", reg.getAddress());
				param.put("Date", Convert.getTicksFromDate(reg.getDate()));
				param.put("Client", reg.getClient());
				param.put("Label", reg.getLabel());
				param.put("CheckValue", reg.isCheck());
				
				arrayOrders.put(param);
				
			} catch (JSONException e) {
				return SyncAdapter.SYNC_EVENT_ERROR;
			}		
		}
		service.addParameter("checklists", arrayOrders);		
		
		try {			
			service.invokeWebService();
		} catch (HttpResponseException e) {
			return SyncAdapter.SYNC_EVENT_CONNECTION_FAILED;
		} catch (IOException e) {
			return SyncAdapter.SYNC_EVENT_CONNECTION_FAILED;
		} catch (XmlPullParserException e) {
			return SyncAdapter.SYNC_EVENT_ERROR;
		}
		
		return SyncAdapter.SYNC_EVENT_SUCCESS;
	}
}
