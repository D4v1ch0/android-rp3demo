package ec.com.rp3.demo.db;

import android.provider.BaseColumns;

public abstract class Contract {

	public abstract class Client implements BaseColumns {		
		public final static String TABLE_NAME = "tbClient";
		
		public final static String COLUMN_NAMES = "Names";
		
		public final static String FIELD_NAMES = COLUMN_NAMES;
	}
	
	public abstract class CodeScan implements BaseColumns {
		
		public final static String TABLE_NAME = "tbCodeScan";
		
		public final static String COLUMN_CODE = "Code";		
		public final static String COLUMN_SCAN_DATE = "ScanDate";
		public final static String COLUMN_LATITUDE = "Latitude";
		public final static String COLUMN_LONGITUDE = "Longitude";
		public final static String COLUMN_USER = "User";
		public final static String COLUMN_ADDRESS = "Address";
		
		
		public final static String FIELD_CODE = COLUMN_CODE;		
		public final static String FIELD_SCAN_DATE = COLUMN_SCAN_DATE;
		public final static String FIELD_LATITUDE = COLUMN_LATITUDE;
		public final static String FIELD_LONGITUDE = COLUMN_LONGITUDE;
		public final static String FIELD_USER = COLUMN_USER;
		public final static String FIELD_ADDRESS = COLUMN_ADDRESS;
		
	}
	
	public abstract class Order implements BaseColumns {
		
		public final static String TABLE_NAME = "tbOrder";
				
		public final static String COLUMN_PRODUCT = "Product";
		public final static String COLUMN_QUANTITY = "Quantity";
		public final static String COLUMN_ORDER_DATE = "OrderDate";
		public final static String COLUMN_CODE = "Code";
		public final static String COLUMN_LATITUDE = "Latitude";
		public final static String COLUMN_LONGITUDE = "Longitude";
		public final static String COLUMN_USER = "User";
		public final static String COLUMN_ADDRESS = "Address";
		public final static String COLUMN_CLIENT = "Client";
		
		public final static String FIELD_PRODUCT = COLUMN_PRODUCT;
		public final static String FIELD_QUANTITY = COLUMN_QUANTITY;
		public final static String FIELD_ORDER_DATE = COLUMN_ORDER_DATE;
		public final static String FIELD_CODE = COLUMN_CODE;				
		public final static String FIELD_LATITUDE = COLUMN_LATITUDE;
		public final static String FIELD_LONGITUDE = COLUMN_LONGITUDE;
		public final static String FIELD_USER = COLUMN_USER;
		public final static String FIELD_ADDRESS = COLUMN_ADDRESS;	
		public final static String FIELD_CLIENT = COLUMN_CLIENT;
	}
	
	public abstract class CheckList implements BaseColumns {
		
		public final static String TABLE_NAME = "tbCheckList";					
	      
	    public final static String COLUMN_DATE = "Date";
		public final static String COLUMN_LABEL = "Label";
		public final static String COLUMN_CHECK = "CheckValue";
		public final static String COLUMN_CODE = "Code";
		public final static String COLUMN_LATITUDE = "Latitude";
		public final static String COLUMN_LONGITUDE = "Longitude";
		public final static String COLUMN_USER = "User";
		public final static String COLUMN_ADDRESS = "Address";
		public final static String COLUMN_CLIENT = "Client";
		
		public final static String FIELD_DATE = COLUMN_DATE;
		public final static String FIELD_LABEL = COLUMN_LABEL;
		public final static String FIELD_CHECK = COLUMN_CHECK;
		public final static String FIELD_CODE = COLUMN_CODE;				
		public final static String FIELD_LATITUDE = COLUMN_LATITUDE;
		public final static String FIELD_LONGITUDE = COLUMN_LONGITUDE;
		public final static String FIELD_USER = COLUMN_USER;
		public final static String FIELD_ADDRESS = COLUMN_ADDRESS;
		public final static String FIELD_CLIENT = COLUMN_CLIENT;
	}
	
	public abstract class Product implements BaseColumns {
		
		public final static String TABLE_NAME = "tbProduct";					
		
	    public final static String COLUMN_NAME = "Name";
	    
	    public final static String FIELD_NAME = COLUMN_NAME;
	}
	
	public abstract class CheckListTemplate implements BaseColumns {
		
		public final static String TABLE_NAME = "tbCheckListTemplate";					
		
	    public final static String COLUMN_LABEL = "Label";
	    
	    public final static String FIELD_LABEL = COLUMN_LABEL;
	}	
	
}
