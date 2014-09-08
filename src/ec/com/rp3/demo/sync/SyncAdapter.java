package ec.com.rp3.demo.sync;

import ec.com.rp3.demo.db.DbOpenHelper;
import rp3.db.sqlite.DataBase;
import rp3.sync.SyncAudit;
import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

public class SyncAdapter extends rp3.content.SyncAdapter {
	
	public static String SYNC_TYPE_SEND_CODE = "SENDCODE";
	public static String SYNC_TYPE_SEND_ORDER = "SENDORDER";
	public static String SYNC_TYPE_SEND_CHECKLIST = "SENDCHECKLIST";
	
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
	}
	
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {		
		super.onPerformSync(account, extras, authority, provider, syncResult);
		
		String syncType = extras.getString(ARG_SYNC_TYPE);
		
		DataBase db = null;		
		int result = 0;
		
		try{
			db = DataBase.newDataBase(DbOpenHelper.class);
			
			if(syncType == null || syncType.equals(SYNC_TYPE_SEND_CODE)){
				//db.beginTransaction();
				long id = extras.getLong(SendCode.ARG_ID);
				result = SendCode.executeSync(db, id);
				addDefaultMessage(result);				
				
				//db.commitTransaction();
			}else if(syncType == null || syncType.equals(SYNC_TYPE_SEND_ORDER)){
				String code = extras.getString(SendOrder.ARG_CODE);
				result = SendOrder.executeSync(db, code);
				addDefaultMessage(result);										
			}else if(syncType == null || syncType.equals(SYNC_TYPE_SEND_CHECKLIST)){
				String code = extras.getString(SendCheckList.ARG_CODE);
				result = SendCheckList.executeSync(db, code);
				addDefaultMessage(result);								
			}
		}catch(Exception ex){			
			Log.e(TAG, "E: " + ex.getMessage());
			addDefaultMessage(SYNC_EVENT_ERROR);
			SyncAudit.insert(syncType, SYNC_EVENT_ERROR);
		} 
		finally{	
			if(db!=null){
				db.endTransaction();
				db.close();
			}
			
			notifySyncFinish();
		}								
	}
}
