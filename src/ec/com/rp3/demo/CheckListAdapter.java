package ec.com.rp3.demo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import ec.com.rp3.demo.R;
import ec.com.rp3.demo.models.CheckListTemplate;

public class CheckListAdapter extends BaseAdapter{

	static class ViewHolder {
		TextView textView_text;
		CheckBox checkBox_check;
	}
	
	List<CheckListTemplate> dataList;
	LayoutInflater inflater;
	
	public CheckListAdapter(Context c,List<CheckListTemplate> data){
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
		return ((CheckListTemplate)dataList.get(pos)).getID();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {			
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.rowlist_checklist, null);
			
			holder = new ViewHolder();			
			holder.textView_text = (TextView)convertView.findViewById(R.id.textView_text);
			holder.checkBox_check = (CheckBox)convertView.findViewById(R.id.checkBox_checked);
			
			holder.checkBox_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {					
					CheckListTemplate current = (CheckListTemplate)getItem(position);
					current.setChecked(isChecked);
				}
			});
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		CheckListTemplate current = (CheckListTemplate)getItem(position);
		
		holder.textView_text.setText(current.getLabel());
		holder.checkBox_check.setChecked(current.checked());
		
		return convertView;
	}
	
}