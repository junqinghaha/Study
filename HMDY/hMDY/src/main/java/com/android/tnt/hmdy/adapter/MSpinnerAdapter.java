package com.android.tnt.hmdy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.tnt.hmdy.R;

/***
 * 
 * 
 * @author TNT
 * 
 */
public class MSpinnerAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	private String[] arrDataStrings = null;

	public MSpinnerAdapter(Context context, String[] arrDatas) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrDataStrings = arrDatas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (arrDataStrings == null) {
			return 0;
		}
		return arrDataStrings.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (arrDataStrings == null) {
			return null;
		}
		return arrDataStrings[arg0];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (arrDataStrings != null && position < arrDataStrings.length) {
			convertView = mInflater.inflate(R.layout.item_spinner_list_show, parent, false);

			TextView tView1 = (TextView) convertView.findViewById(R.id.tv_spinner);
			tView1.setText(arrDataStrings[position]);
		}
		return convertView;
		// return super.getDropDownView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position < arrDataStrings.length) {
			convertView = mInflater.inflate(R.layout.item_spinner_show, parent, false);

			TextView tView1 = (TextView) convertView.findViewById(R.id.tv_spinner);
			tView1.setText(arrDataStrings[position]);
		}
		return convertView;
	}
}
