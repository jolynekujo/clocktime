package com.yuta.clocktime.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuta.clocktime.R;
import com.yuta.clocktime.model.District;


public class CityAdapter extends BaseAdapter{
	private List<District> data = new ArrayList<District>();
	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	private Context context;

	public CityAdapter() {
		super();
	}
	
	public CityAdapter(List<District> data, Context context){
		this.data = data;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		viewHolder = new ViewHolder();
		if(convertView==null){
			view = inflater.inflate(R.layout.add_clock_item, null);
			viewHolder.flagImageView = (ImageView)view.findViewById(R.id.flag_id);
			viewHolder.cityTextView = (TextView)view.findViewById(R.id.city_name);
			viewHolder.zoneTextView = (TextView)view.findViewById(R.id.timezone);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		District city = data.get(position);
		viewHolder.flagImageView.setImageBitmap(getBitmap(context, city.getFlag()));
		viewHolder.cityTextView.setText(city.getCity());
		viewHolder.zoneTextView.setText(city.getTimezone());
		return view;
	}
	
	private Bitmap getBitmap(Context context, int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	class ViewHolder{
		ImageView flagImageView;
		TextView cityTextView;
		TextView zoneTextView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
