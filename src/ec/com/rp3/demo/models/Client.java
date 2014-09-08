package ec.com.rp3.demo.models;

import android.database.Cursor;
import android.text.TextUtils;
import rp3.db.sqlite.DataBase;
import rp3.util.CursorUtils;
import ec.com.rp3.demo.db.Contract;

public class Client extends rp3.data.entity.EntityBase<Client> {

	private long id;
	private String names;
	
	@Override
	public long getID() {		
		return id;
	}

	@Override
	public void setID(long id) {				
	}

	@Override
	public boolean isAutoGeneratedId() {		
		return true;
	}

	@Override
	public String getTableName() {
		return Contract.Client.TABLE_NAME;
	}

	@Override
	public void setValues() {
	}

	@Override
	public Object getValue(String key) {		
		return null;
	}

	@Override
	public String getDescription() {		
		return names;
	}

	public String getNames(){
		return names;
	}
	
	public void setNames(String names){
		this.names = names;
	}
	
	public static Cursor getQueryClients(DataBase db, String text){
		if(TextUtils.isEmpty(text)){
			return null;
		}
			
		Cursor c = db.query(Contract.Client.TABLE_NAME, new String[] {
				Contract.Client._ID,
				Contract.Client.COLUMN_NAMES}, 
				Contract.Client.COLUMN_NAMES + " LIKE ?", "%" +  text + "%");
		
		return c;		
	}	
	
	public static Client getClient(DataBase db, long id){
		Cursor c = db.query(Contract.Client.TABLE_NAME, Contract.Client.COLUMN_NAMES, 
				Contract.Client._ID + " = ?", String.valueOf(id));
		
		Client client = null;
		if(c.moveToFirst()){
			client = new Client();
			client.setID(id);
			client.setNames(CursorUtils.getString(c,Contract.Client.COLUMN_NAMES));
		}
		return client;
	}
}