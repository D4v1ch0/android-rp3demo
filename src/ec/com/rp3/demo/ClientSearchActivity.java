package ec.com.rp3.demo;

import ec.com.rp3.demo.R;
import ec.com.rp3.demo.db.Contract;
import ec.com.rp3.demo.models.Client;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class ClientSearchActivity extends rp3.app.SearchActivity {

	public static Intent newIntent(Context c){
		Intent i = new Intent(c, ClientSearchActivity.class);
		return i;
	}
	
	private SimpleCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setQueryHint(getText(R.string.hint_search_client));	
		
		String[] client_fields = {
		         Contract.Client.FIELD_NAMES		         
		         };

		int[] client_toViews = {
		    		 R.id.textView_content};
		
		adapter = new SimpleCursorAdapter(this, 
	   	        R.layout.base_rowlist_simple_spinner, null, client_fields, client_toViews,0);
		
		setAdapter(adapter);	
				
	}
	
	@Override
	protected boolean onQueryTextChange(String newText) {		
		Cursor c = Client.getQueryClients(getDataBase(), newText);
		adapter.changeCursor(c);
		return true;
	}
	
	@Override
	protected boolean onQueryTextSubmit(String query) {		
		Cursor c = Client.getQueryClients(getDataBase(), query);
		adapter.changeCursor(c);
		return true;
	}
	
}
