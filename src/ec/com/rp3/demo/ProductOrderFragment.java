package ec.com.rp3.demo;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import ec.com.rp3.demo.R;
import rp3.app.BaseFragment;

public class ProductOrderFragment extends BaseFragment {

	public static ProductOrderFragment newInstance(String product, long l){
		ProductOrderFragment f = new ProductOrderFragment();
		Bundle arg = new Bundle();
		arg.putString("PRODUCT", product);
		arg.putLong("QUANTITY", l);
		f.setArguments(arg);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_product);
		
		
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		
		getDialog().setTitle("Producto");
		
		String product = getArguments().getString("PRODUCT");
		long quantity = getArguments().getLong("QUANTITY");
		
		setTextViewText(R.id.textView_product, product);
		setTextViewText(R.id.editText_quantity, String.valueOf(quantity));	
		
		EditText editTextQ = (EditText)rootView.findViewById(R.id.editText_quantity);		
		editTextQ.selectAll();
		//editTextQ.requestFocus();
		getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		setButtonClickListener(R.id.button_cancel, new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED, null);
				finish();
			}
		});
		
		setButtonClickListener(R.id.button_save, new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle b = new Bundle();
				b.putLong("QUANTITY", Long.valueOf(getTextViewString(R.id.editText_quantity)));
				b.putString("PRODUCT", getTextViewString(R.id.textView_product));
				
				setResult(RESULT_OK, b);
				finish();
			}
		});
	}
	
	
}
