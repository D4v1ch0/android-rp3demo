package ec.com.rp3.demo;

import ec.com.rp3.demo.accounts.MyServerAuthenticate;
import ec.com.rp3.demo.db.DbOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import rp3.configuration.Configuration;

public class StartActivity extends rp3.app.StartActivity {

	public StartActivity() {
		rp3.accounts.Authenticator.setServerAuthenticate(new MyServerAuthenticate());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);	
	}
	
	@Override
	public void onContinue() {		
		super.onContinue();
		
		new AsyncTask<Object, Integer, Boolean>() {

			@Override
			protected Boolean doInBackground(Object... params) {				
				SystemClock.sleep(1000);
				return true;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {								
				callNextActivity();
			}
		}.execute();
					
	}
	
	
	private void callNextActivity(){
		startActivity(MainActivity.newIntent(this));
		finish();
	}
}
