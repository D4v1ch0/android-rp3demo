package ec.com.rp3.demo;

import java.util.List;
import ec.com.rp3.demo.R;
import ec.com.rp3.demo.models.Order;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter {

	static class ViewHolder {
		TextView textView_product;
		TextView textView_quantity;
	}
	
	List<Order> dataList;
	LayoutInflater inflater;
	
	public OrderAdapter(Context c,List<Order> data){
		this.dataList = data;
		inflater = LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {			
		return dataList.size();
	}

	@Override
	public Object getItem(int pos) {			
		return dataList.get(pos);
	}

	@Override
	public long getItemId(int pos) {			
		return ((Order)dataList.get(pos)).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {			
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.rowlist_order, null);
			
			holder = new ViewHolder();			
			holder.textView_product = (TextView)convertView.findViewById(R.id.textView_product);
			holder.textView_quantity = (TextView)convertView.findViewById(R.id.textView_quantity);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Order current = (Order)getItem(position);
		
		holder.textView_product.setText(current.getProduct());
		holder.textView_quantity.setText(String.valueOf(current.getQuantity()));
		
		return convertView;
	}
	
}
