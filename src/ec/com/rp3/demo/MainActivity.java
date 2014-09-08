package ec.com.rp3.demo;

import java.util.List;

import ec.com.rp3.demo.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import rp3.app.NavActivity;
import rp3.app.nav.NavItem;

public class MainActivity extends NavActivity {

	public static final int NAV_SET_CODE = 1;
	public static final int NAV_CHECK_LIST = 2;
	public static final int NAV_ORDER = 3;
	
	
	public static Intent newIntent(Context c){
		Intent i = new Intent(c, MainActivity.class);
		return i;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
		
		if(savedInstanceState == null){
			int startNav = NAV_SET_CODE;			
			setNavigationSelection(startNav);  									
		}
						
		//showNavHeader(true);
		//setNavHeaderTitle(Session.getUser().getLogonName());		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {							
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (scanResult != null) {
		    setTextViewText(R.id.textView_code,scanResult.getContents());
		}else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	public void navConfig(List<NavItem> navItems, NavActivity currentActivity) {		
		super.navConfig(navItems, currentActivity);
		
		NavItem setCode = new NavItem(NAV_SET_CODE, R.string.title_option_setCode , R.drawable.ic_action_email);
		NavItem checklist = new NavItem(NAV_CHECK_LIST, R.string.title_option_checklist, 0);
		NavItem order = new NavItem(NAV_ORDER, R.string.title_option_order, 0);
		
		navItems.add(setCode);
		navItems.add(checklist);
		navItems.add(order);
	}
	
	@Override
	public void onNavItemSelected(NavItem item) {		
		super.onNavItemSelected(item);
		switch (item.getId()) {
		case NAV_SET_CODE:
			setNavFragment(CodeScanFragment.newInstance(), item.getTitle());
			break;
		case NAV_CHECK_LIST:
			setNavFragment(CheckListFragment.newInstance(), item.getTitle());
			break;
		case NAV_ORDER:
			setNavFragment(OrderFragment.newInstance(), item.getTitle());
			break;
		}
	}
}
