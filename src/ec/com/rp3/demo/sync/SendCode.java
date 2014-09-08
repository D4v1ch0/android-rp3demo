package ec.com.rp3.demo.sync;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import ec.com.rp3.demo.models.CodeScan;

import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import rp3.util.Convert;

public class SendCode {

	public static final String ARG_ID = "id";
	
	public static int executeSync(DataBase db, long id) {
		//android.os.Debug.waitForDebugger();
		CodeScan reg = CodeScan.getCodeScan(db, id);  		 																											
		
		WebService service = new WebService("loteria","setcode");				
		JSONObject param = new JSONObject();
		
		try {
			param.put("Code", reg.getCode());
			param.put("DeviceId", Session.getDeviceId());
			param.put("Latitude", reg.getLatitude());
			param.put("Longitude", reg.getLongitude());
			param.put("User", reg.getUser());
			param.put("Address", reg.getAddress());
			param.put("ScanDate", Convert.getTicksFromDate(reg.getScanDate()));
		} catch (JSONException e) {
			return SyncAdapter.SYNC_EVENT_ERROR;
		}							
				
		service.addParameter("code", param);		
		
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
