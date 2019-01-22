package com.pioneer.cradle.PLocProvider.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pioneer.cradle.PLocProvider.R;
import com.pioneer.cradle.PLocProvider.tools.DensityUtil;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

public class IconListPreference extends DialogPreference {
	
	private static final int SUMMARY_ICON_WIDTH_OR_HEIGHT_DP = 38;
	
	private int index = 0;
	private List<Item> entyList = new ArrayList<IconListPreference.Item>();
	private Drawable drawable;
	private AlertDialog dialog;
	
	public IconListPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public IconListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setSummary(Drawable icon, CharSequence summary){
		drawable = icon;
		super.setSummary(summary);
	}
	
	public void setSummary(Item summary){
		drawable = summary.icon;
		super.setSummary(summary.name);
		notifyChanged();
	}
	
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		TextView v = (TextView) view.findViewById(android.R.id.summary);
		v.setGravity(Gravity.CENTER_VERTICAL);
		if(drawable != null) {
			v.setCompoundDrawablesWithIntrinsicBounds(getDrawable(drawable), null, null, null);
		}
	}
	
	@Override
	protected void showDialog(Bundle state) {
		if(entyList.size() == 0) {
			Toast.makeText(getContext(), R.string.no_option_tip, Toast.LENGTH_SHORT).show();
			return;
		}
		if(dialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setNegativeButton(android.R.string.cancel, null);
			dialog = builder.create();
			dialog.setView(getListView(dialog));
		}
		dialog.show();
	}
	
	
	private View getListView(final Dialog dialog) {
		ListView list = new ListView(getContext());
		list.setCacheColorHint(0);
		SimpleAdapter simple = new SimpleAdapter(getContext(), getNaviData(), R.layout.installed_app_list, new String[]{"icon", "name"}, new int[]{R.id.icon, R.id.text});
		simple.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if(view instanceof ImageView && data instanceof Drawable) {
					((ImageView)view).setImageDrawable((Drawable) data);
					return true;
				}
				return false;
			}
		});
		list.setAdapter(simple);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(index != position)
				{
					seletedIndex(position);
					callChangeListener(position);
				}
				dialog.dismiss();
			}
		});
		return list;
	}
	
	private List<? extends Map<String, ?>> getNaviData() {
		List<HashMap<String, ?>> dataList = new ArrayList<HashMap<String,?>>(entyList.size());
		
		for(Item item: entyList) {
			HashMap<String, Object> map = new HashMap<String, Object>(2);
			map.put("icon", item.icon);
			map.put("name", item.name);
			dataList.add(map);
		}
		
		return dataList;
	}
	
	public void setEnties(Drawable[] drawable, String[] enties){
		if(drawable.length != enties.length) {
			throw new RuntimeException("drawable.length must be equals enties.length");
		}
		
		entyList.clear();
		for(int i=0; i<drawable.length; i++){
			Item item = new Item(drawable[i], enties[i]);
			entyList.add(item);
		}
		setsummaryText();
	}

	private void setsummaryText(){
		if(index<0 || index>=entyList.size()) {
			setSummary("");
			return;
		}
		if(entyList.size()>0){
			setSummary(entyList.get(index));
			
		}
	}

	public void seletedIndex(int index){
		this.index = index;
		setsummaryText();
	}
	
	public void setEnties(List<Item> list) {
		entyList.clear();
		entyList.addAll(list);
	}
	
	
	public static final class Item{
		private Drawable icon;
		private String name;
		public Item(Drawable icon, String name) {
			this.icon = icon;
			this.name = name;
		}
		
		@Override
		public String toString() {
			return "Item [icon=" + icon + ", name=" + name + "]";
		}
		
	}
	
	private static Bitmap transBitmap(Bitmap bitmap, float sx, float sy) {
		  Matrix matrix = new Matrix();
		  matrix.postScale(sx,sy); 
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
  }
	
	private static Bitmap transBitmap(Bitmap bitmap, int width, int height) {
		  float sx = width/(float)bitmap.getWidth();
		  float sy = height/(float)bitmap.getHeight();
		  return transBitmap(bitmap, sx, sy);
  }
	
	private Drawable getDrawable(Drawable d) {
		BitmapDrawable drawable = (BitmapDrawable) d;
		Bitmap bm = drawable.getBitmap();
		
		int width = DensityUtil.dp2px(getContext(), SUMMARY_ICON_WIDTH_OR_HEIGHT_DP);
		int height = width;
		Bitmap newbm = transBitmap(bm, width, height);
		return new BitmapDrawable(newbm);
	}

}
